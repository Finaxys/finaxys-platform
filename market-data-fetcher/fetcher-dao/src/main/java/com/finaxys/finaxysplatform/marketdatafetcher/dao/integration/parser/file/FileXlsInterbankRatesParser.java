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

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsStocksConverter.
 */
public class FileXlsInterbankRatesParser implements Parser<InterbankRate> {

	private static Logger logger = Logger.getLogger(FileXlsInterbankRatesParser.class);

	public List<InterbankRate> parse(Document document) throws ParserException {
		HSSFWorkbook workbook;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			InputStream is = new ByteArrayInputStream(document.getContent());

			workbook = new HSSFWorkbook(is);

			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			List<InterbankRate> list = new ArrayList<InterbankRate>();

			Row row = rowIterator.next();
			InterbankRate interbankRate;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();

				try {
					interbankRate = new InterbankRate();
					if (cellIterator.hasNext())
						interbankRate.setSymbol(cellIterator.next().toString());
					if (cellIterator.hasNext())
						interbankRate.setCurrency(cellIterator.next().toString());
					interbankRate.setProvider(cellIterator.next().toString().charAt(0));
					interbankRate.setSource(document.getSource());
					interbankRate.setInputDate(new DateTime());
					interbankRate.setDataType(document.getDataType());
					list.add(interbankRate);
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
