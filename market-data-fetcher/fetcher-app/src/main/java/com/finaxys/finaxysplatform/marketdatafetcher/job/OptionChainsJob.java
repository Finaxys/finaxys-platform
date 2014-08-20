package com.finaxys.finaxysplatform.marketdatafetcher.job;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.finaxysplatform.core.domain.MarketDataWrapper;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.StockDao;
import com.finaxys.finaxysplatform.marketdatafetcher.service.RefOptionChainService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.integration.gateway.MarketDataPublishingGateway;

public class OptionChainsJob extends QuartzJobBean {

	private RefOptionChainService service;

	private MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway;

	private StockDao stockDao;

	private ExecutorService executorService;

	private char provider;

	private int bucketSize;

	public RefOptionChainService getService() {
		return service;
	}

	public void setService(RefOptionChainService service) {
		this.service = service;
	}

	public MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> getPublishingGateway() {
		return publishingGateway;
	}

	public void setPublishingGateway(MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway) {
		this.publishingGateway = publishingGateway;
	}

	public StockDao getStockDao() {
		return stockDao;
	}

	public void setStockDao(StockDao stockDao) {
		this.stockDao = stockDao;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

	public int getBucketSize() {
		return bucketSize;
	}

	public void setBucketSize(int bucketSize) {
		this.bucketSize = bucketSize;
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

		List<Stock> stocks = stockDao.list(provider);

		int x = stocks.size() / bucketSize;
		int y = stocks.size() % bucketSize;

		for (int i = 0; i < x; i++) {

			Task task = new Task(stocks.subList((i * bucketSize), ((i + 1) * bucketSize)), service, publishingGateway);
			executorService.execute(task);
		}

		Task task = new Task(stocks.subList((x * bucketSize), (x * bucketSize) + y), service, publishingGateway);
		executorService.execute(task);

	}

	private static class Task implements Runnable {
		List<Stock> stocks;
		private RefOptionChainService service;

		private MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway;

		public Task(List<Stock> stocks, RefOptionChainService service, MarketDataPublishingGateway<MarketDataWrapper<OptionChain>> publishingGateway) {
			super();
			this.stocks = stocks;
			this.service = service;
			this.publishingGateway = publishingGateway;
		}

		public void run() {
			List<OptionChain> data = service.getRefData(stocks);
			if (data != null && data.size() > 0)
				publishingGateway.publishMarketData(new MarketDataWrapper<OptionChain>(data));

		}
	}
}
