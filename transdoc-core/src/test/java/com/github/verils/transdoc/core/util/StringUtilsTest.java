package com.github.verils.transdoc.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void hasText() {
        assertTrue(StringUtils.hasText(" a"));
        assertTrue(StringUtils.hasText("a"));
        assertFalse(StringUtils.hasText(" "));
        assertFalse(StringUtils.hasText(""));
        assertFalse(StringUtils.hasText(null));
    }

    @Test
    public void numberToString() {
        assertEquals("0", StringUtils.numberToString(0, 0));
        assertEquals("0", StringUtils.numberToString(0, 1));
        assertEquals("00", StringUtils.numberToString(0, 2));
        assertEquals("0000", StringUtils.numberToString(0, 4));
        assertEquals("0001", StringUtils.numberToString(1, 4));
        assertEquals("10001", StringUtils.numberToString(10001, 4));
    }
}
