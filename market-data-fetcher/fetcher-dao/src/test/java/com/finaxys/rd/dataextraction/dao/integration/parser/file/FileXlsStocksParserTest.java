package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file.FileXlsStocksParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;

public class FileXlsStocksParserTest {

	FileXlsStocksParser target;

	@Before
	public void setUp() {
		target = new FileXlsStocksParser();
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {
		Stock stock = new Stock();
		stock.setSymbol("TIF");
		stock.setCompanyName("Tiffany & Co.");
		stock.setExchSymb("NY");
		stock.setProvider('1');
		stock.setSource('0');
		stock.setDataType(DataType.REF);
		stock.setInputDate(new DateTime());
		List<Stock> outList = new ArrayList<Stock>();
		outList.add(stock);
		DataWrapper<Stock> out = new DataWrapper<Stock>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsStocksParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<Stock> inList = target.parse(inMessageFixture);
		DataWrapper<Stock> in = new DataWrapper<Stock>(inList);

		// Verification
		assertEquals(TestHelper.marshall(out), TestHelper.marshall(in));
	}


}