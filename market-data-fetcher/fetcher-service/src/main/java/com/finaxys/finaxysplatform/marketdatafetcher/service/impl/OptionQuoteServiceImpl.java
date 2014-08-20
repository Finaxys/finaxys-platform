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
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.IntradayOptionQuoteGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.service.IntradayOptionQuoteService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionQuoteServiceImpl.
 */
public class OptionQuoteServiceImpl implements IntradayOptionQuoteService {

	/** The gateway. */
	private IntradayOptionQuoteGateway gateway;

	public OptionQuoteServiceImpl(IntradayOptionQuoteGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionQuote> getCurrentData(List<Option> products) throws ServiceException {
		try {
			return this.gateway.getCurrentData(products);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<OptionQuote> getCurrentData(List<OptionChain> optionChains, LocalDate expiration) throws ServiceException {
		try {
			return this.gateway.getCurrentData(optionChains, expiration);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}
}