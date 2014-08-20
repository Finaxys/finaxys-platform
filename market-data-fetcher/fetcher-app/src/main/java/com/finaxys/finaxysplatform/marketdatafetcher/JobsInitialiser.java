package com.finaxys.finaxysplatform.marketdatafetcher;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.finaxys.finaxysplatform.marketdatafetcher.job.listener.FetchDataJobListener;
import com.finaxys.finaxysplatform.marketdatafetcher.jobmanager.EODJobManager;
import com.finaxys.finaxysplatform.marketdatafetcher.jobmanager.IntradayJobManager;
import com.finaxys.finaxysplatform.marketdatafetcher.jobmanager.OneTimeJobManager;

@Component("jobsInitialiser")
// @Scope("prototype")
public class JobsInitialiser {
	
	private static Logger logger = Logger.getLogger(JobsInitialiser.class);
	
	@Value("${gateway.yahoo.symbol}")
	private char YAHOO_PROVIDER_SYMBOL;

	@Value("${gateway.file.symbol}")
	private char FILE_PROVIDER_SYMBOL;

	@Value("${gateway.ebf.symbol}")
	private char EBF_PROVIDER_SYMBOL;

	@Autowired
	ApplicationContext context;

	@Autowired
	OneTimeJobManager oneTimeJobManager;
	@Autowired
	IntradayJobManager intradayJobManager;
	@Autowired
	EODJobManager eodJobManager;

	public JobsInitialiser() {
		super();
	}

	public JobsInitialiser(OneTimeJobManager oneTimeJobManager, IntradayJobManager intradayJobManager, EODJobManager eodJobManager) {
		super();
		this.oneTimeJobManager = oneTimeJobManager;
		this.intradayJobManager = intradayJobManager;
		this.eodJobManager = eodJobManager;

	}

	public void initJobsListener() throws SchedulerException {
		Scheduler scheduler = (Scheduler) context.getBean("marketDataJobsScheduler");
		scheduler.getListenerManager().addJobListener(new FetchDataJobListener());
	}

	public void startHistStockQuotesJob(String start, String end) throws IOException, SchedulerException {

		oneTimeJobManager.startHistStockQuotesJob(YAHOO_PROVIDER_SYMBOL, "exchangeDao", start, end, "stockDao", "yahooXmlHistStockQuoteService", "marketDataPublishingGateway", 10);//
	}

	public void getFileXlsStock() throws IOException, SchedulerException {
		oneTimeJobManager.startRefDataJob("fileXlsStockService", "marketDataPublishingGateway");
	}

	public void getFileXlsOption() throws IOException, SchedulerException {
		oneTimeJobManager.startRefDataJob("fileXlsOptionService", "marketDataPublishingGateway");
	}

	public void getFileXlsInterbankRate() throws IOException, SchedulerException {
		oneTimeJobManager.startRefDataJob("fileXlsInterbankRateService", "marketDataPublishingGateway");
	}

	public void getFileXlslIndex() throws IOException, SchedulerException {
		oneTimeJobManager.startRefDataJob("fileXlslIndexService", "marketDataPublishingGateway");
	}

	public void getFileXlsCurrencyPair() throws IOException, SchedulerException {
		oneTimeJobManager.startRefDataJob("fileXlsCurrencyPairService", "marketDataPublishingGateway");
	}

	public void getFileXlsExchange() throws IOException, SchedulerException {
		oneTimeJobManager.startRefDataJob("fileXlsExchangeService", "marketDataPublishingGateway");
	}

	public void startHistIndexQuotesJob(String start, String end) throws IOException, SchedulerException {

		oneTimeJobManager.startHistIndexQuotesJob(YAHOO_PROVIDER_SYMBOL, "exchangeDao", start, end, "indexDao", "yahooXmlHistIndexQuoteService", "marketDataPublishingGateway", 2);//
	}

	public void scheduleStockQuotesJobs() throws IOException, SchedulerException {
		intradayJobManager.scheduleStockQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "stockDao", "yahooXmlStockQuoteService", "marketDataPublishingGateway", 100);
	}

	public void startOptionChainsJob() throws IOException, SchedulerException {
		oneTimeJobManager.startOptionChainsJob(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "stockDao", "yahooXmlOptionChainService", "marketDataPublishingGateway", 100);//
	}

	public void scheduleEODOptionQuotesJobs() throws IOException, SchedulerException {
		eodJobManager.scheduleEODOptionQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "optionChainDao", "yahooXmlEODOptionQuoteService", "marketDataPublishingGateway", 100);// /
	}

	public void scheduleOptionQuotesJobs() throws IOException, SchedulerException {
		intradayJobManager.scheduleOptionQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "optionChainDao", "yahooXmlOptionQuoteService", "marketDataPublishingGateway", 100);// /
	}

	public void scheduleEODOQuotesJobs() throws IOException, SchedulerException {
		eodJobManager.scheduleEODOQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "optionDao", "yahooXmlEODOptionQuoteService", "marketDataPublishingGateway", 200);// /
	}

	public void scheduleOQuotesJobs() throws IOException, SchedulerException {
		intradayJobManager.scheduleOQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "optionDao", "yahooXmlOptionQuoteService", "marketDataPublishingGateway", 100);
	}

	public void scheduleIndexQuotesJobs() throws IOException, SchedulerException {
		intradayJobManager.scheduleIndexQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "indexDao", "yahooXmlIndexQuoteService", "marketDataPublishingGateway", 100);
	}

	public void scheduleEODIndexQuotesJobs() throws IOException, SchedulerException {
		eodJobManager.scheduleEODIndexQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "indexDao", "yahooXmlEODIndexQuoteService", "marketDataPublishingGateway", 100);
	}

	public void scheduleEODFXRatesJobs() throws IOException, SchedulerException {
		eodJobManager.scheduleEODFXRatesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "currencyPairDao", "yahooXmlEODFXRateService", "marketDataPublishingGateway", 100);
	}

	public void scheduleFXRatesJobs() throws IOException, SchedulerException {
		intradayJobManager.scheduleFXRatesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "currencyPairDao", "yahooXmlFXRateService", "marketDataPublishingGateway", 100);
	}

	public void scheduleEODStockQuotesJobs() throws IOException, SchedulerException {
		eodJobManager.scheduleEODStockQuotesJobs(YAHOO_PROVIDER_SYMBOL, "exchangeDao", "stockDao", "yahooXmlEODStockQuoteService", "marketDataPublishingGateway", 100);
	}

	public void initAll() throws IOException, SchedulerException {
		scheduleStockQuotesJobs();
		scheduleEODOptionQuotesJobs();
		scheduleOptionQuotesJobs();
		scheduleEODOQuotesJobs();
		scheduleOQuotesJobs();
		scheduleIndexQuotesJobs();
		scheduleEODIndexQuotesJobs();
		scheduleEODFXRatesJobs();
		scheduleFXRatesJobs();
		scheduleEODStockQuotesJobs();
	}
}
