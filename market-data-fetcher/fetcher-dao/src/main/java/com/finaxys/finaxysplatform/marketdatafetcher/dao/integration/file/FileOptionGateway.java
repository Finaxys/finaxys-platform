package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;
import com.finaxys.finaxysplatform.core.domain.Enum.DataClass;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.GatewayException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.FileGatewayHelper;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.RefDataGateway;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

public class FileOptionGateway implements RefDataGateway<Option> {

	/** The logger. */
	private static Logger logger = Logger.getLogger(FileOptionGateway.class);

	/** The stocks file. */
	@Value("${gateway.file.optionsFile:options}")
	public String OPTIONS_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<Option> parser;

	public FileOptionGateway(ContentType contentType, Parser<Option> parser) {
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

	public Parser<Option> getParser() {
		return parser;
	}

	public void setParser(Parser<Option> parser) {
		this.parser = parser;
	}

	@Override
	public List<Option> getRefData() throws GatewayException {
		try {
			File file = FileGatewayHelper.getResourceFile(FileGatewayHelper

			.getPath(FileGatewayHelper.DATA_FOLDER, OPTIONS_FILE, contentType.getName()));
			if (file != null && file.length() > 0)
				return parser.parse(new Document(contentType, DataType.REF, DataClass.Option, FileGatewayHelper.FILE_PROVIDER_SYMB, FileGatewayHelper.toBytes(file)));
			
				return null;
		} catch (IOException | ParserException e) {
			throw new GatewayException(e);
		}
	}

}
