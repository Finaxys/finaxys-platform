/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Exchange;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsExchangesConverter.
 */
public class FileXlsExchangesParser implements Parser<Exchange> {

	private static Logger logger = Logger.getLogger(FileXlsExchangesParser.class);

	public List<Exchange> parse(Document document) throws ParserException {
		HSSFWorkbook workbook;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			InputStream is = new ByteArrayInputStream(document.getContent());

			workbook = new HSSFWorkbook(is);

			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			List<Exchange> list = new ArrayList<Exchange>();
			DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");

			Row row = rowIterator.next();
			Exchange exchange;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();

				try {
					exchange = new Exchange();
					exchange.setSymbol(cellIterator.next().toString());
					exchange.setSourceSymbol(cellIterator.next().toString());
					exchange.setProvider(cellIterator.next().toString().charAt(0));
					exchange.setSource(document.getSource());
					exchange.setName(cellIterator.next().toString());
					exchange.setType(cellIterator.next().toString());
					exchange.setContinent(cellIterator.next().toString());
					exchange.setCountry(cellIterator.next().toString());
					exchange.setCurrency(cellIterator.next().toString());
					exchange.setOpenTime(formatter.parseDateTime(cellIterator.next().toString()).toLocalTime());
					exchange.setCloseTime(formatter.parseDateTime(cellIterator.next().toString()).toLocalTime());
					Cell delayCell = cellIterator.next();
					delayCell.setCellType(Cell.CELL_TYPE_STRING);
					exchange.setDelay(new Integer(delayCell.toString()));
					exchange.setInputDate(new DateTime());
					exchange.setDataType(document.getDataType());
					list.add(exchange);
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
