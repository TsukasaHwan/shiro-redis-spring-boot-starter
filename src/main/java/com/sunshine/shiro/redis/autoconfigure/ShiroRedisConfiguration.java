package com.sunshine.shiro.redis.autoconfigure;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

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

    protected void applyPoolProperties(GenericObjectPoolConfig<?> poolConfig, ShiroRedisProperties.Pool pool) {
        poolConfig.setMaxTotal(pool.getMaxActive());
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxWait(pool.getMaxWait());
        if (pool.getTimeBetweenEvictionRuns() != null) {
            poolConfig.setTimeBetweenEvictionRuns(pool.getTimeBetweenEvictionRuns());
        }
    }
}
