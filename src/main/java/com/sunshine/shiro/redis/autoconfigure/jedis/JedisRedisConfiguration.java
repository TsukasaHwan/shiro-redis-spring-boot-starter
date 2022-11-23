package com.sunshine.shiro.redis.autoconfigure.jedis;

import com.sunshine.shiro.redis.autoconfigure.ShiroRedisProperties;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.crazycake.shiro.IRedisManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * @author Teamo
 * @since 2022/09/27
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({GenericObjectPool.class, Jedis.class})
@ConditionalOnProperty(name = "shiro.redis.client-type", havingValue = "jedis", matchIfMissing = true)
public class JedisRedisConfiguration extends JedisConfiguration {

    protected JedisRedisConfiguration(ShiroRedisProperties shiroRedisProperties) {
        super(shiroRedisProperties);
    }

    @Bean
    @Override
    @ConditionalOnMissingBean
    protected IRedisManager redisManager() {
        return super.redisManager();
    }
}
