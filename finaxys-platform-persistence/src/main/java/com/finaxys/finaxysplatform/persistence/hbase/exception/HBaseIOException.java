package com.finaxys.finaxysplatform.persistence.hbase.exception;

public class HBaseIOException extends HBaseException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7562082707029723095L;


	public HBaseIOException() {
		super("Hbase exception");
	}

	public HBaseIOException( Throwable throwable) {
		super("Hbase Exception", throwable);
	}

}
