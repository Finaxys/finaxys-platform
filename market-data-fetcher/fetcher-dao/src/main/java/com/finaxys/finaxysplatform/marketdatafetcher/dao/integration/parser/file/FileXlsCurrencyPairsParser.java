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
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class FileXlsCurrencyPairsParser implements Parser<CurrencyPair> {

	private static Logger logger = Logger.getLogger(FileXlsCurrencyPairsParser.class);

	public List<CurrencyPair> parse(Document document) throws ParserException {
		HSSFWorkbook workbook;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			InputStream is = new ByteArrayInputStream(document.getContent());

			workbook = new HSSFWorkbook(is);

			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			List<CurrencyPair> list = new ArrayList<CurrencyPair>();

			Row row = rowIterator.next();
			CurrencyPair currencyPair;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				try {
					currencyPair = new CurrencyPair();
					String sym = cellIterator.next().toString();
					String[] symbol = (sym.split(" ")[0]).split("/");
					if (symbol.length == 2) {
						currencyPair.setSymbol(symbol[0] + symbol[1]);
						currencyPair.setBaseCurrency(symbol[0]);
						currencyPair.setQuoteCurrency(symbol[1]);
						currencyPair.setProvider(cellIterator.next().toString().charAt(0));
						currencyPair.setSource(document.getSource());
						currencyPair.setInputDate(new DateTime());
						currencyPair.setDataType(document.getDataType());
						list.add(currencyPair);
					} else
						throw new IllegalArgumentException();
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
