package com.finaxys.finaxysplatform.marketdatafetcher.dao.exception;

public class DataReadingParserException extends ParserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3920366942392286748L;

	public DataReadingParserException() {
		super("Unable to read data Exception");
	}

	public DataReadingParserException( Throwable throwable) {
		super("Unable to read data Exception", throwable);
	}
}
