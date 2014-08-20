package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class YahooXmlOptionChainsParser implements Parser<OptionChain> {

	private static Logger logger = Logger.getLogger(YahooXmlOptionChainsParser.class);

	/** The date format. */
	@Value("${parser.yahoo.option_chains.date_format}")
	private String DATE_FORMAT;

	@Value("${parser.yahoo.option_chains.old.no_data}")
	private String NO_DATA;

	@Value("${parser.yahoo.option_chains.old.main_option_el}")
	private String MAIN_OPTION_EL;

	@Value("${parser.yahoo.option_chains.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${parser.yahoo.option_chains.old.contract_el}")
	private String CONTRACT_EL;

	public List<OptionChain> parse(Document document) throws ParserException {
		XMLEventReader reader;
		try {

			Assert.notNull(document, "Cannot parse Null document");
			XMLInputFactory ifactory = XMLInputFactory.newInstance();
			InputStream is = new ByteArrayInputStream(document.getContent());
			StreamSource source = new StreamSource(is);

			reader = ifactory.createXMLEventReader(source);

			List<OptionChain> list = new ArrayList<OptionChain>();
			DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

			String symbol = null;
			OptionChain optionChain = null;
			while (reader.hasNext()) {
				try {
					XMLEvent ev = reader.nextEvent();

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(MAIN_OPTION_EL)) {
						Attribute a = ((StartElement) ev).getAttributeByName(new QName(SYMBOL_ATT));
						if (a != null && !a.getValue().equals(""))
							symbol = a.getValue();
					}
					if (symbol != null && ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CONTRACT_EL) && document.getDataType() != null) {
						XMLEvent evt = reader.nextEvent();
						if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA)) {
							optionChain = new OptionChain(YahooGatewayHelper.Y_PROVIDER_SYMB, new DateTime(), symbol, document.getDataType(), YahooGatewayHelper.Y_PROVIDER_SYMB, formatter
									.parseDateTime(evt.asCharacters().getData()).toLocalDate());
							list.add(optionChain);
						} else {
							optionChain = null;
						}
					}
					if (ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(MAIN_OPTION_EL)) {

						symbol = null;
					}

				} catch ( UnsupportedOperationException | IllegalArgumentException e) {
					logger.error("Exception when creating a new object by the parser: " + e);

					break;
				} catch (XMLStreamException | NoSuchElementException e) {
					throw new DataReadingParserException(e);
				}
			}
			return list;
		} catch ( FactoryConfigurationError | XMLStreamException e) {
			throw new DataReadingParserException(e);
		}

	}

}
