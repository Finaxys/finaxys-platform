/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.service.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.HistDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.service.HistDataService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketDataServiceImpl.
 */
public class HistDataServiceImpl<T extends MarketData, K extends MarketData> implements
		HistDataService<T, K> {

	/** The gateway. */
	private HistDataGateway<T, K> gateway;

	public HistDataServiceImpl(HistDataGateway<T, K> gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<T> getHistData(List<K> products,
			LocalDate startDate, LocalDate endDate) throws ServiceException {
		try {
			return this.gateway.getHistData(products, startDate, endDate);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}

}
