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
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.EODOptionQuoteGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.service.EODOptionQuoteService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class EODOptionQuoteServiceImpl.
 */
public class EODOptionQuoteServiceImpl implements EODOptionQuoteService {

	/** The gateway. */
	private EODOptionQuoteGateway gateway;

	public EODOptionQuoteServiceImpl(EODOptionQuoteGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionQuote> getEODData(List<Option> products) throws ServiceException {
		try {
			return this.gateway.getEODData(products);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<OptionQuote> getEODData(List<OptionChain> optionChains, LocalDate expiration) throws ServiceException {
		try {
			return this.gateway.getEODData(optionChains, expiration);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}
}