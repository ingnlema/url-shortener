package com.mercadolibre.urlshortener.config;

import com.mercadolibre.urlshortener.exception.DatabaseConnectionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;


@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        try{
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("redis", 6379);
            config.setPassword(RedisPassword.of("pass123"));
            return new LettuceConnectionFactory(config);
        }catch(Exception e){
            throw new DatabaseConnectionException("Error al intentar conectar a Redis: " + e.getMessage());

        }

    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        try{
            RedisTemplate<?, ?> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            return template;
        }catch(Exception e){
            throw new DatabaseConnectionException("Error al configurar redis template: " + e.getMessage());

        }

    }
}
