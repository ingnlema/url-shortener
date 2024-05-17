package com.mercadolibre.urlshortener.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UniqueIdGeneratorTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private UniqueIdGenerator uniqueIdGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testGenerateUniqueId_Success() {
        when(valueOperations.increment(anyString(), anyLong())).thenReturn(100L);

        long id = uniqueIdGenerator.generateUniqueId();

        assertEquals(100L, id);
    }

    @Test
    public void testGenerateUniqueId_Failure() {
        when(valueOperations.increment(anyString(), anyLong())).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> uniqueIdGenerator.generateUniqueId());
    }

}