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
import com.finaxys.finaxysplatform.core.domain.IndexQuote;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo.YahooXmlIndexQuotesParser;

public class YahooXmlIndexQuotesParserTest {

	YahooXmlIndexQuotesParser target;
	String dateFormat;

	@Before
	public void setUp()   {
		target = new YahooXmlIndexQuotesParser();
		dateFormat = "MM/dd/yyyy h:mmaa";
		ReflectionTestUtils.setField(target, "DATE_FORMAT", dateFormat);
		ReflectionTestUtils.setField(target, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(target, "QUOTE_EL", "quote");
		ReflectionTestUtils.setField(target, "SYMBOL_ATT", "symbol");
		ReflectionTestUtils.setField(target, "LASTTRADEPRICEONLY_EL",
				"LastTradePriceOnly");
		ReflectionTestUtils.setField(target, "LASTTRADEDATE_EL",
				"LastTradeDate");
		ReflectionTestUtils.setField(target, "LASTTRADETIME_EL",
				"LastTradeTime");
		ReflectionTestUtils.setField(target, "CHANGE_EL", "Change");
		ReflectionTestUtils.setField(target, "OPEN_EL", "Open");
		ReflectionTestUtils.setField(target, "DAYSHIGH_EL", "DaysHigh");
		ReflectionTestUtils.setField(target, "DAYSLOW_EL", "DaysLow");
		ReflectionTestUtils.setField(target, "VOLUME_EL", "Volume");
	}

	@Test
	public void test_convert() throws IOException, JAXBException     {

		// Setup
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		IndexQuote indexQuote = new IndexQuote('1', new DateTime(), "^GSPC",
				DataType.INTRA, formatter.parseDateTime("7/22/2014 4:30pm"),
				"US", new BigDecimal("1983.53"), new BigDecimal("+9.90"),
				new BigDecimal("1975.65"), new BigDecimal("1986.24"),
				new BigDecimal("1975.65"), new BigInteger("425726752"),
				new BigDecimal("0"), new BigDecimal("0"));
		List<IndexQuote> outList = new ArrayList<IndexQuote>();
		outList.add(indexQuote);
		commonTest(outList, "/YahooXmlIndexQuotesParser/test_convert.xml");
	}

	@Test
	public void test_convert_no_id() throws IOException, JAXBException   {

		commonTest("/YahooXmlIndexQuotesParser/test_convert_no_id.xml");
	}

	@Test
	public void test_convert_no_ts() throws IOException, JAXBException   {

		commonTest("/YahooXmlIndexQuotesParser/test_convert_no_ts.xml");
	}


	@Test(expected = DataReadingParserException.class)  
	public void test_convert_without_quote_element() throws IOException, JAXBException   {

		commonTest("/YahooXmlIndexQuotesParser/test_convert_not_wellformedxml.xml");
	}




	@Test(expected = DataReadingParserException.class)  
	public void test_convert_not_wellformedxml() throws IOException, JAXBException   {
		commonTest("/YahooXmlIndexQuotesParser/test_convert_not_wellformedxml.xml");
		
	}

	private void commonTest(List<IndexQuote> list, String testFile) throws IOException, JAXBException
			  {
		// Setup
		DataWrapper<IndexQuote> out = null;
		if (list != null) {
			out = new DataWrapper<IndexQuote>(list);
		} else {
			out = new DataWrapper<IndexQuote>();
		}

		byte[] inData = TestHelper.getResourceAsBytes(testFile);
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<IndexQuote> inList = target.parse(inMessageFixture);
		DataWrapper<IndexQuote> in = new DataWrapper<IndexQuote>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	private void commonTest(String testFile) throws IOException, JAXBException   {
		commonTest(null, testFile);
	}

}