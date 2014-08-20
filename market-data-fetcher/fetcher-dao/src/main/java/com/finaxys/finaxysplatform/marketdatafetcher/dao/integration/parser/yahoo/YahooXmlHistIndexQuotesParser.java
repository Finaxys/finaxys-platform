/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.IndexQuote;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlHistIndexQuotesParser implements Parser<IndexQuote> {

	private static Logger logger = Logger.getLogger(YahooXmlHistIndexQuotesParser.class);

	/** The date format. */
	@Value("${parser.yahoo.hist_index_quotes.date_format}")
	private String dateFormat;

	@Value("${parser.yahoo.hist_index_quotes.old.no_data}")
	private String noData;

	@Value("${parser.yahoo.hist_index_quotes.old.quote_el}")
	private String quoteEl;

	@Value("${parser.yahoo.hist_index_quotes.old.symbol_att}")
	private String symbolAtt;

	@Value("${parser.yahoo.hist_index_quotes.old.date_el}")
	private String dateEl;

	@Value("${parser.yahoo.hist_index_quotes.old.open_el}")
	private String openEl;

	@Value("${parser.yahoo.hist_index_quotes.old.high_el}")
	private String highEl;

	@Value("${parser.yahoo.hist_index_quotes.old.low_el}")
	private String lowEl;

	@Value("${parser.yahoo.hist_index_quotes.old.close_el}")
	private String closeEl;

	@Value("${parser.yahoo.hist_index_quotes.old.volume_el}")
	private String volumeEl;

	@Value("${parser.yahoo.hist_index_quotes.old.adj_close_el}")
	private String adjCloseEl;

	public List<IndexQuote> parse(Document document) throws ParserException {
		XMLEventReader reader;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			XMLInputFactory ifactory = XMLInputFactory.newInstance();
			InputStream is = new ByteArrayInputStream(document.getContent());
			StreamSource source = new StreamSource(is);

			reader = ifactory.createXMLEventReader(source);

			List<IndexQuote> list = new ArrayList<IndexQuote>();
			IndexQuote indexQuote = null;
			boolean isValid = true;
			while (reader.hasNext()) {
				try {
					XMLEvent ev = reader.nextEvent();

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(quoteEl)) {
						indexQuote = new IndexQuote();

						Attribute a = ((StartElement) ev).getAttributeByName(new QName(symbolAtt));
						if (a != null && !a.getValue().equals("")) {
							String[] sp = a.getValue().split("\\.");
							indexQuote.setSymbol(sp[0]);
							if (sp.length == 2){
								indexQuote.setExchSymb(sp[1]);
							}else{
								indexQuote.setExchSymb("US");}
						} else
							isValid = false;
					}
					if (indexQuote != null && isValid) {
						if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(dateEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								indexQuote.setQuoteDateTime(new DateTime(evt.asCharacters().getData()));

						}
						if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(openEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								indexQuote.setOpen(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(highEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								indexQuote.setDaysHigh(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(lowEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								indexQuote.setDaysLow(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(closeEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								indexQuote.setClose(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(volumeEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								indexQuote.setVolume(new BigInteger(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(adjCloseEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								indexQuote.setAdjClose(new BigDecimal(evt.asCharacters().getData()));
						}

						else if (isValid && ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(quoteEl)) {

							if ( document.getDataType() != null) {
								indexQuote.setSource(YahooGatewayHelper.Y_PROVIDER_SYMB);
								indexQuote.setDataType(document.getDataType());
								list.add(indexQuote);
								indexQuote = null;

							}
						}

					}
				} catch ( UnsupportedOperationException | IllegalArgumentException e) {
					logger.error("Exception when creating a new object by the parser: " + e);
					isValid = false;
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