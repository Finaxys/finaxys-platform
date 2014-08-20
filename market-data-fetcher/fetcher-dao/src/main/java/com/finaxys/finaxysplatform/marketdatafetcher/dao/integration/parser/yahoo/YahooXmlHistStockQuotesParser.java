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
import com.finaxys.finaxysplatform.core.domain.StockQuote;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlHistStockQuotesParser implements Parser<StockQuote> {

	private static Logger logger = Logger.getLogger(YahooXmlHistStockQuotesParser.class);

	@Value("${parser.yahoo.hist_stock_quotes.date_format}")
	private String DATE_FORMAT;

	@Value("${parser.yahoo.hist_stock_quotes.old.no_data}")
	private String NO_DATA;

	@Value("${parser.yahoo.hist_stock_quotes.old.quote_el}")
	private String QUOTE_EL;

	@Value("${parser.yahoo.hist_stock_quotes.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${parser.yahoo.hist_stock_quotes.old.DATE_EL}")
	private String DATE_EL;

	@Value("${parser.yahoo.hist_stock_quotes.old.open_el}")
	private String OPEN_EL;

	@Value("${parser.yahoo.hist_stock_quotes.old.high_el}")
	private String HIGH_EL;

	@Value("${parser.yahoo.hist_stock_quotes.old.low_el}")
	private String LOW_EL;

	@Value("${parser.yahoo.hist_stock_quotes.old.close_el}")
	private String CLOSE_EL;

	@Value("${parser.yahoo.hist_stock_quotes.old.volume_el}")
	private String VOLUME_EL;

	@Value("${parser.yahoo.hist_stock_quotes.old.adj_close_el}")
	private String ADJ_CLOSE_EL;

	public List<StockQuote> parse(Document document) throws ParserException {
		XMLEventReader reader;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			XMLInputFactory ifactory = XMLInputFactory.newInstance();

			InputStream is = new ByteArrayInputStream(document.getContent());
			StreamSource source = new StreamSource(is);

			reader = ifactory.createXMLEventReader(source);

			List<StockQuote> list = new ArrayList<StockQuote>();
			StockQuote stockQuote = null;
			boolean isValid = true;
			while (reader.hasNext()) {
				try {
					XMLEvent ev = reader.nextEvent();

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {
						stockQuote = new StockQuote();

						Attribute a = ((StartElement) ev).getAttributeByName(new QName(SYMBOL_ATT));
						if (a != null && !a.getValue().equals("")) {
							String[] sp = a.getValue().split("\\.");
							stockQuote.setSymbol(sp[0]);
							if (sp.length == 2){
								stockQuote.setExchSymb(sp[1]);
							}else{
								stockQuote.setExchSymb("US");}
						} else
							isValid = false;
					}
					if (stockQuote != null && isValid) {
						if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DATE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setQuoteDateTime(new DateTime(evt.asCharacters().getData()));

						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPEN_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setOpen(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(HIGH_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setDaysHigh(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LOW_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setDaysLow(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CLOSE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setClose(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(VOLUME_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setVolume(new BigInteger(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(ADJ_CLOSE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setAdjClose(new BigDecimal(evt.asCharacters().getData()));
						}

						else if (isValid && ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {

							if ( document.getDataType() != null) {
								stockQuote.setSource(YahooGatewayHelper.Y_PROVIDER_SYMB);
								stockQuote.setDataType(document.getDataType());
								list.add(stockQuote);
								stockQuote = null;

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
