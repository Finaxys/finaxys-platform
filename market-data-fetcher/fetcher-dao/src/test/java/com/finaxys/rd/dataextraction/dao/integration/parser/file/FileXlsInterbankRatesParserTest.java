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
import org.junit.Before;
import org.junit.Test;

import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file.FileXlsInterbankRatesParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;

public class FileXlsInterbankRatesParserTest {

	FileXlsInterbankRatesParser target;

	@Before
	public void setUp() {
		target = new FileXlsInterbankRatesParser();
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {

		InterbankRate interbankRate = new InterbankRate('0', new DateTime(),
				"EONIA", DataType.REF, '1', "EUR");
		List<InterbankRate> outList = new ArrayList<InterbankRate>();
		outList.add(interbankRate);
		DataWrapper<InterbankRate> out = new DataWrapper<InterbankRate>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsInterbankRatesParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<InterbankRate> inList = target.parse(inMessageFixture);
		DataWrapper<InterbankRate> in = new DataWrapper<InterbankRate>(inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	private String marshall(DataWrapper<?> interbankRates) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class,
				InterbankRate.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(interbankRates, os);
		return new String(os.toByteArray());
	}
}