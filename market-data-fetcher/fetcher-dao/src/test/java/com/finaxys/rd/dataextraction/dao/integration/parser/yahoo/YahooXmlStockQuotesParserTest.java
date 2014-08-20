package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.StockQuote;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo.YahooXmlStockQuotesParser;

public class YahooXmlStockQuotesParserTest {

	YahooXmlStockQuotesParser target;
	String dateFormat;

	@Before
	public void setUp()  {
		target = new YahooXmlStockQuotesParser();
		dateFormat = "MM/dd/yyyy h:mmaa";
		ReflectionTestUtils.setField(target, "DATE_FORMAT", dateFormat);
		ReflectionTestUtils.setField(target, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(target, "HEADER_EL", "query");
		ReflectionTestUtils.setField(target, "HEADER_NS", "yahoo");
		ReflectionTestUtils.setField(target, "HEADER_CREATED", "created");
		ReflectionTestUtils.setField(target, "QUOTE_EL", "quote");
		ReflectionTestUtils.setField(target, "AVERAGEDAILYVOLUME_EL", "AverageDailyVolume");
		ReflectionTestUtils.setField(target, "CHANGE_EL", "Change");
		ReflectionTestUtils.setField(target, "DAYSLOW_EL", "DaysLow");
		ReflectionTestUtils.setField(target, "DAYSHIGH_EL", "DaysHigh");
		ReflectionTestUtils.setField(target, "YEARLOW_EL", "YearLow");
		ReflectionTestUtils.setField(target, "YEARHIGH_EL", "YearHigh");
		ReflectionTestUtils.setField(target, "MARKETCAPITALIZATION_EL", "MarketCapitalization");
		ReflectionTestUtils.setField(target, "LASTTRADEPRICEONLY_EL", "LastTradePriceOnly");
		ReflectionTestUtils.setField(target, "DAYSRANGE_EL", "DaysRange");
		ReflectionTestUtils.setField(target, "NAME_EL", "Name");
		ReflectionTestUtils.setField(target, "SYMBOL_ATT", "symbol");
		ReflectionTestUtils.setField(target, "VOLUME_EL", "Volume");
	}

	@Test
	public void test_convert() throws JAXBException, IOException  {

		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		StockQuote stockQuote = new StockQuote('1', new DateTime(), "YHOO", DataType.INTRA, new DateTime("2014-07-23T13:56:28Z"), "US", new BigInteger("19698700"), new BigDecimal(
				"+0.14"), new BigDecimal("33.71"), new BigDecimal("33.80"), new BigDecimal("26.75"), new BigDecimal("41.72"), "34.236B", new BigDecimal("33.74"), "33.71 - 33.80",
				"Yahoo! Inc.", new BigInteger("743201"), new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("0.0"));

		List<StockQuote> outList = new ArrayList<StockQuote>();
		outList.add(stockQuote);
		commonTest(outList, "/YahooXmlStockQuotesParser/test_convert.xml");
	}

	@Test
	public void test_convert_no_symbol() throws JAXBException, IOException  {

		commonTest("/YahooXmlStockQuotesParser/test_convert_no_symbol.xml");
	}

	@Test
	public void test_convert_no_ts() throws JAXBException, IOException  {

		commonTest("/YahooXmlStockQuotesParser/test_convert_no_ts.xml");
	}

	@Test
	public void test_convert_without_quote_element() throws JAXBException, IOException  {

		commonTest("/YahooXmlStockQuotesParser/test_convert_without_quote_element.xml");
	}

	@Test(expected = DataReadingParserException.class)  
	public void test_convert_fields_not_wellformedxml_throws_DataReadingParserException() throws JAXBException, IOException  {

		commonTest("/YahooXmlStockQuotesParser/test_convert_not_wellformedxml.xml");
		
	}

	private void commonTest(List<StockQuote> list, String testFile) throws JAXBException, IOException  {
		DataWrapper<StockQuote> out = null;
		if (list != null) {
			out = new DataWrapper<StockQuote>(list);
		} else {
			out = new DataWrapper<StockQuote>();
		}

		byte[] inData = TestHelper.getResourceAsBytes(testFile);
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<StockQuote> inList = target.parse(inMessageFixture);
		DataWrapper<StockQuote> in = new DataWrapper<StockQuote>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	private void commonTest(String testFile) throws JAXBException, IOException  {
		commonTest(null, testFile);
	}
}