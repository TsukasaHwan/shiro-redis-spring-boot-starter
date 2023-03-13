package com.sunshine.shiro.redis.autoconfigure.lettuce;

import com.sunshine.shiro.redis.autoconfigure.ShiroRedisProperties;
import io.lettuce.core.RedisClient;
import org.crazycake.shiro.IRedisManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Teamo
 * @since 2022/09/27
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisClient.class)
@ConditionalOnProperty(name = "shiro.redis.client-type", havingValue = "lettuce", matchIfMissing = true)
public class LettuceRedisConfiguration extends LettuceConfiguration {

    public LettuceRedisConfiguration(ShiroRedisProperties shiroRedisProperties) {
        super(shiroRedisProperties);
    }

    @Bean
    @Override
    @ConditionalOnMissingBean
    public IRedisManager redisManager() {
        return super.redisManager();
    }
}
