package com.finaxys.finaxysplatform.persistence.hbase.exception;

public class HBaseException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7562082707029723095L;

	public HBaseException() {
		super();
	}

	public HBaseException(String m) {
		super(m);
	}

	public HBaseException(String m, Throwable throwable) {
		super(m, throwable);
	}

	public HBaseException(Throwable throwable) {
		super(throwable);
	}
}
