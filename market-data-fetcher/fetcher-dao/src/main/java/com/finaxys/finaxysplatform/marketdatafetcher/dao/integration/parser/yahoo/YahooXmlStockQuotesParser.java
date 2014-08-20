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
public class YahooXmlStockQuotesParser implements Parser<StockQuote> {

	private static Logger logger = Logger.getLogger(YahooXmlStockQuotesParser.class);

	/** The date format. */
	@Value("${parser.yahoo.stock_quotes.date_format}")
	private String DATE_FORMAT;

	@Value("${parser.yahoo.stock_quotes.old.no_data}")
	private String NO_DATA;

	/** The date format. */
	@Value("${parser.yahoo.header_el}")
	private String HEADER_EL;

	/** The date format. */
	@Value("${parser.yahoo.header_ns}")
	private String HEADER_NS;

	/** The date format. */
	@Value("${parser.yahoo.header_created}")
	private String HEADER_CREATED;

	@Value("${parser.yahoo.stock_quotes.old.quote_el}")
	private String QUOTE_EL;

	@Value("${parser.yahoo.stock_quotes.old.averagedailyvolume_el}")
	private String AVERAGEDAILYVOLUME_EL;

	@Value("${parser.yahoo.stock_quotes.old.change_el}")
	private String CHANGE_EL;

	@Value("${parser.yahoo.stock_quotes.old.dayslow_el}")
	private String DAYSLOW_EL;

	@Value("${parser.yahoo.stock_quotes.old.dayshigh_el}")
	private String DAYSHIGH_EL;

	@Value("${parser.yahoo.stock_quotes.old.yearlow_el}")
	private String YEARLOW_EL;

	@Value("${parser.yahoo.stock_quotes.old.yearhigh_el}")
	private String YEARHIGH_EL;

	@Value("${parser.yahoo.stock_quotes.old.marketcapitalization_el}")
	private String MARKETCAPITALIZATION_EL;

	@Value("${parser.yahoo.stock_quotes.old.lasttradepriceonly_el}")
	private String LASTTRADEPRICEONLY_EL;

	@Value("${parser.yahoo.stock_quotes.old.daysrange_el}")
	private String DAYSRANGE_EL;

	@Value("${parser.yahoo.stock_quotes.old.name_el}")
	private String NAME_EL;

	@Value("${parser.yahoo.stock_quotes.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${parser.yahoo.stock_quotes.old.volume_el}")
	private String VOLUME_EL;

	public List<StockQuote> parse(Document document) throws ParserException {
		XMLEventReader reader;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			XMLInputFactory ifactory = XMLInputFactory.newInstance();
			InputStream is = new ByteArrayInputStream(document.getContent());
			StreamSource source = new StreamSource(is);

			reader = ifactory.createXMLEventReader(source);

			List<StockQuote> list = new ArrayList<StockQuote>();
			String ts = "";
			StockQuote stockQuote = null;
			boolean isValid = true;
			while (reader.hasNext()) {
				try {
					XMLEvent ev = reader.nextEvent();

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equalsIgnoreCase(HEADER_EL)) {
						Attribute a = ((StartElement) ev).getAttributeByName(new QName(((StartElement) ev).getNamespaceURI(HEADER_NS), HEADER_CREATED));
						if (a != null && !a.getValue().isEmpty())
							ts = a.getValue();
					}

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {
						stockQuote = new StockQuote();

						if (ts == null || ts.isEmpty()){
							isValid = false;
						}else{
							isValid = true;}

						Attribute a = ((StartElement) ev).getAttributeByName(new QName(SYMBOL_ATT));
						if (a != null && !a.getValue().equals("")) {
							String[] sp = a.getValue().split("\\.");
							stockQuote.setSymbol(sp[0]);
							if (sp.length == 2){
								stockQuote.setExchSymb(sp[1]);
							}else{
								stockQuote.setExchSymb("US");}
						} else{
							isValid = false;}
					}
					if (stockQuote != null && isValid) {
						if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(AVERAGEDAILYVOLUME_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setAverageDailyVolume(new BigInteger(evt.asCharacters().getData()));

						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CHANGE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setChange(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSLOW_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setDaysLow(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSHIGH_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setDaysHigh(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(YEARLOW_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setYearLow(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(YEARHIGH_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setYearHigh(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(MARKETCAPITALIZATION_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setMarketCapitalization(evt.asCharacters().getData());
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LASTTRADEPRICEONLY_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setLastTradePriceOnly(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(DAYSRANGE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setDaysRange(evt.asCharacters().getData());
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(NAME_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setName(evt.asCharacters().getData());
						}

						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(VOLUME_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								stockQuote.setVolume(new BigInteger(evt.asCharacters().getData()));
						}
						else if (isValid && ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(QUOTE_EL)) {

							if (ts != null && !ts.isEmpty() && document.getDataType() != null) {
								stockQuote.setQuoteDateTime(new DateTime(ts));
								stockQuote.setSource(YahooGatewayHelper.Y_PROVIDER_SYMB);
								stockQuote.setDataType(document.getDataType());
								stockQuote.setInputDate(new DateTime());
								list.add(stockQuote);
								stockQuote = null;

							}
						}

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
