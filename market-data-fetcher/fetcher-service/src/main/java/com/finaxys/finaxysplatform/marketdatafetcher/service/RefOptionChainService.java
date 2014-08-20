package com.finaxys.finaxysplatform.marketdatafetcher.service;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;


public interface RefOptionChainService{

	 List<OptionChain> getRefData(List<Stock> stocks) throws ServiceException;	
}
