package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.core.domain.DataWrapper;
import com.finaxys.finaxysplatform.core.domain.FXRate;
import com.finaxys.finaxysplatform.core.domain.IndexQuote;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.core.domain.StockQuote;

public class TestHelper {

	
	public static byte[] getResourceAsBytes(String path) throws IOException{
		InputStream in = TestHelper.class.getResourceAsStream(path);
		return IOUtils.toByteArray(in);
	}
	
	public static String marshall(DataWrapper<?> currencyPairs) throws JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataWrapper.class,
				CurrencyPair.class, Stock.class, FXRate.class, IndexQuote.class, OptionChain.class
				,OptionQuote.class, StockQuote.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(currencyPairs, os);
		return new String(os.toByteArray());
	}
}
