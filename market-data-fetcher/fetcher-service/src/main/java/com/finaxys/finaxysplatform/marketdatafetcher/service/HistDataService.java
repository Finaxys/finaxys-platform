package com.finaxys.finaxysplatform.marketdatafetcher.service;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;



public interface HistDataService<T extends MarketData, K extends MarketData> {

	 List<T> getHistData(List<K> products, LocalDate startDate, LocalDate endDate) throws ServiceException;	
}
