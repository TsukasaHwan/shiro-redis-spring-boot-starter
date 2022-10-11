package com.sunshine.shiro.redis.autoconfigure;

import com.sunshine.shiro.redis.autoconfigure.jedis.JedisRedisConfiguration;
import com.sunshine.shiro.redis.autoconfigure.lettuce.LettuceRedisConfiguration;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

/**
 * @author Teamo
 * @since 2022/09/22
 */
@AutoConfiguration
@EnableConfigurationProperties(value = {ShiroRedisProperties.class})
@ConditionalOnProperty(value = "shiro.redis.enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(value = {RedisSessionDAO.class, RedisCacheManager.class})
@Import({LettuceRedisConfiguration.class, JedisRedisConfiguration.class})
class ShiroRedisAutoConfiguration extends ShiroRedisConfiguration {

    ShiroRedisAutoConfiguration(ShiroRedisProperties shiroRedisProperties) {
        super(shiroRedisProperties);
    }

    @Bean
    @ConditionalOnMissingBean(RedisSessionDAO.class)
    @ConditionalOnProperty(value = "shiro.redis.session.enable", havingValue = "true", matchIfMissing = true)
    RedisSessionDAO redisSessionDAO(IRedisManager redisManager) {
        ShiroRedisProperties.Session session = getProperties().getSession();
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        if (StringUtils.hasText(session.getKeyPrefix())) {
            redisSessionDAO.setKeyPrefix(session.getKeyPrefix() + redisSessionDAO.getKeyPrefix());
        }
        redisSessionDAO.setExpire((int) session.getExpire().getSeconds());
        redisSessionDAO.setSessionInMemoryEnabled(session.getSessionInMemoryEnabled());
        redisSessionDAO.setSessionInMemoryTimeout(session.getSessionInMemoryTimeout().toMillis());
        return redisSessionDAO;
    }

    @Bean
    @ConditionalOnMissingBean(RedisCacheManager.class)
    RedisCacheManager redisCacheManager(IRedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        ShiroRedisProperties.Cache cache = getProperties().getCache();
        if (StringUtils.hasText(cache.getPrincipalIdFieldName())) {
            redisCacheManager.setPrincipalIdFieldName(cache.getPrincipalIdFieldName());
        }
        if (StringUtils.hasText(cache.getKeyPrefix())) {
            redisCacheManager.setKeyPrefix(cache.getKeyPrefix() + redisCacheManager.getKeyPrefix());
        }
        redisCacheManager.setExpire((int) cache.getExpire().getSeconds());
        return redisCacheManager;
    }
}
