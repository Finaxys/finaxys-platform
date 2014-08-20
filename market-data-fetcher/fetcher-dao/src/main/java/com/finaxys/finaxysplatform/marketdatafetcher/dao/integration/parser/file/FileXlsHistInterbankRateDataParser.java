package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.core.domain.Enum.Bucket;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class FileXlsHistInterbankRateDataParser implements Parser<InterbankRateData> {

	private static Logger logger = Logger.getLogger(FileXlsHistInterbankRateDataParser.class);

	public List<InterbankRateData> parse(Document document) throws ParserException {
		HSSFWorkbook workbook;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			InputStream is = new ByteArrayInputStream(document.getContent());

			workbook = new HSSFWorkbook(is);

			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			List<InterbankRateData> list = new ArrayList<InterbankRateData>();

			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

			Row row = rowIterator.next();

			InterbankRateData rate;
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				Iterator<Cell> cellIterator = row.cellIterator();
				try {
					rate = new InterbankRateData();
					rate.setSymbol(cellIterator.next().toString());
					rate.setCurrency(cellIterator.next().toString());
					rate.setDataType(document.getDataType());
					rate.setSource(document.getSource());
					rate.setBucket(Bucket.valueOf("_" + cellIterator.next().toString()).getName());
					rate.setRateDateTime(formatter.parseDateTime(cellIterator.next().toString()));
					rate.setValue(new BigDecimal(cellIterator.next().toString()));
					rate.setInputDate(new DateTime());
					list.add(rate);

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
