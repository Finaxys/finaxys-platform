package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;


public interface RefDataGateway<T extends MarketData> {

	 List<T> getRefData() throws GatewayException;	
}
