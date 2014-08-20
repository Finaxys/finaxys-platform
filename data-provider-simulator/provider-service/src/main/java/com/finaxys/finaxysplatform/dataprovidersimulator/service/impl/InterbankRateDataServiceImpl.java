package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.InterbankRateDataDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.InterbankRateDataService;

public class InterbankRateDataServiceImpl implements InterbankRateDataService{


	@Autowired
	private InterbankRateDataDao dao;

	@Override
	public void add( InterbankRateData interbankRateData) {
		 dao.add(interbankRateData);
	}

}
