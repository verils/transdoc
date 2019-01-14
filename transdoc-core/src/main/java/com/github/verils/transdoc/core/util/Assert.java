package com.github.verils.transdoc.core.util;

import java.util.Objects;

public class Assert {

    public static void notNull(String message, Object obj) {
        Objects.requireNonNull(obj, message);
    }
}
