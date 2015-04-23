package com.dad.device.server.util;

public class ParseException extends Exception {

	private static final long serialVersionUID = -7201044162713806019L;

	public ParseException() {

	}

	public ParseException(String msg) {
		super(msg);
	}

	   public ParseException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
