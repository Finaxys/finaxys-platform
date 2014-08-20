/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.FXRate;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.FXRateDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.FXRateService;

// TODO: Auto-generated Javadoc
/**
 * The Class FXRateServiceImpl.
 */
public class FXRateServiceImpl implements FXRateService {


	@Autowired
	private FXRateDao dao;

	@Override
	public void add(FXRate fxRate) {
		 dao.add(fxRate);
	}


}
