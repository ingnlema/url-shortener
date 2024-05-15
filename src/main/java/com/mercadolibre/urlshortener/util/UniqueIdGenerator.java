package com.mercadolibre.urlshortener.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;


@Component
public class UniqueIdGenerator {

    private static final int JUMP = 30;
    private static final String ID_KEY = "url_shortener_id";
    private final StringRedisTemplate stringRedisTemplate;


    @Autowired
    public UniqueIdGenerator(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long generateUniqueId() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Long id = ops.increment(ID_KEY, JUMP);
        if (id == null) {
            throw new IllegalStateException("Failed to increment Redis counter");
        }
        return id;
    }

}
