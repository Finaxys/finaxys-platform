package com.finaxys.finaxysplatform.marketdatafetcher.service.impl;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.RefOptionChainGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.service.RefOptionChainService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

public class OptionChainServiceImpl implements RefOptionChainService {

	/** The gateway. */
	private RefOptionChainGateway gateway;

	public OptionChainServiceImpl(RefOptionChainGateway gateway) {
		super();
		this.gateway = gateway;
	}

	@Override
	public List<OptionChain> getRefData(List<Stock> stocks) throws ServiceException {
		try {
			return this.gateway.getRefData(stocks);
		} catch (GatewayException e) {
			throw new ServiceException(e);
		}
	}
}