package com.finaxys.rd.dataextraction.dao.integration.parser.ebf;

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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.ebf.EbfXlsHistInterbankRateDataParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;


public class EbfXlsHistInterbankRateDataParserTest {

	EbfXlsHistInterbankRateDataParser target;

	@Before
	public void setUp() {
		target = new EbfXlsHistInterbankRateDataParser();
		ReflectionTestUtils.setField(target, "EURIBOR_SYMB", "EURIBOR");
		ReflectionTestUtils.setField(target, "EURO_SYMB", "EUR");

	}

	@Test
	public void test_convert() throws IOException, JAXBException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
		
		InterbankRateData interbankRateData = new InterbankRateData('0', new DateTime(), "EURIBOR",
				DataType.HIST, formatter.parseDateTime("02/01/2014"), "EUR",
				"1W", new BigDecimal("0.183")); 
				
		List<InterbankRateData> outInterbankRateDatasList = new ArrayList<InterbankRateData>();
		outInterbankRateDatasList.add(interbankRateData);
		DataWrapper<InterbankRateData> outInterbankRateDatas = new DataWrapper<InterbankRateData>(outInterbankRateDatasList);
		

		
		byte[] inData = TestHelper.getResourceAsBytes("/EbfXlsHistEuriborParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.HIST);

		// Execution
		List<InterbankRateData> inInterbankRateDatasList = target.parse(inMessageFixture);
		DataWrapper<InterbankRateData> inInterbankRateDatas = new DataWrapper<InterbankRateData>(inInterbankRateDatasList);
		
		// Verification
		assertEquals(marshall(outInterbankRateDatas), marshall(inInterbankRateDatas));
	}
	

	
	private String marshall(DataWrapper<?> interbankRateDatas) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class, InterbankRateData.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(interbankRateDatas, os);
		return new String(os.toByteArray());
	}
}