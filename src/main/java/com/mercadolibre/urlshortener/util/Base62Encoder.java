package com.mercadolibre.urlshortener.util;

public class Base62Encoder {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public static String encode(long value) {
        if (value == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % BASE)));
            value /= BASE;
        }
        return sb.reverse().toString();
    }

    public static long decode(String value) {
        long result = 0;
        for (int i = 0; i < value.length(); i++) {
            int index = BASE62.indexOf(value.charAt(i));
            if (index == -1) {
                throw new StringIndexOutOfBoundsException("Character '" + value.charAt(i) + "' is not a valid Base62 character.");
            }
            result = result * BASE + index;
        }
        return result;
    }
}