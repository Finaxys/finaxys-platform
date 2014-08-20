/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Interface CurrencyPairDao.
 */
public interface CurrencyPairDao extends BasicDao<CurrencyPair> {

	 List<CurrencyPair> list(char provider) throws DataAccessException;
}
