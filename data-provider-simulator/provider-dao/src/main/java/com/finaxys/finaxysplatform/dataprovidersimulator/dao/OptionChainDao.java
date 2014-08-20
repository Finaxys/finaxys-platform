/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Interface OptionChainDao.
 */
public interface OptionChainDao extends BasicDao<OptionChain>{
	 List<OptionChain> list(char provider) throws DataAccessException;
}
