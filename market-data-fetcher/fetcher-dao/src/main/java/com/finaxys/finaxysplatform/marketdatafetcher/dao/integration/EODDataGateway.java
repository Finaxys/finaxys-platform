package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;


public interface EODDataGateway<T extends MarketData, K extends MarketData> {

	 List<T> getEODData(List<K> products) throws GatewayException;	
}
