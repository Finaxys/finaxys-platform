package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.ebf;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.core.domain.Enum.Bucket;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataReadingParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class EbfXlsHistInterbankRateDataParser implements Parser<InterbankRateData> {

	private static Logger logger = Logger.getLogger(EbfXlsHistInterbankRateDataParser.class);

	@Value("${parser.ebf.hist_interbank_rate_data.euribor_symb}")
	public String EURIBOR_SYMB;

	@Value("${parser.ebf.hist_interbank_rate_data.euro_symb}")
	public String EURO_SYMB;

	public List<InterbankRateData> parse(Document document) throws ParserException {
		HSSFWorkbook workbook;
		try {
			Assert.notNull(document, "Cannot parse Null document");
			InputStream is = new ByteArrayInputStream(document.getContent());

			workbook = new HSSFWorkbook(is);

			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			List<DateTime> dates = new ArrayList<DateTime>();
			List<InterbankRateData> list = new ArrayList<InterbankRateData>();

			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			cellIterator.next();
			String date = "";
			while (cellIterator.hasNext() && !(date = cellIterator.next().toString()).equals("")){
				dates.add(formatter.parseDateTime(date));
			}

			String bucket = "";
			while (rowIterator.hasNext() && !(bucket = (cellIterator = (row = rowIterator.next()).cellIterator()).next().toString()).equals("")) {

				Iterator<DateTime> datesIterator = dates.iterator();
				String value = "";
				InterbankRateData rate;
				while (cellIterator.hasNext() && !(value = cellIterator.next().toString()).equals("")) {
					try {
						rate = new InterbankRateData();
						rate.setSymbol(EURIBOR_SYMB);
						rate.setCurrency(EURO_SYMB);
						rate.setBucket(getBucket(bucket));
						rate.setDataType(document.getDataType());
						rate.setSource(document.getSource());
						rate.setRateDateTime(datesIterator.next());
						rate.setValue(new BigDecimal(value));
						rate.setInputDate(new DateTime());
						list.add(rate);
					} catch ( NoSuchElementException | IllegalArgumentException e) {
						logger.error("Exception when creating a new object by the parser: " + e);
					}
				}

			}
			return list;
		} catch ( IOException e) {
			throw new DataReadingParserException(e);
		}

	}

	private String getBucket(String bucket) {

		if (bucket.equals("1w"))
			return Bucket._1W.getName();
		if (bucket.equals("2w"))
			return Bucket._2W.getName();
		if (bucket.equals("3w"))
			return Bucket._3W.getName();
		if (bucket.equals("1m"))
			return Bucket._1M.getName();
		if (bucket.equals("2m"))
			return Bucket._2M.getName();
		if (bucket.equals("3m"))
			return Bucket._3M.getName();
		if (bucket.equals("4m"))
			return Bucket._4M.getName();
		if (bucket.equals("5m"))
			return Bucket._5M.getName();
		if (bucket.equals("5m"))
			return Bucket._5M.getName();
		if (bucket.equals("6m"))
			return Bucket._6M.getName();
		if (bucket.equals("7m"))
			return Bucket._7M.getName();
		if (bucket.equals("8m"))
			return Bucket._8M.getName();
		if (bucket.equals("9m"))
			return Bucket._9M.getName();
		if (bucket.equals("10m"))
			return Bucket._10M.getName();
		if (bucket.equals("11m"))
			return Bucket._11M.getName();
		if (bucket.equals("12m"))
			return Bucket._1Y.getName();

		else
			return Bucket.valueOf(bucket).getName();

	}
}
