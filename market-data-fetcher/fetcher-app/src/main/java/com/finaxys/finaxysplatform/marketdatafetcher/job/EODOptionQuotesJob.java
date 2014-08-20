/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.finaxysplatform.core.domain.MarketDataWrapper;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.OptionChainDao;
import com.finaxys.finaxysplatform.marketdatafetcher.service.EODOptionQuoteService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.integration.gateway.MarketDataPublishingGateway;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionQuotesJob.
 */
public class EODOptionQuotesJob extends QuartzJobBean {

	private EODOptionQuoteService service;

	private MarketDataPublishingGateway<MarketDataWrapper<OptionQuote>> publishingGateway;

	private OptionChainDao optionChainDao;

	private char provider;

	private String exchSymb;

	private int bucketSize;

	private ExecutorService executorService;

	private DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM");

	public EODOptionQuoteService getService() {
		return service;
	}

	public void setService(EODOptionQuoteService service) {
		this.service = service;
	}

	public MarketDataPublishingGateway<MarketDataWrapper<OptionQuote>> getPublishingGateway() {
		return publishingGateway;
	}

	public void setPublishingGateway(MarketDataPublishingGateway<MarketDataWrapper<OptionQuote>> publishingGateway) {
		this.publishingGateway = publishingGateway;
	}

	public OptionChainDao getOptionChainDao() {
		return optionChainDao;
	}

	public void setOptionChainDao(OptionChainDao optionChainDao) {
		this.optionChainDao = optionChainDao;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

	public String getExchSymb() {
		return exchSymb;
	}

	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}

	public int getBucketSize() {
		return bucketSize;
	}

	public void setBucketSize(int bucketSize) {
		this.bucketSize = bucketSize;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Map<LocalDate, List<OptionChain>> map = new HashMap<LocalDate, List<OptionChain>>();

		// Get optionChains that input date is this month => we should
		// refresh the list every month

		List<OptionChain> optionChains = optionChainDao.list(this.provider);
		for (OptionChain optionChain : optionChains) {
			List<OptionChain> list = map.get(optionChain.getExpiration());
			if (list != null)
				list.add(optionChain);
			else {
				list = new ArrayList<OptionChain>();
				list.add(optionChain);
			}
			map.put(optionChain.getExpiration(), list);
		}
		for (LocalDate expiration : map.keySet()) {
			Task task = new Task(map, expiration, service, publishingGateway);
			executorService.execute(task);
		}

	}

	private static class Task implements Runnable {
		Map<LocalDate, List<OptionChain>> map;
		LocalDate expiration;
		private EODOptionQuoteService service;

		private MarketDataPublishingGateway<MarketDataWrapper<OptionQuote>> publishingGateway;

		public Task(Map<LocalDate, List<OptionChain>> map, LocalDate expiration, EODOptionQuoteService service,
				MarketDataPublishingGateway<MarketDataWrapper<OptionQuote>> publishingGateway) {
			super();
			this.map = map;
			this.expiration = expiration;
			this.service = service;
			this.publishingGateway = publishingGateway;
		}

		public void run() {
			List<OptionQuote> data = service.getEODData(this.map.get(this.expiration), this.expiration);
			if (data != null && data.size() > 0)
				publishingGateway.publishMarketData(new MarketDataWrapper<OptionQuote>(data));

		}
	}
}
