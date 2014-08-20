/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.StockQuote;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.StockQuoteDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.StockQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class StockQuoteServiceImpl.
 */
public class StockQuoteServiceImpl implements StockQuoteService {

	@Autowired
	private StockQuoteDao dao;

	@Override
	public void add(StockQuote stockQuote) {
	 dao.add(stockQuote);
	}


}