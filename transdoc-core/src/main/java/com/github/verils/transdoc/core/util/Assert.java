package com.github.verils.transdoc.core.util;

public class Assert {

    public static void notNull(String message, Object obj) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }
}
