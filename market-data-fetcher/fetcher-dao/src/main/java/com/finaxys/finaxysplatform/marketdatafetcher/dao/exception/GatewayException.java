package com.finaxys.finaxysplatform.marketdatafetcher.dao.exception;

public class GatewayException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3920366942392286748L;

	public GatewayException() {
		super();
	}

	public GatewayException(String m) {
		super(m);
	}

	public GatewayException(String m, Throwable throwable) {
		super(m, throwable);
	}

	public GatewayException(Throwable throwable) {
		super(throwable);
	}
}
