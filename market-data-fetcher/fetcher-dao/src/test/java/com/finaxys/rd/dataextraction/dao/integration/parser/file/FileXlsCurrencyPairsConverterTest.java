package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file.FileXlsCurrencyPairsParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;

public class FileXlsCurrencyPairsConverterTest {

	FileXlsCurrencyPairsParser target;

	@Before
	public void setUp() {
		target = new FileXlsCurrencyPairsParser();
	}

	@Test
	public void test_convert() throws IOException, JAXBException  {

		CurrencyPair currencyPair = new 
		CurrencyPair('0', new DateTime(), "EURUSD",
				DataType.REF, '1', "EUR",
				 "USD");

		List<CurrencyPair> outList = new ArrayList<CurrencyPair>();
		outList.add(currencyPair);
		DataWrapper<CurrencyPair> out = new DataWrapper<CurrencyPair>(
				outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsCurrencyPairsParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<CurrencyPair> inList = target.parse(inMessageFixture);
		DataWrapper<CurrencyPair> in= new DataWrapper<CurrencyPair>(
				inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}
	
	@Test
	public void test_convert_bad_symbol() throws IOException, JAXBException   {

		
		DataWrapper<CurrencyPair> out = new DataWrapper<CurrencyPair>(
				);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsCurrencyPairsParser/test_convert_bad_symbol.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<CurrencyPair> inList = target.parse(inMessageFixture);
		DataWrapper<CurrencyPair> in= new DataWrapper<CurrencyPair>(
				inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}


}