package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;
import com.finaxys.finaxysplatform.core.domain.Enum.DataClass;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.FileGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.RefDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class FileInterbankRateGateway implements RefDataGateway<InterbankRate> {

	/** The logger. */
	private static Logger logger = Logger.getLogger(FileInterbankRateGateway.class);

	/** The stocks file. */
	@Value("${gateway.file.interbankRatesFile:interbank_rates}")
	public String RATES_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<InterbankRate> parser;

	public FileInterbankRateGateway(ContentType contentType, Parser<InterbankRate> parser) {
		super();
		this.contentType = contentType;
		this.parser = parser;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Parser<InterbankRate> getParser() {
		return parser;
	}

	public void setParser(Parser<InterbankRate> parser) {
		this.parser = parser;
	}

	@Override
	public List<InterbankRate> getRefData() throws GatewayException {

		try {
			File file = FileGatewayHelper.getResourceFile(FileGatewayHelper.getPath(FileGatewayHelper.DATA_FOLDER, RATES_FILE, contentType.getName()));
			if (file != null && file.length() > 0)
				return parser.parse(new Document(contentType, DataType.REF, DataClass.InterbankRate, FileGatewayHelper.FILE_PROVIDER_SYMB, FileGatewayHelper.toBytes(file)));
			
				return null;
		} catch (  IOException | ParserException e) {
			throw new GatewayException(e);
		}
	}

}
