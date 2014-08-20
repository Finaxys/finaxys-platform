package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo.YahooXmlOptionQuotesParser;

public class YahooXmlOptionQuotesParserTest {

	YahooXmlOptionQuotesParser target;
	String dateFormat;
	String expirationDateFormat;

	@Before
	public void setUp()   {
		target = new YahooXmlOptionQuotesParser();
		dateFormat = "MM/dd/yyyy h:mmaa";
		expirationDateFormat = "yyyy-MM-dd";
		ReflectionTestUtils.setField(target, "DATE_FORMAT", dateFormat);
		ReflectionTestUtils.setField(target, "HEADER_EL", "query");
		ReflectionTestUtils.setField(target, "HEADER_NS", "yahoo");
		ReflectionTestUtils.setField(target, "HEADER_CREATED", "created");
		ReflectionTestUtils.setField(target, "EXPIRATION_DATE_FORMAT",
				expirationDateFormat);
		ReflectionTestUtils.setField(target, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(target, "OPTION_EL", "option");
		ReflectionTestUtils
				.setField(target, "OPTIONS_CHAIN_EL", "optionsChain");
		ReflectionTestUtils.setField(target, "SYMBOL_ATT", "symbol");
		ReflectionTestUtils.setField(target, "OCH_SYMBOL_ATT", "symbol");
		ReflectionTestUtils.setField(target, "CHANGE_EL", "change");
		ReflectionTestUtils.setField(target, "LAST_PRICE_EL", "lastPrice");
		ReflectionTestUtils.setField(target, "BID_EL", "bid");
		ReflectionTestUtils.setField(target, "ASK_EL", "ask");
		ReflectionTestUtils.setField(target, "STRIKE_EL", "strikePrice");
		ReflectionTestUtils.setField(target, "EXPIRATION_ATT", "expiration");
		ReflectionTestUtils.setField(target, "VOLUME_EL", "vol");
		ReflectionTestUtils.setField(target, "OPENINTEREST_EL", "openInt");
		ReflectionTestUtils.setField(target, "OPTION_TYPE_ATT", "type");
	}

	@Test
	public void test_convert() throws JAXBException, IOException  {

		// Setup
		DateTimeFormatter formatter = DateTimeFormat.forPattern(
				expirationDateFormat).withLocale(Locale.US);
		OptionQuote optionQuote = new OptionQuote( '1', new DateTime() ,  "GOOG140920C00265000",
				DataType.INTRA ,  new DateTime("2014-07-27T17:17:37Z"),  "GOOG",
				"C", new BigDecimal("309.50"), new BigDecimal("0"),
				new BigDecimal("309.50"), new BigDecimal("309.50"),
				new BigDecimal("322"), new BigDecimal("326.1"), new BigDecimal(
						"265"), formatter.parseDateTime("2014-09-20")
						.toLocalDate(), new BigInteger("1"), new Integer("1"));
		
		List<OptionQuote> outList = new ArrayList<OptionQuote>();
		outList.add(optionQuote);
		commonTest(outList, "/YahooXmlOptionQuotesParser/test_convert.xml");
	}

	@Test
	public void test_convert_no_symbol() throws JAXBException, IOException  {
		commonTest("/YahooXmlOptionQuotesParser/test_convert_no_symbol.xml");
	}

	@Test
	public void test_convert_no_ts() throws JAXBException, IOException  {
		commonTest("/YahooXmlOptionQuotesParser/test_convert_no_ts.xml");
	}



	

	@Test
	public void test_convert_without_option_element() throws JAXBException, IOException  {
		commonTest("/YahooXmlOptionQuotesParser/test_convert_without_option_element.xml");
	}

	@Test
	public void test_convert_without_optionschain_element() throws JAXBException, IOException  {
		commonTest("/YahooXmlOptionQuotesParser/test_convert_without_optionschain_element.xml");
	}


	@Test(expected = DataReadingParserException.class)  
	public void test_convert_fields_not_wellformedxml() throws JAXBException, IOException  {
		commonTest("/YahooXmlOptionQuotesParser/test_convert_not_wellformedxml.xml");
	}

	private void commonTest(List<OptionQuote> list, String testFile) throws JAXBException, IOException
			 {
		// Setup
		DataWrapper<OptionQuote> out = null;
		if (list != null) {
			out = new DataWrapper<OptionQuote>(list);
		} else {
			out = new DataWrapper<OptionQuote>();
		}

		byte[] inData = TestHelper.getResourceAsBytes(testFile);
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<OptionQuote> inList = target.parse(inMessageFixture);
		DataWrapper<OptionQuote> in = new DataWrapper<OptionQuote>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	private void commonTest(String testFile) throws JAXBException, IOException  {
		commonTest(null, testFile);
	}
}