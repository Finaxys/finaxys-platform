package com.finaxys.finaxysplatform.marketdatafetcher.dao.exception;

public class GatewayCommunicationException extends GatewayException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3166657414077516082L;

	public GatewayCommunicationException() {
		super("Communication exception. Unable to access resource.");
	}

	public GatewayCommunicationException( Throwable throwable) {
		super("Communication exception. Unable to access resource.", throwable);
	}
}
