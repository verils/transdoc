package com.github.verils.transdoc.core;

public class TransdocException extends RuntimeException {
	private static final long serialVersionUID = -7103413496472730725L;

	public TransdocException(String message) {
		super(message);
	}

	public TransdocException(Throwable cause) {
		super(cause);
	}

	public TransdocException(String message, Throwable cause) {
		super(message, cause);
	}
}