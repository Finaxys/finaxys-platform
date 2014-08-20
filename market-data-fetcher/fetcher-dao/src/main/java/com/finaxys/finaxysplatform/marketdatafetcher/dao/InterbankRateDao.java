package com.finaxys.finaxysplatform.marketdatafetcher.dao;

import java.util.List;

import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataAccessException;


public interface InterbankRateDao extends BasicDao<InterbankRate>{
	
	 List<InterbankRate> list(char provider) throws DataAccessException;
}
