package com.dalessandro.ManagerSystem.helpers;

public final class StringHelper {
    private StringHelper() {}

    public static String capitalize(String text) {
        if (text.length() <= 1) {
            return text.toUpperCase();
        }

        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
