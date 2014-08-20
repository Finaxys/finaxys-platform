/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.IndexQuote;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.IndexQuoteDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.IndexQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuoteServiceImpl.
 */
public class IndexQuoteServiceImpl implements IndexQuoteService {


	@Autowired
	private IndexQuoteDao dao;

	@Override
	public void add(IndexQuote index) {
		 dao.add(index);
	}

}
