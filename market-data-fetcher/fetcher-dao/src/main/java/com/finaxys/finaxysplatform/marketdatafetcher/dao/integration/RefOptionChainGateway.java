package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;


public interface RefOptionChainGateway {

	 List<OptionChain> getRefData(List<Stock> stocks) throws GatewayException ;
		
}
