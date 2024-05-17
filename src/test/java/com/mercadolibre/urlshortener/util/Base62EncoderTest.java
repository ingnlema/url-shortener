package com.mercadolibre.urlshortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Base62EncoderTest {

    @Test
    public void testEncode() {
        assertEquals("0", Base62Encoder.encode(0));
        assertEquals("1", Base62Encoder.encode(1));
        assertEquals("a", Base62Encoder.encode(10));
        assertEquals("z", Base62Encoder.encode(35));
        assertEquals("A", Base62Encoder.encode(36));
        assertEquals("Z", Base62Encoder.encode(61));
        assertEquals("10", Base62Encoder.encode(62));
        assertEquals("1Z", Base62Encoder.encode(123));
        assertEquals("aZl8N0y58M7", Base62Encoder.encode(Long.MAX_VALUE));
    }

    @Test
    public void testDecode() {
        assertEquals(0L, Base62Encoder.decode("0"));
        assertEquals(1L, Base62Encoder.decode("1"));
        assertEquals(10L, Base62Encoder.decode("a"));
        assertEquals(35L, Base62Encoder.decode("z"));
        assertEquals(36L, Base62Encoder.decode("A"));
        assertEquals(61L, Base62Encoder.decode("Z"));
        assertEquals(62L, Base62Encoder.decode("10"));
        assertEquals(123L, Base62Encoder.decode("1Z"));
        assertEquals(Long.MAX_VALUE, Base62Encoder.decode("aZl8N0y58M7"));
    }

    @Test
    public void testEncodeDecodeConsistency() {
        long[] values = {0L, 1L, 10L, 62L, 123L, 4567L, 89012L, 123456789L, Long.MAX_VALUE};
        for (long value : values) {
            String encoded = Base62Encoder.encode(value);
            long decoded = Base62Encoder.decode(encoded);
            assertEquals(value, decoded, "Failed for value: " + value);
        }
    }

    @Test
    public void testDecodeInvalidCharacters() {
        assertThrows(StringIndexOutOfBoundsException.class, () -> Base62Encoder.decode("!"));
        assertThrows(StringIndexOutOfBoundsException.class, () -> Base62Encoder.decode("@"));
        assertThrows(StringIndexOutOfBoundsException.class, () -> Base62Encoder.decode("["));
    }
}