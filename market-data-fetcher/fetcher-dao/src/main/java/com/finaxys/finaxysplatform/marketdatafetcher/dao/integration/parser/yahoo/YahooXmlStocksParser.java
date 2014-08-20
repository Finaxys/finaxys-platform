/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.yahoo;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.finaxysplatform.core.domain.Document;
import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.ParserException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.integration.parser.Parser;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooXmlStocksConverter.
 */
public class YahooXmlStocksParser implements Parser<Stock> {

	private static Logger logger = Logger
			.getLogger(YahooXmlStocksParser.class);
	
	@Value("${parser.yahoo.stocks.new.stock_el}")
	private String ITEM_EL;
	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.parser.Parser#parse(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public List<Stock> parse(Document document) throws ParserException {

		return null;
	}
}
