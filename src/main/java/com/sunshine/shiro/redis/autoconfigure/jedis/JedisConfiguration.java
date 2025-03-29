package com.sunshine.shiro.redis.autoconfigure.jedis;

import com.sunshine.shiro.redis.autoconfigure.ShiroRedisConfiguration;
import com.sunshine.shiro.redis.autoconfigure.ShiroRedisProperties;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSentinelManager;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author Teamo
 * @since 2022/09/28
 */
abstract class JedisConfiguration extends ShiroRedisConfiguration {
    protected JedisConfiguration(ShiroRedisProperties shiroRedisProperties) {
        super(shiroRedisProperties);
    }

    protected IRedisManager redisManager() {
        ShiroRedisProperties.Cluster cluster = getProperties().getCluster();
        ShiroRedisProperties.Sentinel sentinel = getProperties().getSentinel();
        if (sentinel != null) {
            return getJedisRedisSentinelManager();
        } else if (cluster != null) {
            return getJedisRedisClusterManager();
        }
        return getStandaloneJedisRedisManager();
    }

    /**
     * 哨兵模式JedisRedisManager
     *
     * @return 完整配置的JedisRedisSentinelManager
     */
    protected RedisSentinelManager getJedisRedisSentinelManager() {
        ShiroRedisProperties.Sentinel sentinel = getProperties().getSentinel();
        PropertyMapper mapper = PropertyMapper.get();
        RedisSentinelManager redisSentinelManager = new RedisSentinelManager();
        mapper.from(sentinel.getNodes()).whenNonNull().as(StringUtils::collectionToCommaDelimitedString).to(redisSentinelManager::setHost);
        mapper.from(sentinel.getMaster()).whenHasText().to(redisSentinelManager::setMasterName);
        mapper.from(getProperties().getConnectTimeout()).whenNonNull().asInt(Duration::toMillis).to(redisSentinelManager::setTimeout);
        mapper.from(getProperties().getTimeout()).whenNonNull().asInt(Duration::toMillis).to(redisSentinelManager::setSoTimeout);
        mapper.from(sentinel.getPassword()).whenHasText().to(redisSentinelManager::setPassword);
        redisSentinelManager.setDatabase(getProperties().getDatabase());
        redisSentinelManager.setCount(getProperties().getCount());
        redisSentinelManager.setJedisPoolConfig(getPoolConfig());
        return redisSentinelManager;
    }

    /**
     * 集群模式JedisRedisManager
     *
     * @return 完整配置的JedisRedisClusterManager
     */
    protected RedisClusterManager getJedisRedisClusterManager() {
        PropertyMapper mapper = PropertyMapper.get();
        ShiroRedisProperties.Cluster cluster = getProperties().getCluster();
        RedisClusterManager redisClusterManager = new RedisClusterManager();
        mapper.from(cluster.getNodes()).whenNonNull().as(StringUtils::collectionToCommaDelimitedString).to(redisClusterManager::setHost);
        mapper.from(getProperties().getConnectTimeout()).whenNonNull().asInt(Duration::toMillis).to(redisClusterManager::setTimeout);
        mapper.from(getProperties().getTimeout()).whenNonNull().asInt(Duration::toMillis).to(redisClusterManager::setSoTimeout);
        mapper.from(getProperties().getPassword()).whenHasText().to(redisClusterManager::setPassword);
        redisClusterManager.setDatabase(getProperties().getDatabase());
        redisClusterManager.setCount(getProperties().getCount());
        mapper.from(cluster.getMaxRedirects()).whenNonNull().to(redisClusterManager::setMaxAttempts);
        redisClusterManager.setJedisPoolConfig(getPoolConfig());
        return redisClusterManager;
    }

    /**
     * 单机模式JedisRedisManager
     *
     * @return 完整配置的JedisRedisManager
     */
    protected RedisManager getStandaloneJedisRedisManager() {
        PropertyMapper mapper = PropertyMapper.get();
        RedisManager redisManager = new RedisManager();
        String host = getProperties().getHost() + ":" + getProperties().getPort();
        redisManager.setHost(host);
        mapper.from(getProperties().getConnectTimeout()).whenNonNull().asInt(Duration::toMillis).to(redisManager::setTimeout);
        mapper.from(getProperties().getPassword()).whenHasText().to(redisManager::setPassword);
        redisManager.setDatabase(getProperties().getDatabase());
        redisManager.setCount(getProperties().getCount());
        redisManager.setJedisPoolConfig(getPoolConfig());
        return redisManager;
    }

    private JedisPoolConfig getPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        applyPoolProperties(poolConfig, getProperties().getJedis().getPool());
        return poolConfig;
    }
}
