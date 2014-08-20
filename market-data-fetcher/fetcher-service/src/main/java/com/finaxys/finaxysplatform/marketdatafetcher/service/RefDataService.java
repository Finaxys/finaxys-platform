package com.finaxys.finaxysplatform.marketdatafetcher.service;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;


public interface RefDataService<T extends MarketData> {

	 List<T> getRefData() throws ServiceException;	
}
