/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.finaxys.finaxysplatform.marketdatafetcher.jobmanager.EODJobManager;
import com.finaxys.finaxysplatform.marketdatafetcher.jobmanager.IntradayJobManager;
import com.finaxys.finaxysplatform.marketdatafetcher.jobmanager.OneTimeJobManager;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class Application.
 */
@Configuration
@Import(ServicesConfig.class)
public class Application {

	private static Logger logger = Logger.getLogger(Application.class);

	public static final String usage = "Choose an action (set the number) \n"
			+ "1: initAllJobs \n"
			+ "2: getFileXlsCurrencyPair \n"
			+ "3: getFileXlsExchange \n"
			+ "4: getFileXlsInterbankRate \n"
			+ "5: getFileXlslIndex \n"
			+ "6: getFileXlsOption \n"
			+ "7: getFileXlsStock \n"
			+ "8: scheduleFXRatesJobs \n"
			+ "9: scheduleIndexQuotesJobs \n"
			+ "10: scheduleOptionQuotesJobs \n"
			+ "11: scheduleOQuotesJobs \n"
			+ "12: scheduleStockQuotesJobs \n"
			+ "13: startOptionChainsJob \n"
			+ "14: scheduleEODFXRatesJobs \n"
			+ "15: scheduleEODIndexQuotesJobs \n"
			+ "16: scheduleEODOptionQuotesJobs \n"
			+ "17: scheduleEODOQuotesJobs \n"
			+ "18: scheduleEODStockQuotesJobs \n"
	        + "19: exit \n";
	

	/** The rabbit host. */
	@Value("${rabbitmq.host}")
	private String rabbitHost;

	/** The rabbit host. */
	@Value("${app.threadpool.size}")
	private int threadPoolSize;

	@Bean
	ExecutorService executorService() {
		return Executors.newFixedThreadPool(threadPoolSize);
	}

	/**
	 * Connection.
	 * 
	 * @return the connection
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Bean
	Connection connection() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitHost);
		return factory.newConnection();
	}

	/**
	 * Channel.
	 * 
	 * @return the channel
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Bean
	@Scope("prototype")
	Channel channel() throws IOException {
		return connection().createChannel();
	}

	/**
	 * H connection.
	 * 
	 * @return the h connection
	 * @throws ZooKeeperConnectionException
	 *             the zoo keeper connection exception
	 */
	@Bean
	HConnection hConnection() throws ZooKeeperConnectionException {
		return HConnectionManager.createConnection(HBaseConfiguration.create());
	}

	/**
	 * Client.
	 * 
	 * @return the closeable http async client
	 * @throws IOReactorException
	 *             the IO reactor exception
	 */
	@Bean
	CloseableHttpAsyncClient client() throws IOReactorException {
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).setConnectionRequestTimeout(50000)
				.setStaleConnectionCheckEnabled(true).build();
		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		cm.setDefaultMaxPerRoute(100);
		cm.setMaxTotal(200);
		executorService().execute(new IdleNConnectionMonitorThread(cm));
		return HttpAsyncClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(cm).build();

	}

	@Bean
	CloseableHttpClient httpClient() {

		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).setConnectionRequestTimeout(50000)
				.setStaleConnectionCheckEnabled(true).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setDefaultMaxPerRoute(100);
		cm.setMaxTotal(200);
		executorService().execute(new IdleConnectionMonitorThread(cm));
		return HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(cm).build();

	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SchedulerException
	 */
	public static void main(String[] args) throws IOException, SchedulerException {

		final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/spring-config.xml");
		context.registerShutdownHook();

//		JobsInitialiser jobsInitialiser = (JobsInitialiser) context.getBean("jobsInitialiser", new OneTimeJobManager(context), new IntradayJobManager(context), new EODJobManager(
//				context));
		JobsInitialiser jobsInitialiser = (JobsInitialiser) context.getBean("jobsInitialiser");
		logger.info(usage);
		jobsInitialiser.initJobsListener();
		while(true){
			Scanner sc = new Scanner(System.in);
			int str = sc.nextInt();

			switch(str){
			case 1:	jobsInitialiser.initAll();	break;
			case 2:	jobsInitialiser.getFileXlsCurrencyPair();	break;
			case 3:	jobsInitialiser.getFileXlsExchange();	break;
			case 4:	jobsInitialiser.getFileXlsInterbankRate();	break;
			case 5:	jobsInitialiser.getFileXlslIndex();	break;
			case 6:	jobsInitialiser.getFileXlsOption();	break;
			case 7:	jobsInitialiser.getFileXlsStock();	break;
			case 8:	jobsInitialiser.scheduleFXRatesJobs();	break;
			case 9:	jobsInitialiser.scheduleIndexQuotesJobs();	break;
			case 10:	jobsInitialiser.scheduleOptionQuotesJobs();	break;
			case 11:	jobsInitialiser.scheduleOQuotesJobs();;	break;
			case 12:	jobsInitialiser.scheduleStockQuotesJobs();;	break;
			case 13:	jobsInitialiser.startOptionChainsJob();	break;
			case 14:	jobsInitialiser.scheduleEODFXRatesJobs();	break;
			case 15:	jobsInitialiser.scheduleEODIndexQuotesJobs();	break;
			case 16:	jobsInitialiser.scheduleEODOptionQuotesJobs();	break;
			case 17:	jobsInitialiser.scheduleEODOQuotesJobs();	break;
			case 18:	jobsInitialiser.scheduleEODStockQuotesJobs();	break;
			case 19:	System.exit(0);
			}	
		}
		
		

	}

	public static class IdleConnectionMonitorThread implements Runnable {

		private final HttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(5000);
						// Close expired connections
						connMgr.closeExpiredConnections();
						// Optionally, close connections
						// that have been idle longer than 30 sec
						connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				// terminate
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}

	}

	public static class IdleNConnectionMonitorThread implements Runnable {

		private final PoolingNHttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleNConnectionMonitorThread(PoolingNHttpClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(5000);
						// Close expired connections
						connMgr.closeExpiredConnections();
						// Optionally, close connections
						// that have been idle longer than 30 sec
						connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				// terminate
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}

	}
}
