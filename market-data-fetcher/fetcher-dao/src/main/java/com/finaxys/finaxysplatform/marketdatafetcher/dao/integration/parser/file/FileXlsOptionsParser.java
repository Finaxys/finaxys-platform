/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsOptionsConverter.
 */
public class FileXlsOptionsParser implements Parser<Option> {

	private static Logger logger = Logger.getLogger(FileXlsOptionsParser.class);

	/** The expiration date format. */
	@Value("${parser.file.options.input_date_format}")
	private String INPUT_DATE_FORMAT;

	@Value("${parser.file.options.expiration_date_format}")
	private String EXPIRATION_DATE_FORMAT;

	public List<Option> parse(Document document) throws ParserException {
		HSSFWorkbook workbook;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			InputStream is = new ByteArrayInputStream(document.getContent());

			workbook = new HSSFWorkbook(is);

			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			List<Option> list = new ArrayList<Option>();

			DateTimeFormatter expirationFormatter = DateTimeFormat.forPattern(EXPIRATION_DATE_FORMAT).withLocale(Locale.ENGLISH);
			DateTimeFormatter inputFormatter = DateTimeFormat.forPattern(INPUT_DATE_FORMAT).withLocale(Locale.ENGLISH);

			Row row = rowIterator.next();
			Option option;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				try {
					option = new Option();

					option.setSymbol(cellIterator.next().toString());
					option.setOptionChain(cellIterator.next().toString());
					option.setExpiration(expirationFormatter.parseDateTime(cellIterator.next().toString()).toLocalDate());
					option.setStrike(new BigDecimal(cellIterator.next().toString()));
					option.setOptionType("" + cellIterator.next().toString().charAt(0));
					option.setExchSymb(cellIterator.next().toString());
					option.setProvider(cellIterator.next().toString().charAt(0));
					option.setSource(document.getSource());
					option.setInputDate(new DateTime());
					option.setDataType(document.getDataType());
					list.add(option);
				} catch ( NoSuchElementException | IllegalArgumentException e) {
					logger.error("Exception when creating a new object by the parser: " + e);
				}
			}

			return list;
		} catch ( IOException e) {
			throw new DataReadingParserException(e);
		}
	}

}