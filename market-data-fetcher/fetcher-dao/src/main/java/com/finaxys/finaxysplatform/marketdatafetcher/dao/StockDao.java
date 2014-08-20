/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Interface StockDao.
 */
public interface StockDao extends BasicDao<Stock> {
	
	 List<Stock> list(char provider, String exchSymb) throws DataAccessException;
	
	 List<Stock> list(char provider) throws DataAccessException;
}