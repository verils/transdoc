package com.github.verils.transdoc.core;

public class StringUtils {
	private static final char WHITE_SPACE_CHAR = ' ';

	public static String rtrim(String str) {
		int len = str.length();
		char[] val = str.toCharArray();
		while ((len > 0) && (val[(len - 1)] <= WHITE_SPACE_CHAR)) {
			--len;
		}
		return str.substring(0, len);
	}
}