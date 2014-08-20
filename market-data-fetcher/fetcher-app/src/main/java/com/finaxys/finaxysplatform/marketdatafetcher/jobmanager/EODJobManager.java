package com.finaxys.finaxysplatform.marketdatafetcher.jobmanager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.joda.time.LocalTime;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.core.domain.Exchange;
import com.finaxys.finaxysplatform.core.domain.Index;
import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.CurrencyPairDao;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.ExchangeDao;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.IndexDao;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.OptionChainDao;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.OptionDao;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.StockDao;
import com.finaxys.finaxysplatform.marketdatafetcher.service.EODDataService;
import com.finaxys.finaxysplatform.marketdatafetcher.service.integration.gateway.MarketDataPublishingGateway;

@Component
public class EODJobManager {
	
	private static Logger logger = Logger.getLogger(EODJobManager.class);
	@Autowired
	ApplicationContext context;

	public EODJobManager( ) {
		super();
	}
	
	public EODJobManager(ApplicationContext context) {
		super();
		this.context = context;
	}

	public void scheduleEODOptionQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				List<OptionChain> products = ((OptionChainDao) context.getBean(productDaoBeanName)).list(provider);

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);

				scheduleJob(com.finaxys.finaxysplatform.marketdatafetcher.job.OptionQuotesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODOQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Option> products = ((OptionDao) context.getBean(productDaoBeanName)).list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.finaxysplatform.marketdatafetcher.job.EODDataJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODIndexQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Index> products = ((IndexDao) context.getBean(productDaoBeanName)).list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);

				scheduleJob(com.finaxys.finaxysplatform.marketdatafetcher.job.IndexQuotesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODStockQuotesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {
				List<Stock> products = ((StockDao) context.getBean(productDaoBeanName)).list(provider, exchange.getSourceSymbol());

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.finaxysplatform.marketdatafetcher.job.StockQuotesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}
	}

	public void scheduleEODFXRatesJobs(char provider, String exchangeDaoBeanName, String productDaoBeanName, String serviceBeanName, String publishingGatewayBeanName,
			Integer bucket) throws IOException, SchedulerException {

		ExchangeDao exchangeDao = (ExchangeDao) context.getBean(exchangeDaoBeanName);

		byte[] prefix = new byte[1];
		Bytes.putByte(prefix, 0, (byte) provider);
		List<Exchange> exchanges = exchangeDao.list(prefix);
		List<CurrencyPair> products = ((CurrencyPairDao) context.getBean(productDaoBeanName)).list(provider);

		for (Exchange exchange : exchanges) {
			if (exchange.getProvider() == provider) {

				Map<String, Object> jobData = getJobData(products, serviceBeanName, publishingGatewayBeanName, bucket);
				scheduleJob(com.finaxys.finaxysplatform.marketdatafetcher.job.FXRatesJob.class, jobData, exchange.getCloseTime(), exchange.getDelay());
			}
		}

	}

	private Map<String, Object> getJobData(List<? extends MarketData> products, String serviceBeanName, String publishingGatewayBeanName, Integer bucket) {
		Map<String, Object> jobData = new HashMap<String, Object>();
		jobData.put("products", products);
		jobData.put("service", (EODDataService<?, ?>) context.getBean(serviceBeanName));
		jobData.put("publishingGateway", (MarketDataPublishingGateway<?>) context.getBean(publishingGatewayBeanName));
		jobData.put("executorService", (ExecutorService) context.getBean("executorService"));
		jobData.put("bucketSize", bucket);

		return jobData;

	}

	private void scheduleJob(Class<?> jobClass, Map<String, Object> jobData, LocalTime closeTime, Integer delay) throws SchedulerException {

		Scheduler scheduler = (Scheduler) context.getBean("marketDataJobsScheduler");

		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setGroup("marketDataJobsGroup");
		jobDetailFactory.setBeanName("jobDetail-" + jobClass.getSimpleName() + "-" + jobData.hashCode());
		jobDetailFactory.setJobClass(jobClass);

		jobDetailFactory.setJobDataAsMap(jobData);

		jobDetailFactory.setDurability(true);
		jobDetailFactory.afterPropertiesSet();
		JobDetail jobDetail = (JobDetail) jobDetailFactory.getObject();

		CronTrigger trigger = createTrigger("trigger-" + jobClass.getSimpleName() + "-" + jobData.hashCode(), jobDetail, closeTime, delay);


		scheduler.addJob(jobDetail, true);
		scheduler.scheduleJob(trigger);
		
		if (!scheduler.isStarted())
			scheduler.start();

	}

	private CronTrigger createTrigger(String name, JobDetail jobDetail, LocalTime closeTime, Integer delay) {

		String cronExpression = createCronExpression(closeTime, delay);

		CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
		cronTrigger.setGroup("marketDataJobsGroup");
		cronTrigger.setJobDetail(jobDetail);
		cronTrigger.setBeanName(name);
		cronTrigger.setCronExpression(cronExpression);
		cronTrigger.afterPropertiesSet();

		return (CronTrigger) cronTrigger.getObject();

	}

	private String createCronExpression(LocalTime closeTime, Integer delay) {
		closeTime = closeTime.plusSeconds(delay);
		return closeTime.getSecondOfMinute() + " " + (closeTime.getMinuteOfHour() + 2) + " " + closeTime.getHourOfDay() + " ? * MON-FRI *";
	}

}
