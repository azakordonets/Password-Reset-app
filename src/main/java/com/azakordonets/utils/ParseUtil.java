package com.azakordonets.utils;

public final class ParseUtil {

    public static int parseInt(String intProperty) {
        try {
            return Integer.parseInt(intProperty);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(intProperty + " not a number. " + nfe.getMessage());
        }
    }
}
