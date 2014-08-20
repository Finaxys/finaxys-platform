/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Interface OptionChainDao.
 */
public interface OptionChainDao extends BasicDao<OptionChain>{
	
	 List<OptionChain> list(char provider) throws DataAccessException;
}
