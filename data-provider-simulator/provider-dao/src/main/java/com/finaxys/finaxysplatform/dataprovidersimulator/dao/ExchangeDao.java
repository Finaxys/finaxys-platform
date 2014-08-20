/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.Exchange;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Interface ExchangeDao.
 */
public interface ExchangeDao extends BasicDao<Exchange>{
	
	 List<Exchange> list(char provider) throws DataAccessException;
}
