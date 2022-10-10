package com.sunshine.shiro.redis.autoconfigure.lettuce;

import com.sunshine.shiro.redis.autoconfigure.ShiroRedisConfiguration;
import com.sunshine.shiro.redis.autoconfigure.ShiroRedisProperties;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.lettuce.manager.LettuceRedisClusterManager;
import org.crazycake.shiro.lettuce.manager.LettuceRedisManager;
import org.crazycake.shiro.lettuce.manager.LettuceRedisSentinelManager;
import org.springframework.boot.context.properties.PropertyMapper;

import java.time.Duration;

/**
 * @author Teamo
 * @since 2022/09/23
 */
@SuppressWarnings("unchecked")
abstract class LettuceConfiguration extends ShiroRedisConfiguration {

    protected LettuceConfiguration(ShiroRedisProperties shiroRedisProperties) {
        super(shiroRedisProperties);
    }

    protected IRedisManager redisManager() {
        ShiroRedisProperties.Cluster cluster = getProperties().getCluster();
        ShiroRedisProperties.Sentinel sentinel = getProperties().getSentinel();
        if (sentinel != null) {
            return getLettuceRedisSentinelManager();
        } else if (cluster != null) {
            return getLettuceRedisClusterManager();
        }
        return getStandaloneLettuceRedisManager();
    }

    /**
     * 哨兵模式LettuceRedisManager
     *
     * @return 完整配置的LettuceRedisSentinelManager
     */
    protected LettuceRedisSentinelManager getLettuceRedisSentinelManager() {
        PropertyMapper mapper = PropertyMapper.get();
        LettuceRedisSentinelManager lettuceRedisSentinelManager = new LettuceRedisSentinelManager();
        ShiroRedisProperties.Sentinel sentinel = getProperties().getSentinel();
        mapper.from(sentinel.getMaster()).whenHasText().to(lettuceRedisSentinelManager::setMasterName);
        mapper.from(sentinel.getNodes()).whenNonNull().to(lettuceRedisSentinelManager::setNodes);
        Duration connectTimeout = getProperties().getConnectTimeout();
        if (connectTimeout != null) {
            lettuceRedisSentinelManager.setClientOptions(ClientOptions.builder().socketOptions(SocketOptions.builder().connectTimeout(connectTimeout).build()).build());
        }
        mapper.from(getProperties().getPassword()).whenHasText().to(lettuceRedisSentinelManager::setPassword);
        mapper.from(sentinel.getPassword()).whenHasText().to(lettuceRedisSentinelManager::setSentinelPassword);
        mapper.from(getProperties().getLettuce().getAsync()).whenNonNull().to(lettuceRedisSentinelManager::setIsAsync);
        lettuceRedisSentinelManager.setDatabase(getProperties().getDatabase());
        lettuceRedisSentinelManager.setCount(getProperties().getCount());
        mapper.from(sentinel.getReadFromType()).whenNonNull().as(readFromType -> ReadFrom.valueOf(readFromType.name())).to(lettuceRedisSentinelManager::setReadFrom);
        lettuceRedisSentinelManager.setGenericObjectPoolConfig((GenericObjectPoolConfig<StatefulRedisMasterReplicaConnection<byte[], byte[]>>) getPoolConfig());
        return lettuceRedisSentinelManager;
    }

    /**
     * 集群模式LettuceRedisManager
     *
     * @return 完整配置的LettuceRedisClusterManager
     */
    protected LettuceRedisClusterManager getLettuceRedisClusterManager() {
        LettuceRedisClusterManager lettuceRedisClusterManager = new LettuceRedisClusterManager();
        PropertyMapper mapper = PropertyMapper.get();
        ShiroRedisProperties.Cluster cluster = getProperties().getCluster();
        mapper.from(cluster.getNodes()).whenNonNull().to(lettuceRedisClusterManager::setNodes);
        mapper.from(getProperties().getPassword()).whenHasText().to(lettuceRedisClusterManager::setPassword);
        mapper.from(getProperties().getLettuce().getAsync()).whenNonNull().to(lettuceRedisClusterManager::setIsAsync);
        lettuceRedisClusterManager.setDatabase(getProperties().getDatabase());
        lettuceRedisClusterManager.setCount(getProperties().getCount());
        lettuceRedisClusterManager.setClusterClientOptions(getClusterClientOptions(lettuceRedisClusterManager));
        lettuceRedisClusterManager.setGenericObjectPoolConfig((GenericObjectPoolConfig<StatefulRedisClusterConnection<byte[], byte[]>>) getPoolConfig());
        return lettuceRedisClusterManager;
    }

    /**
     * 单机模式LettuceRedisManager
     *
     * @return 完整配置的LettuceRedisManager
     */
    protected LettuceRedisManager getStandaloneLettuceRedisManager() {
        LettuceRedisManager lettuceRedisManager = new LettuceRedisManager();
        PropertyMapper mapper = PropertyMapper.get();
        mapper.from(getProperties().getPassword()).whenHasText().to(lettuceRedisManager::setPassword);
        mapper.from(getProperties().getHost()).whenHasText().to(lettuceRedisManager::setHost);
        mapper.from(getProperties().getPort()).to(lettuceRedisManager::setPort);
        Duration connectTimeout = getProperties().getConnectTimeout();
        if (connectTimeout != null) {
            lettuceRedisManager.setClientOptions(ClientOptions.builder().socketOptions(SocketOptions.builder().connectTimeout(connectTimeout).build()).build());
        }
        mapper.from(getProperties().getTimeout()).whenNonNull().to(lettuceRedisManager::setTimeout);
        mapper.from(getProperties().getLettuce().getAsync()).whenNonNull().to(lettuceRedisManager::setIsAsync);
        lettuceRedisManager.setDatabase(getProperties().getDatabase());
        lettuceRedisManager.setCount(getProperties().getCount());
        lettuceRedisManager.setGenericObjectPoolConfig((GenericObjectPoolConfig<StatefulRedisConnection<byte[], byte[]>>) getPoolConfig());
        return lettuceRedisManager;
    }

    /**
     * 获取集群客户端配置
     *
     * @param lettuceRedisClusterManager LettuceRedisClusterManager
     * @return 集群客户端配置
     */
    private ClusterClientOptions getClusterClientOptions(LettuceRedisClusterManager lettuceRedisClusterManager) {
        ShiroRedisProperties.Cluster cluster = getProperties().getCluster();
        Duration connectTimeout = getProperties().getConnectTimeout();
        ClusterClientOptions clusterClientOptions = lettuceRedisClusterManager.getClusterClientOptions();
        if (connectTimeout != null) {
            lettuceRedisClusterManager.setClusterClientOptions(clusterClientOptions.mutate().socketOptions(SocketOptions.builder().connectTimeout(connectTimeout).build()).build());
        }
        Integer maxRedirects = cluster.getMaxRedirects();
        if (maxRedirects != null) {
            lettuceRedisClusterManager.setClusterClientOptions(clusterClientOptions.mutate().maxRedirects(maxRedirects).build());
        }
        return clusterClientOptions;
    }
}
