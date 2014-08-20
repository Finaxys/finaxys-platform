/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.core.domain.Index;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.IndexDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.IndexService;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexInfoServiceImpl.
 */
public class IndexServiceImpl implements IndexService{


	@Autowired
	private IndexDao dao;

	@Override
	public void add(Index index) {
		 dao.add(index);
	}


}