/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.StockDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.StockService;

// TODO: Auto-generated Javadoc
/**
 * The Class StockServiceImpl.
 */
public class StockServiceImpl implements StockService {

	@Autowired
	private StockDao dao;

	@Override
	public void add(Stock stock) {
		 dao.add(stock);
	}


}