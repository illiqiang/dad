package com.dad.device.server.util;

public class IllegalRequestException extends RuntimeException {

	private static final long serialVersionUID = 352412387596614522L;

	public static byte unknown = 0;
	public static byte notRegister = 1;
	public static byte deviceChange = 2;
	public static byte crcError = 3;
	public static byte parseError = 4;

	public IllegalRequestException(String msg) {
		super(unknown + ":" + msg);
	}

	public IllegalRequestException(byte type, String msg) {
		super(type + ":" + msg);
	}

	public IllegalRequestException(String msg, Throwable cause) {
		super(unknown + ":" + msg, cause);
	}

	public IllegalRequestException(byte type, String msg, Throwable cause) {
		super(type + ":" + msg, cause);
	}
}
