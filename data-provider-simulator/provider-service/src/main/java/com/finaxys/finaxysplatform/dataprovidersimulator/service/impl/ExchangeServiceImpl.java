/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.Exchange;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.ExchangeDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.ExchangeService;

// TODO: Auto-generated Javadoc
/**
 * The Class ExchangeServiceImpl.
 */
public class ExchangeServiceImpl implements ExchangeService {


	@Autowired
	private ExchangeDao dao;

	@Override
	public void add(Exchange exchange) {
		 dao.add(exchange);
	}




	

}