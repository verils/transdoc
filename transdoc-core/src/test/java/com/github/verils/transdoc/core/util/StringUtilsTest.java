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
}
