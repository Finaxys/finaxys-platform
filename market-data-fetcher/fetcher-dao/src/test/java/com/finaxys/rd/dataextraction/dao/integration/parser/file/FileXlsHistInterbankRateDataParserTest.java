package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file.FileXlsHistInterbankRateDataParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;

public class FileXlsHistInterbankRateDataParserTest {

	FileXlsHistInterbankRateDataParser target;

	@Before
	public void setUp() {
		target = new FileXlsHistInterbankRateDataParser();
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {

		InterbankRateData interbankRateData = new InterbankRateData('0',
				new DateTime(), "LIBOR", DataType.HIST, new DateTime(
						"2001-01-02"), "USD", "0D", new BigDecimal(
						"6.65125"));
		List<InterbankRateData> outList = new ArrayList<InterbankRateData>();
		outList.add(interbankRateData);
		DataWrapper<InterbankRateData> out = new DataWrapper<InterbankRateData>(
				outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsHistInterbankRateDataParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.HIST);

		// Execution
		List<InterbankRateData> inList = target.parse(inMessageFixture);
		DataWrapper<InterbankRateData> in = new DataWrapper<InterbankRateData>(
				inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	@Test
	public void test_convert_bad_dates() throws IOException, JAXBException   {

		DataWrapper<InterbankRateData> out = new DataWrapper<InterbankRateData>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsHistInterbankRateDataParser/test_convert_bad_dates.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.HIST);

		// Execution
		List<InterbankRateData> inList = target.parse(inMessageFixture);
		DataWrapper<InterbankRateData> in = new DataWrapper<InterbankRateData>(
				inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	@Test
	public void test_convert_bad_rate_value() throws IOException, JAXBException   {

		DataWrapper<InterbankRateData> out = new DataWrapper<InterbankRateData>();

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsHistInterbankRateDataParser/test_convert_bad_rate_value.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.HIST);

		// Execution
		List<InterbankRateData> inList = target.parse(inMessageFixture);
		DataWrapper<InterbankRateData> in = new DataWrapper<InterbankRateData>(
				inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	private String marshall(DataWrapper<?> exchanges) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class,
				InterbankRateData.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(exchanges, os);
		return new String(os.toByteArray());
	}
}