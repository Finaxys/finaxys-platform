package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.yahoo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.core.domain.StockQuote;
import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;
import com.finaxys.finaxysplatform.core.domain.Enum.DataClass;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayCommunicationException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewaySecurityException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.EODDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.HistDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.IntradayDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class YahooStockQuoteGateway implements IntradayDataGateway<StockQuote, Stock>, EODDataGateway<StockQuote, Stock>, HistDataGateway<StockQuote, Stock> {

	@Value("${gateway.yahoo.yqlStockQuoteQuery}")
	private String CURRENT_QUOTE_QUERY;

	@Value("${gateway.yahoo.yqlEODStockQuoteQuery}")
	private String EOD_QUOTE_QUERY;

	@Value("${gateway.yahoo.yqlHistStockQuoteQuery}")
	private String HIST_QUOTE_QUERY;

	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;

	/** The content type. */
	private ContentType contentType;

	private Parser<StockQuote> intradayDataParser;

	private Parser<StockQuote> eodDataParser;

	private Parser<StockQuote> histDataParser;

	public YahooStockQuoteGateway(ContentType contentType, Parser<StockQuote> intradayDataParser, Parser<StockQuote> eodDataParser, Parser<StockQuote> histDataParser) {
		super();
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.histDataParser = histDataParser;
		this.context = HttpClientContext.create();
	}

	public YahooStockQuoteGateway(CloseableHttpClient httpClient, ContentType contentType, Parser<StockQuote> intradayDataParser, Parser<StockQuote> eodDataParser,
			Parser<StockQuote> histDataParser) {
		super();
		this.httpClient = httpClient;
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.histDataParser = histDataParser;
		this.context = HttpClientContext.create();
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Parser<StockQuote> getIntradayDataParser() {
		return intradayDataParser;
	}

	public void setIntradayDataParser(Parser<StockQuote> intradayDataParser) {
		this.intradayDataParser = intradayDataParser;
	}

	public Parser<StockQuote> getEodDataParser() {
		return eodDataParser;
	}

	public void setEodDataParser(Parser<StockQuote> eodDataParser) {
		this.eodDataParser = eodDataParser;
	}

	@Override
	public List<StockQuote> getEODData(List<Stock> products) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getStocksSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(EOD_QUOTE_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return eodDataParser.parse(new Document(data, DataType.EOD));

			return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch ( ParserException e) {
			throw new GatewayException(e);
		}
	}

	@Override
	public List<StockQuote> getCurrentData(List<Stock> products) {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getStocksSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(CURRENT_QUOTE_QUERY, params, contentType, httpClient, context);
			if (data.length > 0)
				return intradayDataParser.parse(new Document(data, DataType.INTRA));

			return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch ( ParserException e) {
			throw new GatewayException(e);
		}
	}

	@Override
	public List<StockQuote> getHistData(List<Stock> products, LocalDate startDate, LocalDate endDate) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			Assert.notNull(startDate, "Cannot execute data extraction. Start date is null.");
			Assert.notNull(endDate, "Cannot execute data extraction. End date is null.");

			 DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getStocksSymbols(products), dformatter.print(startDate), dformatter.print(endDate)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(HIST_QUOTE_QUERY, params, contentType, httpClient, context);

			if (data.length > 0)
				return histDataParser.parse(new Document(contentType, DataType.HIST, DataClass.StockQuote, YahooGatewayHelper.Y_PROVIDER_SYMB, data));

			return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch ( ParserException e) {
			throw new GatewayException(e);
		}
	}

}