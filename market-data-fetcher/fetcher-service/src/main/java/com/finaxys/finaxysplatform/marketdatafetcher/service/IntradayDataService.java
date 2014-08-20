package com.finaxys.finaxysplatform.marketdatafetcher.service;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;


public interface IntradayDataService<T extends MarketData, K extends MarketData> {

	 List<T> getCurrentData(List<K> products) throws ServiceException;	
}
