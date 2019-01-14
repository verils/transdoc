package com.github.verils.transdoc.web.test.matcher;

import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class UUIDMatcher extends BaseMatcher<String> {

	private static final Pattern UUID_PATTERN = Pattern.compile("[a-z0-9]{32}");

	@Override
	public boolean matches(Object item) {
		return item instanceof String && UUID_PATTERN.matcher((String) item).matches();
	}

	@Override
	public void describeTo(Description description) {
	}

	public static UUIDMatcher isUUID() {
		return new UUIDMatcher();
	}
}
