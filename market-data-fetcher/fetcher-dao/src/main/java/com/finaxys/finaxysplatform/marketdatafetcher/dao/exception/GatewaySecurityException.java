package com.finaxys.finaxysplatform.marketdatafetcher.dao.exception;

public class GatewaySecurityException extends GatewayException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3166657414077516082L;

	public GatewaySecurityException() {
		super("Authentification exception. Unable to private access resource.");
	}

	public GatewaySecurityException( Throwable throwable) {
		super("Authentification exception. Unable to private access resource.", throwable);
	}
}
