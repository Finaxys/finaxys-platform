/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.Option;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Interface OptionDao.
 */
public interface OptionDao extends BasicDao<Option> {

	 List<Option> list(char provider, String exchSymb) throws DataAccessException;

}
