package com.bryant.zj.exception;

public class ParameterException extends BusinessExcepion {

	private static final long serialVersionUID = 1L;

	public ParameterException() {
	}

	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(Throwable cause) {
		super(cause);
	}

}
