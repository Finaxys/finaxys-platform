/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.finaxys.finaxysplatform.core.domain.FXRate;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.YahooGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-genequoted Javadoc
/**
 * The Class YahooXmlIndexQuotesConverter.
 */
public class YahooXmlFXRatesParser implements Parser<FXRate> {

	private static Logger logger = Logger.getLogger(YahooXmlFXRatesParser.class);

	/** The date format. */
	@Value("${parser.yahoo.fx_rates.date_format}")
	private String dateFormat;

	@Value("${parser.yahoo.fx_rates.old.no_data}")
	private String noData;

	@Value("${parser.yahoo.fx_rates.old.main_rate_el}")
	private String mainRateEl;

	/** The id att. */
	@Value("${parser.yahoo.fx_rates.old.id_att}")
	private String idAtt;

	@Value("${parser.yahoo.fx_rates.old.rate_el}")
	private String rateEl;

	@Value("${parser.yahoo.fx_rates.old.date_el}")
	private String dateEl;

	@Value("${parser.yahoo.fx_rates.old.time_el}")
	private String timeEl;

	@Value("${parser.yahoo.fx_rates.old.ask_el}")
	private String askEl;

	@Value("${parser.yahoo.fx_rates.old.bid_el}")
	private String bidEl;

	public List<FXRate> parse(Document document) throws ParserException {
		XMLEventReader reader;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			XMLInputFactory ifactory = XMLInputFactory.newInstance();
			InputStream is = new ByteArrayInputStream(document.getContent());
			StreamSource source = new StreamSource(is);

			reader = ifactory.createXMLEventReader(source);

			List<FXRate> list = new ArrayList<FXRate>();
			String date = null;
			String time = null;
			DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);

			FXRate fxRate = null;
			boolean isValid = true;
			while (reader.hasNext()) {
				try {
					XMLEvent ev = reader.nextEvent();

					if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(mainRateEl)) {
						fxRate = new FXRate();
						isValid = true;
						Attribute a = ((StartElement) ev).getAttributeByName(new QName(idAtt));
						if (a != null && !a.getValue().equals("")){
							fxRate.setSymbol(a.getValue());
						}else{
							isValid = false;}
					} else if (fxRate != null && isValid) {
						if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(rateEl)) {

							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData)){
								fxRate.setRate(new BigDecimal(evt.asCharacters().getData()));
							}else{
								isValid = false;}
						} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(askEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								fxRate.setAsk(new BigDecimal(evt.asCharacters().getData()));

						} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(bidEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData))
								fxRate.setBid(new BigDecimal(evt.asCharacters().getData()));

						} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(dateEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData)){
								date = evt.asCharacters().getData();
							}else{
								isValid = false;}
						} else if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(timeEl)) {
							XMLEvent evt = reader.nextEvent();
							if (evt.isCharacters() && !evt.asCharacters().getData().equals(noData)){
								time = evt.asCharacters().getData();
							}else{
								isValid = false;}
						} else if (isValid && ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(mainRateEl)) {

							if (date != null && time != null  && document.getDataType() != null) {
								DateTime ts = formatter.parseDateTime(date + " " + time);
								fxRate.setRateDateTime(ts);
								fxRate.setSource(YahooGatewayHelper.Y_PROVIDER_SYMB);
								fxRate.setDataType(document.getDataType());
								fxRate.setInputDate(new DateTime());
								list.add(fxRate);
								fxRate = null;
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
