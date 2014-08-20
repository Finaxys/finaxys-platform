package com.finaxys.finaxysplatform.marketdatafetcher.dao.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.MarketData;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;

public class YahooGatewayHelper {

	/** The Constant MD5_LENGTH. */
	public static final int MD5_LENGTH = Integer.valueOf(Configuration.MD5_LENGTH.get()); // bytes

	/** The Constant YQL_HOST. */
	private static final String YQL_HOST = Configuration.YQL_HOST.get();

	/** The Constant YQL_PATH. */
	private static final String YQL_PATH = Configuration.YQL_PATH.get();

	/** The Constant YQL_QUERY_PARAM. */
	private static final String YQL_QUERY_PARAM = Configuration.YQL_QUERY_PARAM.get();

	/** The Constant YQL_FORMAT_PARAM. */
	private static final String YQL_FORMAT_PARAM = Configuration.YQL_FORMAT_PARAM.get();

	/** The Constant YQL_ENV_PARAM. */
	private static final String YQL_ENV_PARAM = Configuration.YQL_ENV_PARAM.get();

	private static final String YQL_CONSUMERKEY = Configuration.YQL_CONSUMERKEY.get();

	private static final String YQL_CONSUMERSECRET = Configuration.YQL_CONSUMERSECRET.get();

	public static final char Y_PROVIDER_SYMB = Configuration.Y_PROVIDER_SYMB.get().charAt(0);

	private static final String YQL_DEFAULT_ENV = Configuration.YQL_DEFAULT_ENV.get();

	public static byte[] executeYQLQuery(String yqlQuery, List<String> params, ContentType contentType, CloseableHttpClient httpClient, HttpContext context)
			throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, ClientProtocolException, IOException, OAuthCommunicationException {
		String query = yqlQuery;
		if (params != null && params.size() > 0)
			for (String param : params)
				query = YahooGatewayHelper.constructQuery(query, param);
		URI uri = YahooGatewayHelper.contructYqlUri(query, contentType, YQL_DEFAULT_ENV);
		HttpGet request = new HttpGet(uri);
		YahooGatewayHelper.signOAuthYQLRequest(request);
		CloseableHttpResponse response = httpClient.execute(request, context);
		byte[] data = IOUtils.toByteArray(response.getEntity().getContent());
		if (response != null)
			response.close();
		return data;

	}

	/**
	 * Construct query.
	 * 
	 * @param query
	 *            the query
	 * @param symbs
	 *            the symbs
	 * @return the string
	 */
	public static String constructQuery(String query, String param) {
		return query.replaceFirst("\\?", param);
	}

	/**
	 * Contruct yql uri.
	 * 
	 * @param query
	 *            the query
	 * @param format
	 *            the format
	 * @param env
	 *            the env
	 * @return the uri
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 */
	public static URI contructYqlUri(String query, ContentType format, String env) throws URISyntaxException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(YQL_QUERY_PARAM, query));
		params.add(new BasicNameValuePair(YQL_FORMAT_PARAM, format.getName()));
		params.add(new BasicNameValuePair(YQL_ENV_PARAM, env));

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(YQL_HOST).setPath(YQL_PATH).setParameters(params);
		return builder.build();
	}

	public static void signOAuthRequest(Object request, String consumerKey, String consumerSecret) throws OAuthMessageSignerException, OAuthExpectationFailedException,
			OAuthCommunicationException {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		consumer.sign(request);
	}

	public static void signOAuthYQLRequest(Object request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
		signOAuthRequest(request, YQL_CONSUMERKEY, YQL_CONSUMERSECRET);
	}

	public static String getSymbols(List<? extends MarketData> products) {
		StringBuilder sb = new StringBuilder();
		if (products != null && products.size() > 0)
			for (MarketData product : products)
				sb.append("\"" + product.getSymbol() + "\",");

		return sb.toString().replaceAll(",$", "");
	}

	public static String getStocksSymbols(List<Stock> stocks) {
		StringBuilder sb = new StringBuilder();
		for (Stock stock : stocks) {
			if (stock.getExchSymb().equals("US"))
				sb.append("\"" + stock.getSymbol() + "\",");
			else
				sb.append("\"" + stock.getSymbol() + "." + stock.getExchSymb() + "\",");
		}

		return sb.toString().replaceAll(",$", "");
	}

	/**
	 * The Enum Configuration.
	 */
	public enum Configuration {

		Y_PROVIDER_SYMB("gateway.yahoo.symbol"),

		YQL_DEFAULT_ENV("gateway.yahoo.yqlDefaultEnv"),

		/** The yql host. */
		YQL_HOST("gateway.yahoo.yql.host"),

		/** The yql path. */
		YQL_PATH("gateway.yahoo.yql.path"),

		/** The yql query param. */
		YQL_QUERY_PARAM("gateway.yahoo.yql.query_param"),

		/** The yql format param. */
		YQL_FORMAT_PARAM("gateway.yahoo.yql.format_param"),

		/** The yql env param. */
		YQL_ENV_PARAM("gateway.yahoo.yql.env_param"),

		/** The M d5_ length. */
		MD5_LENGTH("gateway.helper.md5Length"),

		YQL_CONSUMERKEY("gateway.yahoo.yql.consumerKey"),

		YQL_CONSUMERSECRET("gateway.yahoo.yql.consumerSecret");

		/** The key. */
		private final String key;

		/**
		 * Instantiates a new configuration.
		 * 
		 * @param key
		 *            the key
		 */
		Configuration(String key) {
			this.key = key;
		}

		/** The Constant logger. */
		private final static Logger logger = Logger.getLogger(Configuration.class);
		// TODo Share resources (properties files) inter modules
		/** The Constant CONFIG_FILE. */
		private final static String CONFIG_FILE = "/gateway.properties";

		/** The Constant configuration. */
		private final static Map<Configuration, String> configuration = new EnumMap<>(Configuration.class);

		static {
			readConfigurationFrom(CONFIG_FILE);
		}

		/**
		 * Read configuration from.
		 * 
		 * @param fileName
		 *            the file name
		 */
		private static void readConfigurationFrom(String fileName) {
			logger.info("read configuration from " + fileName + " for the gateway helper");
			try (InputStream resource = Configuration.class.getResourceAsStream(fileName);) {
				Properties properties = new Properties();
				properties.load(resource); // throws a NPE if resource not
											// founds
				for (String key : properties.stringPropertyNames()) {
					Configuration c = getConfigurationKey(key);
					if (c != null)
						configuration.put(c, properties.getProperty(key));
				}
			} catch (IllegalArgumentException | IOException  e) {
				logger.error("Exception when reading configuration file " + fileName + ": " + e);
			}
		}

		/**
		 * Gets the configuration key.
		 * 
		 * @param key
		 *            the key
		 * @return the configuration key
		 */
		private static Configuration getConfigurationKey(String key) {
			for (Configuration c : values()) {
				if (c.key.equals(key)) {
					return c;
				}
			}
			return null;
		}

		/**
		 * Gets the.
		 * 
		 * @return the property corresponding to the key or null if not found
		 */
		public String get() {
			String c = configuration.get(this);
			if (c == null)
				throw new IllegalArgumentException();
			return c;
		}
	}
}
