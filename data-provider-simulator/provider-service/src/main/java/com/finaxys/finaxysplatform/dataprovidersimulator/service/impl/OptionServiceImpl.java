/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.OptionDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.OptionService;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionServiceImpl.
 */
public class OptionServiceImpl implements OptionService {

	@Autowired
	private OptionDao dao;

	@Override
	public void add(Option option) {
		 dao.add(option);
	}

}