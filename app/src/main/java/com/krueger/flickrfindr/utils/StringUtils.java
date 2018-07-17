package com.krueger.flickrfindr.utils;

public class StringUtils {

    public static String getSafeString(String value) {
        return value == null ? "" : value;
    }

    public static String getSafeString(Integer value) {
        return value == null ? "" : value.toString();
    }
}
