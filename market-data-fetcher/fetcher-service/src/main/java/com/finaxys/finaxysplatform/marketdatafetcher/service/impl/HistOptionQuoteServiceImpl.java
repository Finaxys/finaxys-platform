/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.service.impl;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.HistOptionQuoteGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.service.HistOptionQuoteService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class HistOptionQuoteServiceImpl.
 */
public class HistOptionQuoteServiceImpl implements HistOptionQuoteService {

	/** The gateway. */
	private HistOptionQuoteGateway gateway;

	public HistOptionQuoteServiceImpl(HistOptionQuoteGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionQuote> getHistData(List<Option> products, LocalDate startDate, LocalDate endDate) throws ServiceException {
		try {
			return this.gateway.getHistData(products, startDate, endDate);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<OptionQuote> getHistData(List<OptionChain> optionChains, LocalDate expiration, LocalDate startDate, LocalDate endDate) throws ServiceException {

		try {
			return this.gateway.getHistData(optionChains, expiration, startDate, endDate);

		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}

}