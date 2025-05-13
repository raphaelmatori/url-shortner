package com.urlshortener.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class RedisConfig {
    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory("redis", 6379)

    @Bean
    fun redisTemplate() = StringRedisTemplate(redisConnectionFactory())
}
