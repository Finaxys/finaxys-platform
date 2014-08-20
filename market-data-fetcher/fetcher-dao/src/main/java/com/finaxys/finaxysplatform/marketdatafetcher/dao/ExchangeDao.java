/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.Exchange;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Interface ExchangeDao.
 */
public interface ExchangeDao extends BasicDao<Exchange>{
	
	 List<Exchange> list(char provider) throws DataAccessException;
}
