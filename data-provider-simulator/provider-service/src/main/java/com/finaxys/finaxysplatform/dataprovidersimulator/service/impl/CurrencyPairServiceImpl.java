/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.CurrencyPairDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.CurrencyPairService;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairServiceImpl.
 */
public class CurrencyPairServiceImpl implements CurrencyPairService {


	@Autowired
	private CurrencyPairDao dao;

	@Override
	public void add(CurrencyPair currencyPair) {
		 dao.add(currencyPair);
	}

}
