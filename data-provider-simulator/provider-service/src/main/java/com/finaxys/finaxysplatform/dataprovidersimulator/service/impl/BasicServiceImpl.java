/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.finaxys.finaxysplatform.dataprovidersimulator.dao.BasicDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.exception.DataAccessException;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.BasicService;
import com.finaxys.finaxysplatform.dataprovidersimulator.service.exception.ServiceException;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairServiceImpl.
 */
public class BasicServiceImpl<T> implements BasicService<T> {

	@Autowired
	private BasicDao<T> dao;

	@Override
	public void add(T t) throws ServiceException {
		try {
			dao.add(t);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

}
