package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;


public interface IntradayDataGateway<T extends MarketData, K extends MarketData> {

	 List<T> getCurrentData(List<K> products) throws GatewayException;	
}
