package com.sunshine.shiro.redis.autoconfigure.lettuce;

import com.sunshine.shiro.redis.autoconfigure.ShiroRedisProperties;
import io.lettuce.core.RedisClient;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.lettuce.manager.LettuceRedisManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Teamo
 * @since 2022/09/27
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisClient.class)
@ConditionalOnProperty(name = "shiro.redis.client-type", havingValue = "lettuce", matchIfMissing = true)
public class LettuceRedisConfiguration extends LettuceConfiguration {

    protected LettuceRedisConfiguration(ShiroRedisProperties shiroRedisProperties) {
        super(shiroRedisProperties);
    }

    @Bean
    @Override
    @ConditionalOnMissingBean
    public IRedisManager redisManager() {
        return super.redisManager();
    }
}