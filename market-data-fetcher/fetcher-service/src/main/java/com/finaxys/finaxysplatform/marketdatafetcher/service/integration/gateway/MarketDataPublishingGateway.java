package com.finaxys.finaxysplatform.marketdatafetcher.service.integration.gateway;

import com.finaxys.finaxysplatform.core.domain.MarketDataWrapper;


public interface MarketDataPublishingGateway<T extends MarketDataWrapper<?>> {

	 void publishMarketData(T data);
}
