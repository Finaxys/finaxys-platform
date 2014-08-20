package com.finaxys.finaxysplatform.persistence.hbase.exception;

public class HBaseRowKeyLookupException extends HBaseException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5402600870659562507L;

	public HBaseRowKeyLookupException() {
		super("Hbase rowKey lookup exception");
	}

	public HBaseRowKeyLookupException( Throwable throwable) {
		super("Hbase rowKey lookup exception", throwable);
	}
}
