package com.mercadolibre.urlshortener.config;

import com.mercadolibre.urlshortener.exception.DatabaseConnectionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.beans.factory.annotation.Value;


@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        try{
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
            config.setPassword(RedisPassword.of(redisPassword));
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
