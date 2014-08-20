/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.service.impl;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.EODDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.service.EODDataService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class EODMarketDataServiceImpl.
 */
public class EODDataServiceImpl<T extends MarketData, K extends MarketData> implements EODDataService<T, K> {

	/** The gateway. */
	private EODDataGateway<T, K> gateway;

	public EODDataServiceImpl(EODDataGateway<T, K> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getEODData(List<K> products) throws ServiceException {
		try {
			return this.gateway.getEODData(products);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}
}
