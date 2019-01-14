package com.github.verils.transdoc.web.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response<Object> handleNullPointerException(NullPointerException e) {
		LOGGER.error(e.getMessage(), e);
		return Response.fail(e.getClass().getName() + ": " + e.getMessage());
	}

}
