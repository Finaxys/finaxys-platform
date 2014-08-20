package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.finaxys.finaxysplatform.core.domain.FXRate;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo.YahooXmlFXRatesParser;

public class YahooXmlFXRatesParserTest {

	YahooXmlFXRatesParser target;
	String dateFormat;

	@Before
	public void setUp()   {
		target = new YahooXmlFXRatesParser();
		dateFormat = "MM/dd/yyyy h:mmaa";
		ReflectionTestUtils.setField(target, "DATE_FORMAT", dateFormat);
		ReflectionTestUtils.setField(target, "MAIN_RATE_EL", "rate");
		ReflectionTestUtils.setField(target, "RATE_EL", "Rate");
		ReflectionTestUtils.setField(target, "ID_ATT", "id");
		ReflectionTestUtils.setField(target, "DATE_EL", "Date");
		ReflectionTestUtils.setField(target, "TIME_EL", "Time");
		ReflectionTestUtils.setField(target, "NO_DATA", "N/A");
		ReflectionTestUtils.setField(target, "ASK_EL", "Ask");
		ReflectionTestUtils.setField(target, "BID_EL", "Bid");
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {

		// Setup
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		FXRate fxRate = new FXRate('1', new DateTime(), "EURUSD",
				DataType.INTRA, formatter.parseDateTime("7/23/2014 10:35pm"),
				new BigDecimal("1.3463"), new BigDecimal("1.3463"),
				new BigDecimal("1.3462"));
		List<FXRate> outList = new ArrayList<FXRate>();
		outList.add(fxRate);
		DataWrapper<FXRate> out = new DataWrapper<FXRate>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_no_id() throws JAXBException, IOException   {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_no_id.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	@Test
	public void test_convert_no_ts() throws IOException, JAXBException   {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_no_ts.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}

	


	@Test
	public void test_convert_without_rate_element() throws JAXBException, IOException   {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_without_rate_element.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);
		DataWrapper<FXRate> in = new DataWrapper<FXRate>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}


	@Test(expected = DataReadingParserException.class)  
	public void test_convert_fields_not_wellformedxml() throws IOException   {

		// Setup
		DataWrapper<FXRate> out = new DataWrapper<FXRate>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/YahooXmlFXRatesParser/test_convert_not_wellformedxml.xml");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('1');
		inMessageFixture.setDataType(DataType.INTRA);

		// Execution
		List<FXRate> inList = target.parse(inMessageFixture);

	}

}