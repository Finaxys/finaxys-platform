package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Exchange;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file.FileXlsExchangesParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;

public class FileXlsExchangesParserTest {

	FileXlsExchangesParser target;

	@Before
	public void setUp() {
		target = new FileXlsExchangesParser();
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {
		Exchange exchange = new Exchange('0', new DateTime(), "XLON",
				DataType.REF, "L", '1', "LONDON STOCK EXCHANGE", "type",
				"Europe", "UNITED KINGDOM", "GBP", new LocalTime("11:00:00"),
				new LocalTime("15:00:00"), new Integer("0"));

		List<Exchange> outList = new ArrayList<Exchange>();
		outList.add(exchange);
		DataWrapper<Exchange> out = new DataWrapper<Exchange>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsExchangesParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<Exchange> inList = target.parse(inMessageFixture);
		DataWrapper<Exchange> in = new DataWrapper<Exchange>(inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	@Test
	public void test_convert_bad_dates() throws Exception {

		DataWrapper<Exchange> out = new DataWrapper<Exchange>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsExchangesParser/test_convert_bad_dates.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<Exchange> inList = target.parse(inMessageFixture);
		DataWrapper<Exchange> in = new DataWrapper<Exchange>(inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	private String marshall(DataWrapper<?> exchanges) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class,
				Exchange.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(exchanges, os);
		return new String(os.toByteArray());
	}
}