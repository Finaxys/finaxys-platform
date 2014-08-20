/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.core.domain.MarketDataWrapper;
import com.finaxys.finaxysplatform.marketdatafetcher.service.RefDataService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.integration.gateway.MarketDataPublishingGateway;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairsJob.
 */
public class RefDataJob<T extends MarketData> extends QuartzJobBean {

	private RefDataService<T> service;

	private MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway;

	public RefDataService<T> getService() {
		return service;
	}

	public void setService(RefDataService<T> service) {
		this.service = service;
	}

	public MarketDataPublishingGateway<MarketDataWrapper<T>> getPublishingGateway() {
		return publishingGateway;
	}

	public void setPublishingGateway(MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway) {
		this.publishingGateway = publishingGateway;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org
	 * .quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		List<T> data = service.getRefData();
		if (data != null && data.size() > 0)
			publishingGateway.publishMarketData(new MarketDataWrapper<T>(data));

	
	}
}