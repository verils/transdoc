package com.github.verils.transdoc.core.util;

public abstract class StringUtils {

    public static boolean hasText(String text) {
        return text != null && text.trim().length() > 0;
    }

    public static String numberToString(int num, int length) {
        String numStr = String.valueOf(num);
        StringBuilder builder = new StringBuilder();
        if (numStr.length() < length) {
            int pre = length - numStr.length();
            for (int i = 0; i < pre; i++) {
                builder.append("0");
            }
        }
        builder.append(numStr);
        return builder.toString();
    }
}
