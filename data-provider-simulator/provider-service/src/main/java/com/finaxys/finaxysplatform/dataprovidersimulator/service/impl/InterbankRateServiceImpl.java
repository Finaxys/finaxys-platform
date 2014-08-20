package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.InterbankRateDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.InterbankRateService;

public class InterbankRateServiceImpl implements InterbankRateService {


	@Autowired
	private InterbankRateDao dao;

	@Override
	public void add(InterbankRate interbankRate) {
		
		 dao.add(interbankRate);
	}

}
