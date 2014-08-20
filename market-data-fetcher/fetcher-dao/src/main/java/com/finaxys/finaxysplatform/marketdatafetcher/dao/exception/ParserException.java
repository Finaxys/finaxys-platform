package com.finaxys.finaxysplatform.marketdatafetcher.dao.exception;

public class ParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3920366942392286748L;

	public ParserException() {
		super();
	}

	public ParserException(String m) {
		super(m);
	}

	public ParserException(String m, Throwable throwable) {
		super(m, throwable);
	}

	public ParserException(Throwable throwable) {
		super(throwable);
	}
}
