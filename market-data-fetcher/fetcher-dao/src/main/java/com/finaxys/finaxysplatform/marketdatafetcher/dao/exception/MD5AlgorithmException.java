package com.finaxys.finaxysplatform.marketdatafetcher.dao.exception;

public class MD5AlgorithmException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7562082707029723095L;

	public MD5AlgorithmException() {
		super();
	}

	public MD5AlgorithmException(String m) {
		super(m);
	}

	public MD5AlgorithmException(String m, Throwable throwable) {
		super(m, throwable);
	}

	public MD5AlgorithmException(Throwable throwable) {
		super(throwable);
	}
}
