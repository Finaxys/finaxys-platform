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
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo.YahooXmlOQuotesParser;

public class YahooXmlOQuotesParserTest {

	YahooXmlOQuotesParser target;
	String dateFormat;
	String expirationDateFormat;

	@Before
	public void setUp()  {
		target = new YahooXmlOQuotesParser();
		dateFormat = "MM/dd/yyyy h:mmaa";
		expirationDateFormat = "dd-MMM-yy";
		ReflectionTestUtils.setField(target, "DATE_FORMAT", dateFormat);
		ReflectionTestUtils.setField(target, "HEADER_EL", "query");
		ReflectionTestUtils.setField(target, "HEADER_NS", "yahoo");
		ReflectionTestUtils.setField(target, "HEADER_CREATED", "created");
		ReflectionTestUtils.setField(target, "EXPIRATION_DATE_FORMAT", expirationDateFormat );
		ReflectionTestUtils.setField(target, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(target, "OPTION_EL", "option");
		ReflectionTestUtils.setField(target, "SYMBOL_ATT", "sym");
		ReflectionTestUtils.setField(target, "CHANGE_EL", "change");
		ReflectionTestUtils.setField(target, "PRICE_EL", "price");
		ReflectionTestUtils.setField(target, "PREVCLOSE_EL", "prevClose");
		ReflectionTestUtils.setField(target, "OPEN_EL", "open");
		ReflectionTestUtils.setField(target, "BID_EL", "bid");
		ReflectionTestUtils.setField(target, "ASK_EL", "ask");
		ReflectionTestUtils.setField(target, "STRIKE_EL", "strike");
		ReflectionTestUtils.setField(target, "EXPIRATION_EL", "expire");
		ReflectionTestUtils.setField(target, "VOLUME_EL", "volume");
		ReflectionTestUtils.setField(target, "OPENINTEREST_EL", "openInterest");
	}

	@Test
	public void test_convert() throws JAXBException, IOException  {

		DateTimeFormatter formatter = DateTimeFormat.forPattern(expirationDateFormat).withLocale(Locale.US);
	
		OptionQuote optionQuote = new OptionQuote( '1', new DateTime() ,  "A140816C00060000",
				DataType.INTRA ,  new DateTime("2014-07-24T02:54:40Z"),null, null,new  BigDecimal("0.73"), new BigDecimal("+0.14"), new BigDecimal ("0.79"), new BigDecimal ("1.03"),new BigDecimal ("0.65"), new BigDecimal ("0.70"), new BigDecimal ("60.00"),
				formatter.parseDateTime("16-Aug-14").toLocalDate(), new BigInteger ("48"), new Integer ("7330"));
		
		
		List<OptionQuote> outList = new ArrayList<OptionQuote>();
		outList.add(optionQuote);
		commonTest(outList, "/YahooXmlOQuotesParser/test_convert.xml");
		}

	@Test
	public void test_convert_no_symbol() throws JAXBException, IOException  {

		commonTest("/YahooXmlOQuotesParser/test_convert_no_symbol.xml");
		}

	@Test
	public void test_convert_no_ts() throws JAXBException, IOException  {

		commonTest("/YahooXmlOQuotesParser/test_convert_no_ts.xml");}


	@Test
	public void test_convert_without_option_element() throws JAXBException, IOException  {

		commonTest("/YahooXmlOQuotesParser/test_convert_without_option_element.xml");
	}


	@Test(expected = DataReadingParserException.class)  
	public void test_convert_fields_not_wellformedxml() throws JAXBException, IOException  {

		commonTest("/YahooXmlOQuotesParser/test_convert_not_wellformedxml.xml");}
	
	private void commonTest(List<OptionQuote> list, String testFile) throws JAXBException, IOException
			 {
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