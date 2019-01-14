package com.github.verils.transdoc.core;

public class WordParsingException extends TransdocException {
	private static final long serialVersionUID = -532689023986569011L;

	public WordParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public WordParsingException(String message) {
		super(message);
	}

	public WordParsingException(Throwable cause) {
		super(cause);
	}
}