package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file.FileXlsOptionsParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;

public class FileXlsOptionsParserTest {

	FileXlsOptionsParser target;
	String inputDateFormat;
	String expirationDateFormat;

	@Before
	public void setUp() {
		target = new FileXlsOptionsParser();
		inputDateFormat = "yyyy-MM-dd";
		expirationDateFormat = "dd-MMM-yyyy";
		ReflectionTestUtils.setField(target, "INPUT_DATE_FORMAT",
				inputDateFormat);
		ReflectionTestUtils.setField(target, "EXPIRATION_DATE_FORMAT",
				expirationDateFormat);
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {
		DateTimeFormatter inputFormatter = DateTimeFormat.forPattern(
				inputDateFormat).withLocale(Locale.ENGLISH);
		DateTimeFormatter expirationFormatter = DateTimeFormat.forPattern(
				expirationDateFormat).withLocale(Locale.ENGLISH);

		Option option = new Option('0', new DateTime(), "A140816C00050000",
				DataType.REF, '1', "NY", "A", "C", new BigDecimal("50.0"),
				expirationFormatter.parseDateTime("16-Aug-2014").toLocalDate());
		List<Option> outList = new ArrayList<Option>();
		outList.add(option);
		DataWrapper<Option> out = new DataWrapper<Option>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsOptionsParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<Option> inList = target.parse(inMessageFixture);
		DataWrapper<Option> in = new DataWrapper<Option>(inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	private String marshall(DataWrapper<?> options) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class,
				Option.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(options, os);
		return new String(os.toByteArray());
	}
}