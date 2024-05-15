package com.mercadolibre.urlshortener.util;


import java.util.HashMap;
import java.util.Map;

public class Base62Encoder {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Map<Character, Integer> BASE62_INDEX = new HashMap<>();
    static {
        for (int i = 0; i < BASE62.length(); i++) {
            BASE62_INDEX.put(BASE62.charAt(i), i);
        }
    }

    public static String encode(long value) {
        if (value == 0) return "0";
        char[] buffer = new char[11]; // Long.MAX_VALUE in base 62 is 10 characters
        int index = buffer.length;
        while (value > 0) {
            buffer[--index] = BASE62.charAt((int) (value % 62));
            value /= 62;
        }
        return new String(buffer, index, buffer.length - index);
    }

    public static long decode(String value) {
        long result = 0;
        long power = 1;
        for (int i = value.length() - 1; i >= 0; i--) {
            result += BASE62_INDEX.get(value.charAt(i)) * power;
            power *= 62;
        }
        return result;
    }
}

