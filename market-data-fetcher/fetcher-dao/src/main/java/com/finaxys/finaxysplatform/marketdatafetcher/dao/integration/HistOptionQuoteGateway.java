package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration;

import java.util.List;

import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;

public interface HistOptionQuoteGateway extends HistDataGateway<OptionQuote, Option>{
	 List<OptionQuote> getHistData(List<OptionChain> optionChains, LocalDate expiration, LocalDate startDate, LocalDate endDate) throws GatewayException;

}
