package com.finaxys.finaxysplatform.marketdatafetcher.dao.exception;

public class BadDataParserException extends ParserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3920366942392286748L;


	public BadDataParserException() {
		super("Bad data found when parsing the file");
	}

	public BadDataParserException( Throwable throwable) {
		super("Bad data found when parsing the file", throwable);
	}


}
