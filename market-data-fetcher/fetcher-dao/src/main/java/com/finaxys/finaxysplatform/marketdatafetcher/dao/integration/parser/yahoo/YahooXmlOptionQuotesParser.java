/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlOptionQuotesParser implements Parser<OptionQuote> {

	private static Logger logger = Logger.getLogger(YahooXmlOptionQuotesParser.class);

	/** The date format. */
	@Value("${parser.yahoo.option_quotes.date_format}")
	private String DATE_FORMAT;

	@Value("${parser.yahoo.option_quotes.expiration_date_format}")
	private String EXPIRATION_DATE_FORMAT;

	@Value("${parser.yahoo.option_quotes.old.no_data}")
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

	@Value("${parser.yahoo.option_quotes.old.option_el}")
	private String OPTION_EL;

	@Value("${parser.yahoo.option_quotes.old.options_chain_el}")
	private String OPTIONS_CHAIN_EL;

	@Value("${parser.yahoo.option_quotes.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${parser.yahoo.option_quotes.old.type_att}")
	private String OPTION_TYPE_ATT;

	@Value("${parser.yahoo.option_quotes.old.change_el}")
	private String CHANGE_EL;

	@Value("${parser.yahoo.option_quotes.old.lastprice_el}")
	private String LAST_PRICE_EL;

	@Value("${parser.yahoo.option_quotes.old.open_el}")
	private String OPEN_EL;

	@Value("${parser.yahoo.option_quotes.old.bid_el}")
	private String BID_EL;

	@Value("${parser.yahoo.option_quotes.old.ask_el}")
	private String ASK_EL;

	@Value("${parser.yahoo.option_quotes.old.strike_el}")
	private String STRIKE_EL;

	@Value("${parser.yahoo.option_quotes.old.volume_el}")
	private String VOLUME_EL;

	@Value("${parser.yahoo.option_quotes.old.openinterest_el}")
	private String OPENINTEREST_EL;

	@Value("${parser.yahoo.option_quotes.old.expiration_att}")
	private String EXPIRATION_ATT;

	@Value("${parser.yahoo.option_quotes.old.och_symbol_att}")
	private String OCH_SYMBOL_ATT;

	public List<OptionQuote> parse(Document document) throws ParserException {
		try {
			Assert.notNull(document, "Cannot parse Null document");
			XMLInputFactory ifactory = XMLInputFactory.newInstance();
			InputStream is = new ByteArrayInputStream(document.getContent());
			StreamSource source = new StreamSource(is);
			XMLEventReader reader;

			reader = ifactory.createXMLEventReader(source);

			List<OptionQuote> list = new ArrayList<OptionQuote>();
			DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
			DateTimeFormatter expirationFormatter = DateTimeFormat.forPattern(EXPIRATION_DATE_FORMAT).withLocale(Locale.ENGLISH);

			String ts = "";
			String optionsChain = "";
			String expiration = "";
			OptionQuote optionQuote = null;
			boolean isValid = true;
			while (reader.hasNext()) {
				try {
					XMLEvent ev = reader.nextEvent();

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equalsIgnoreCase(HEADER_EL)) {
						Attribute a = ((StartElement) ev).getAttributeByName(new QName(((StartElement) ev).getNamespaceURI(HEADER_NS), HEADER_CREATED));
						if (a != null && !a.getValue().isEmpty())
							ts = a.getValue();
					}

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPTIONS_CHAIN_EL)) {
						Attribute a = ((StartElement) ev).getAttributeByName(new QName(OCH_SYMBOL_ATT));
						if (a != null && !a.getValue().isEmpty())
							optionsChain = a.getValue();

						Attribute b = ((StartElement) ev).getAttributeByName(new QName(EXPIRATION_ATT));
						if (b != null && !b.getValue().isEmpty())
							expiration = b.getValue();
					}

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPTION_EL)) {
						optionQuote = new OptionQuote();
						if (ts == null || ts.isEmpty()){
							isValid = false;
						}else{
							isValid = true;}

						Attribute a = ((StartElement) ev).getAttributeByName(new QName(SYMBOL_ATT));
						if (a != null && !a.getValue().isEmpty()){
							optionQuote.setSymbol(a.getValue());
						}else{
							isValid = false;}

						a = ((StartElement) ev).getAttributeByName(new QName(OPTION_TYPE_ATT));
						if (a != null && !a.getValue().isEmpty()){
							optionQuote.setOptionType(a.getValue());
						}else{
							isValid = false;}

						if (optionsChain != null && !optionsChain.isEmpty()){
							optionQuote.setOptionChain(optionsChain);
						}else{
							isValid = false;}

						if (expiration != null && !expiration.isEmpty()){
							optionQuote.setExpiration(expirationFormatter.parseDateTime(expiration).toLocalDate());
						}else{
							isValid = false;}

					}
					if (optionQuote != null && isValid) {
						if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CHANGE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								optionQuote.setChange(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(LAST_PRICE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA)) {
								optionQuote.setPrice(new BigDecimal(evt.asCharacters().getData()));
								optionQuote.setPrevClose(new BigDecimal(evt.asCharacters().getData()));
								optionQuote.setOpen(new BigDecimal(evt.asCharacters().getData()));
							}
						}

						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(BID_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								optionQuote.setBid(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(ASK_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								optionQuote.setAsk(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(STRIKE_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA))
								optionQuote.setStrike(new BigDecimal(evt.asCharacters().getData()));
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(VOLUME_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA)) {
								NumberFormat nf_us = NumberFormat.getInstance(Locale.US);
								optionQuote.setVolume(new BigInteger(nf_us.parse(evt.asCharacters().getData()).toString()));
							}
						}
						else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(OPENINTEREST_EL)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA)) {

								NumberFormat nf_us = NumberFormat.getInstance(Locale.US);
								optionQuote.setOpenInterest(nf_us.parse(evt.asCharacters().getData()).intValue());

							}
						}

						else if (isValid && ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(OPTION_EL)) {

							if (ts != null && !ts.isEmpty()  && document.getDataType() != null) {
								optionQuote.setQuoteDateTime(new DateTime(ts));
								optionQuote.setSource(YahooGatewayHelper.Y_PROVIDER_SYMB);
								optionQuote.setDataType(document.getDataType());
								optionQuote.setInputDate(new DateTime());
								list.add(optionQuote);
								optionQuote = null;
							}
						}
					}
				} catch ( ParseException | UnsupportedOperationException | IllegalArgumentException e) {
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
