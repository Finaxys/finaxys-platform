package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.OptionChainDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.OptionChainService;

public class OptionChainServiceImpl implements OptionChainService{

	


	@Autowired
	private OptionChainDao dao;

	@Override
	public void add(OptionChain optionChain) {
		 dao.add(optionChain);
	}

	
}