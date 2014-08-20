package com.finaxys.finaxysplatform.marketdatafetcher.service;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.marketdatafetcher.service.exception.ServiceException;

public interface EODOptionQuoteService extends EODDataService<OptionQuote, Option>{
	
	 List<OptionQuote> getEODData(List<OptionChain> optionChains, LocalDate expiration) throws ServiceException;
	

}
