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
import com.finaxys.finaxysplatform.core.domain.Index;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file.FileXlsIndexesParser;
import com.finaxys.rd.dataextraction.dao.integration.parser.yahoo.TestHelper;

public class FileXlsIndexesParserTest {

	FileXlsIndexesParser target;

	@Before
	public void setUp() {
		target = new FileXlsIndexesParser();
	}

	@Test
	public void test_convert() throws IOException, JAXBException   {

		Index index = new Index('0', new DateTime(), "^DJX", DataType.REF, '1',
				"1/100 DOW JONES INDUSTRIAL AVER", "NY");
		List<Index> outList = new ArrayList<Index>();
		outList.add(index);
		DataWrapper<Index> out = new DataWrapper<Index>(outList);

		byte[] inData = TestHelper
				.getResourceAsBytes("/FileXlsIndexesParser/test_convert.xls");
		Document inMessageFixture = new Document(inData);
		inMessageFixture.setSource('0');
		inMessageFixture.setDataType(DataType.REF);

		// Execution
		List<Index> inList = target.parse(inMessageFixture);
		DataWrapper<Index> in = new DataWrapper<Index>(inList);

		// Verification
		assertEquals(marshall(out), marshall(in));
	}

	private String marshall(DataWrapper<?> indexes) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class,
				Index.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(indexes, os);
		return new String(os.toByteArray());
	}
}