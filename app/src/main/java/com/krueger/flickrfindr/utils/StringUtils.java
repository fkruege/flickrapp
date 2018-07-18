package com.krueger.flickrfindr.utils;

public class StringUtils {

    public static String getSafeString(String value) {
        return value == null ? "" : value;
    }

    public static String getSafeString(Integer value) {
        return value == null ? "" : value.toString();
    }

    public static int getSafeInt(Integer value) {
        return value == null ? 0 : value;
    }

    public static int getSafeInt(String value) {
        if (value == null) {
            return 0;
        }

        int asInt = 0;

        try {
            asInt = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            asInt = 0;
        }
        return asInt;
    }
}
