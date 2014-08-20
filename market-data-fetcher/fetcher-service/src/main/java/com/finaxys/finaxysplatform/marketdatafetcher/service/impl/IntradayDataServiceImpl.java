/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.service.impl;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.IntradayDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.service.IntradayDataService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketDataServiceImpl.
 */
public class IntradayDataServiceImpl<T extends MarketData, K extends MarketData> implements IntradayDataService<T, K> {

	/** The gateway. */
	private IntradayDataGateway<T, K> gateway;

	public IntradayDataServiceImpl(IntradayDataGateway<T, K> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getCurrentData(List<K> products) throws ServiceException {
		try {
			return this.gateway.getCurrentData(products);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}
}