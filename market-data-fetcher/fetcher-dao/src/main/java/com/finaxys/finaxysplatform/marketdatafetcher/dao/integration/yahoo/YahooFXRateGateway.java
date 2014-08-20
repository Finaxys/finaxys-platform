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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.FXRate;
import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayCommunicationException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewaySecurityException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.EODDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.IntradayDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class YahooFXRateGateway implements IntradayDataGateway<FXRate, CurrencyPair>, EODDataGateway<FXRate, CurrencyPair> {

	@Value("${gateway.yahoo.yqlFXRateQuery}")
	private String currentFXRateQuery;

	@Value("${gateway.yahoo.yqlEODFXRateQuery}")
	private String eodFXRateQuey;

	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;

	/** The content type. */
	private ContentType contentType;

	private Parser<FXRate> intradayDataParser;

	private Parser<FXRate> eodDataParser;

	public YahooFXRateGateway(ContentType contentType, Parser<FXRate> intradayDataParser, Parser<FXRate> eodDataParser) {
		super();
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
		this.context = HttpClientContext.create();
	}

	public YahooFXRateGateway(CloseableHttpClient httpClient, ContentType contentType, Parser<FXRate> intradayDataParser, Parser<FXRate> eodDataParser) {
		super();
		this.httpClient = httpClient;
		this.contentType = contentType;
		this.intradayDataParser = intradayDataParser;
		this.eodDataParser = eodDataParser;
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

	public Parser<FXRate> getIntradayDataParser() {
		return intradayDataParser;
	}

	public void setIntradayDataParser(Parser<FXRate> intradayDataParser) {
		this.intradayDataParser = intradayDataParser;
	}

	public Parser<FXRate> getEodDataParser() {
		return eodDataParser;
	}

	public void setEodDataParser(Parser<FXRate> eodDataParser) {
		this.eodDataParser = eodDataParser;
	}

	@Override
	public List<FXRate> getEODData(List<CurrencyPair> products) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");

			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getSymbols(products)));
			byte[] data;
			data = YahooGatewayHelper.executeYQLQuery(eodFXRateQuey, params, contentType, httpClient, context);
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
	public List<FXRate> getCurrentData(List<CurrencyPair> products) throws GatewayException {

		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");

			List<String> params = new ArrayList<String>(Arrays.asList(YahooGatewayHelper.getSymbols(products)));
			byte[] data = YahooGatewayHelper.executeYQLQuery(currentFXRateQuery, params, contentType, httpClient, context);
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

}