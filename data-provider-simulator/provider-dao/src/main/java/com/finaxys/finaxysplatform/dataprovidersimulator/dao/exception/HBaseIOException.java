package com.finaxys.finaxysplatform.dataprovidersimulator.dao.exception;

public class HBaseIOException extends DataAccessException {


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
