package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;
import com.finaxys.finaxysplatform.core.domain.Enum.DataClass;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.FileGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.HistDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class FileInterbankRateDataGateway implements HistDataGateway<InterbankRateData, InterbankRate> {

	/** The logger. */
	private static Logger logger = Logger.getLogger(FileInterbankRateDataGateway.class);

	/** The exchanges file. */
	@Value("${gateway.file.histInterbankRateDataFile:hist_interbank_rate_data_}")
	public String RATES_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<InterbankRateData> histDataParser;

	public FileInterbankRateDataGateway(ContentType contentType, Parser<InterbankRateData> histDataParser) {
		super();
		this.contentType = contentType;
		this.histDataParser = histDataParser;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Parser<InterbankRateData> getHistDataParser() {
		return histDataParser;
	}

	public void setHistDataParser(Parser<InterbankRateData> histDataParser) {
		this.histDataParser = histDataParser;
	}

	public byte[] filterHistRatesData(File file, List<InterbankRate> interbankRates, LocalDate startDate, LocalDate endDate) throws IOException {

		List<String> rates = new ArrayList<String>();
		if (interbankRates != null && interbankRates.size() > 0)
			for (InterbankRate rate : interbankRates) {
				rates.add(rate.getSymbol() + "|" + rate.getCurrency());
			}

		InputStream is = new FileInputStream(file);
		HSSFWorkbook workbook = new HSSFWorkbook(is);
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
				rate.setBucket(cellIterator.next().toString());
				rate.setRateDateTime(formatter.parseDateTime(cellIterator.next().toString()));
				rate.setValue(new BigDecimal(cellIterator.next().toString()));

				if (rates.contains(rate.getSymbol() + "|" + rate.getCurrency()) && rate.getRateDateTime().toLocalDate().isAfter(startDate)
						&& rate.getRateDateTime().toLocalDate().isBefore(endDate))
					list.add(rate);

			} catch ( NoSuchElementException | IllegalArgumentException e) {
				logger.error("Exception when creating a new object by the parser: " + e);
			}
		}
		return writeRatesToByteArray(list);
	}

	private byte[] writeRatesToByteArray(List<InterbankRateData> rates) throws IOException {

		if (rates != null && rates.size() > 0) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			int i = 0;

			for (InterbankRateData rate : rates) {
				Row row = sheet.createRow(i++);
				row.createCell(0).setCellValue(rate.getSymbol());
				row.createCell(0).setCellValue(rate.getCurrency());
				row.createCell(0).setCellValue(rate.getBucket());
				row.createCell(0).setCellValue(formatter.print(rate.getRateDateTime()));
				row.createCell(0).setCellValue(rate.getValue() + "");
			}
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			workbook.write(os);

			os.flush();
			return os.toByteArray();
		}
		return null;
	}

	@Override
	public List<InterbankRateData> getHistData(List<InterbankRate> products, LocalDate startDate, LocalDate endDate) throws GatewayException {
		try {
			Assert.notNull(products, "Cannot execute data extraction. Products list is null.");
			Assert.notEmpty(products, "Cannot execute data extraction. Products list is empty.");
			Assert.notNull(startDate, "Cannot execute data extraction. Start date is null.");
			Assert.notNull(endDate, "Cannot execute data extraction. End date is null.");
			
			File file = FileGatewayHelper.getResourceFile(FileGatewayHelper
			.getPath(FileGatewayHelper.DATA_FOLDER, RATES_FILE, contentType.getName()));
			byte[] data = filterHistRatesData(file, products, startDate, endDate);
			if (data != null && data.length > 0)
				return histDataParser.parse(new Document(contentType, DataType.HIST, DataClass.InterbankRatesData, FileGatewayHelper.FILE_PROVIDER_SYMB, data));
			
				return null;
		} catch (  IOException | ParserException e) {
			throw new GatewayException(e);
		}
	}
}
