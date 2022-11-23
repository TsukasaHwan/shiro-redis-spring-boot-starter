package com.sunshine.shiro.redis.autoconfigure;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Teamo
 * @since 2022/09/23
 */
public abstract class ShiroRedisConfiguration {
    private final ShiroRedisProperties shiroRedisProperties;

    protected ShiroRedisConfiguration(ShiroRedisProperties shiroRedisProperties) {
        this.shiroRedisProperties = shiroRedisProperties;
    }

    protected ShiroRedisProperties getProperties() {
        return this.shiroRedisProperties;
    }

    /**
     * 连接池配置
     *
     * @return 完整配置的连接池配置
     */
    protected GenericObjectPoolConfig<?> getPoolConfig() {
        ShiroRedisProperties.ClientType clientType = getProperties().getClientType();
        return ShiroRedisProperties.ClientType.JEDIS.equals(clientType) ? getJedisPool() : getPool();
    }

    private JedisPoolConfig getJedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        applyPoolProperties(poolConfig, getProperties().getJedis().getPool());
        return poolConfig;
    }

    private GenericObjectPoolConfig<?> getPool() {
        GenericObjectPoolConfig<?> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        applyPoolProperties(genericObjectPoolConfig, getProperties().getLettuce().getPool());
        return genericObjectPoolConfig;
    }

    private void applyPoolProperties(GenericObjectPoolConfig<?> poolConfig, ShiroRedisProperties.Pool pool) {
        poolConfig.setMaxTotal(pool.getMaxActive());
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxWait(pool.getMaxWait());
        if (pool.getTimeBetweenEvictionRuns() != null) {
            poolConfig.setTimeBetweenEvictionRuns(pool.getTimeBetweenEvictionRuns());
        }
    }
}
