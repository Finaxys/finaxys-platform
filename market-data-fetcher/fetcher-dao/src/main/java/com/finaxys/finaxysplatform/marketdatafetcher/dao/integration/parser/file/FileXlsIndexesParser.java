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
import com.finaxys.finaxysplatform.core.domain.Index;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsIndexInfosConverter.
 */
public class FileXlsIndexesParser implements Parser<Index> {

	private static Logger logger = Logger.getLogger(FileXlsIndexesParser.class);

	public List<Index> parse(Document document) throws ParserException {
		HSSFWorkbook workbook;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			InputStream is = new ByteArrayInputStream(document.getContent());

			workbook = new HSSFWorkbook(is);

			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			List<Index> list = new ArrayList<Index>();

			Row row = rowIterator.next();
			Index index;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();

				try {
					index = new Index();
					index.setSymbol(cellIterator.next().toString());
					index.setName(cellIterator.next().toString());
					index.setExchSymb(cellIterator.next().toString());
					index.setProvider(cellIterator.next().toString().charAt(0));
					index.setSource(document.getSource());
					index.setInputDate(new DateTime());
					index.setDataType(document.getDataType());
					list.add(index);

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
