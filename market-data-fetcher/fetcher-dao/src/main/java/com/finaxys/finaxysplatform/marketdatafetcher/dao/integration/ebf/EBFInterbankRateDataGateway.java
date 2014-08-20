package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.ebf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;
import com.finaxys.finaxysplatform.core.domain.Enum.DataClass;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayCommunicationException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewaySecurityException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.EBFGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.HistDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class EBFInterbankRateDataGateway implements HistDataGateway<InterbankRateData, InterbankRate> {

	@Autowired
	private CloseableHttpClient httpClient;

	private final HttpContext context;

	/** The content type. */
	private ContentType contentType;

	private Parser<InterbankRateData> histDataParser;

	public EBFInterbankRateDataGateway(ContentType contentType, Parser<InterbankRateData> histDataParser) {
		super();
		this.contentType = contentType;
		this.histDataParser = histDataParser;
		this.context = HttpClientContext.create();
	}

	public EBFInterbankRateDataGateway(CloseableHttpClient httpClient, ContentType contentType, Parser<InterbankRateData> histDataParser) {
		super();
		this.httpClient = httpClient;
		this.contentType = contentType;
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

	public Parser<InterbankRateData> getHistDataParser() {
		return histDataParser;
	}

	public void setHistDataParser(Parser<InterbankRateData> histDataParser) {
		this.histDataParser = histDataParser;
	}

	@Override
	public List<InterbankRateData> getHistData(List<InterbankRate> products, LocalDate startDate, LocalDate endDate) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			Assert.notNull(startDate, "Cannot execute data extraction. Start date is null.");
			Assert.notNull(endDate, "Cannot execute data extraction. End date is null.");

			URI uri = EBFGatewayHelper.contructEBFHistEuriborUri(startDate.getYear(), contentType);
			HttpGet request = new HttpGet(uri);
			YahooGatewayHelper.signOAuthYQLRequest(request);
			CloseableHttpResponse response = httpClient.execute(request, context);
			byte[] data = IOUtils.toByteArray(response.getEntity().getContent());

			if (data.length > 0)
				return histDataParser.parse(new Document(contentType, DataType.HIST, DataClass.InterbankRatesData, EBFGatewayHelper.EBF_PROVIDER_SYMB, data));

			return null;
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException e) {
			throw new GatewaySecurityException(e);
		} catch (OAuthCommunicationException | URISyntaxException | IOException e) {
			throw new GatewayCommunicationException(e);
		} catch (ParserException e) {
			throw new GatewayException(e);
		}
	}

}
