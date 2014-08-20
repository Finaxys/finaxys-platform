package com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.InterbankRateData;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.InterbankRateDataDao;

public class InterbankRateDataDaoImpl extends AbstractBasicDao<InterbankRateData> implements InterbankRateDataDao {

	private static Logger logger = Logger.getLogger(InterbankRateDataDaoImpl.class);

	public InterbankRateDataDaoImpl() {
		super();
	}

	public InterbankRateDataDaoImpl(HConnection connection) {
		super(connection);
	}

}
