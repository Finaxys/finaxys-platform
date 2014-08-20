package com.finaxys.finaxysplatform.persistence.hbase.exception;

public class HBaseRowKeyBadTypeException extends HBaseException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5402600870659562507L;

	public HBaseRowKeyBadTypeException() {
		super("Hbase rowKey bad type exception");
	}

	public HBaseRowKeyBadTypeException( Throwable throwable) {
		super("Hbase rowKey bad type exception", throwable);
	}
}
