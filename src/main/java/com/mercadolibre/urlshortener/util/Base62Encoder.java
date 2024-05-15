package com.mercadolibre.urlshortener.util;

public class Base62Encoder {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int[] BASE62_INDEX = new int[128]; // ASCII size coverage for fast indexing
    private static final long[] POWERS_OF_62 = new long[11]; // Pre-computed powers of 62

    static {
        for (int i = 0; i < BASE62.length(); i++) {
            BASE62_INDEX[BASE62.charAt(i)] = i;
        }
        for (int i = 0; i < POWERS_OF_62.length; i++) {
            POWERS_OF_62[i] = (long) Math.pow(62, i);
        }
    }

    public static String encode(long value) {
        if (value == 0) return "0";
        char[] buffer = new char[11]; // Buffer size based on Long.MAX_VALUE length in base 62
        int index = buffer.length;
        while (value > 0) {
            buffer[--index] = BASE62.charAt((int) (value % 62));
            value /= 62;
        }
        return new String(buffer, index, buffer.length - index);
    }

    public static long decode(String value) {
        long result = 0;
        for (int i = value.length() - 1, powerIdx = 0; i >= 0; i--, powerIdx++) {
            result += BASE62_INDEX[value.charAt(i)] * POWERS_OF_62[powerIdx];
        }
        return result;
    }
}