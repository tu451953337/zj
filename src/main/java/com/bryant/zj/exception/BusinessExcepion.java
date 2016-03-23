package com.bryant.zj.exception;

public class BusinessExcepion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessExcepion() {
		super();
	}

	public BusinessExcepion(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessExcepion(String message) {
		super(message);
	}

	public BusinessExcepion(Throwable cause) {
		super(cause);
	}
	
}
