/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.dao;

import java.util.List;

import com.finaxys.finaxysplatform.dataprovidersimulator.dao.exception.DataAccessException;


// TODO: Auto-generated Javadoc
/**
 * The Interface CurrencyPairDao.
 */
public interface BasicDao<T> {

	 void add(T bean) throws DataAccessException;

	 List<T> list(byte[] prefix) throws DataAccessException;

	 List<T> listAll() throws DataAccessException;

}
