package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser;

/*
 * 
 */

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;

// TODO: Auto-generated Javadoc
/**
 * The Interface Parser.
 */
public interface Parser<T> {

	/**
	 * Convert.
	 *
	 * @param document
	 *            the document
	 * @throws Exception
	 *             the exception
	 */
	 List<T> parse(Document document) throws ParserException;
}
