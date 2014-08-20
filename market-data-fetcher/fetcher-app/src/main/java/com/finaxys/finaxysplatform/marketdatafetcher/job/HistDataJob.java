package com.finaxys.finaxysplatform.marketdatafetcher.job;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.core.domain.MarketDataWrapper;
import com.finaxys.finaxysplatform.marketdatafetcher.service.HistDataService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.integration.gateway.MarketDataPublishingGateway;

public class HistDataJob<T extends MarketData, K extends MarketData> extends QuartzJobBean {

	private List<K> products;

	private HistDataService<T, K> service;

	private MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway;

	private ExecutorService executorService;

	private String startDate;

	private String endDate;

	private int bucketSize;

	 private DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	public List<K> getProducts() {
		return products;
	}

	public void setProducts(List<K> products) {
		this.products = products;
	}

	public HistDataService<T, K> getService() {
		return service;
	}

	public void setService(HistDataService<T, K> service) {
		this.service = service;
	}

	public MarketDataPublishingGateway<MarketDataWrapper<T>> getPublishingGateway() {
		return publishingGateway;
	}

	public void setPublishingGateway(MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway) {
		this.publishingGateway = publishingGateway;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getBucketSize() {
		return bucketSize;
	}

	public void setBucketSize(int bucketSize) {
		this.bucketSize = bucketSize;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		LocalDate start = dformatter.parseLocalDate(startDate);
		LocalDate end = dformatter.parseLocalDate(endDate);

		int x = products.size() / bucketSize;
		int y = products.size() % bucketSize;

		for (int i = 0; i < x; i++) {

			Task<T, K> task = new Task<T, K>(products.subList((i * bucketSize), ((i + 1) * bucketSize)), service, publishingGateway, start, end);
			executorService.execute(task);
		}

		Task<T, K> task = new Task<T, K>(products.subList((x * bucketSize), (x * bucketSize) + y), service, publishingGateway, start, end);
		executorService.execute(task);

	}

	private static class Task<T extends MarketData, K extends MarketData> implements Runnable {
		private List<K> products;

		private HistDataService<T, K> service;

		private MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway;

		private LocalDate start;
		private LocalDate end;

		public Task(List<K> products, HistDataService<T, K> service, MarketDataPublishingGateway<MarketDataWrapper<T>> publishingGateway, LocalDate start, LocalDate end) {
			super();
			this.products = products;
			this.service = service;
			this.publishingGateway = publishingGateway;
			this.start = start;
			this.end = end;
		}

		public void run() {
			List<T> data = service.getHistData(products, start, end);
			if (data != null && data.size() > 0)
				publishingGateway.publishMarketData(new MarketDataWrapper<T>(data));

		}
	}

}
