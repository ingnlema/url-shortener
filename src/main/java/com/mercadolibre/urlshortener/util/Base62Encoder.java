package com.mercadolibre.urlshortener.util;


public class Base62Encoder {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }
        return sb.reverse().toString();
    }

    public static long decode(String value) {
        long result = 0;
        long power = 1;
        for (int i = value.length() - 1; i >= 0; i--) {
            int index = BASE62.indexOf(value.charAt(i));
            result += index * power;
            power *= 62;
        }
        return result;
    }
}
