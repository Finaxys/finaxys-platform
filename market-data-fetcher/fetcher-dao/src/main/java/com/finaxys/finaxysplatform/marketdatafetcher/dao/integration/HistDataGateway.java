package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;



public interface HistDataGateway<T extends MarketData, K extends MarketData> {

	 List<T> getHistData(List<K> products, LocalDate startDate, LocalDate endDate) throws GatewayException;	
}
