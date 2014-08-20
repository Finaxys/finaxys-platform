/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.OptionQuote;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.OptionQuoteDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.OptionQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionQuoteServiceImpl.
 */
public class OptionQuoteServiceImpl implements OptionQuoteService {

	@Autowired
	private OptionQuoteDao dao;

	@Override
	public void add(OptionQuote optionQuote) {
		 dao.add(optionQuote);
	}

	
}