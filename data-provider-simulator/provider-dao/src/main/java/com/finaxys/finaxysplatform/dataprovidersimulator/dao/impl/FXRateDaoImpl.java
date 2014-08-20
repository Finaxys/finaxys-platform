/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.FXRate;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.FXRateDao;

// TODO: Auto-generated Javadoc
/**
 * The Class FXRateDaoImpl.
 */
public class FXRateDaoImpl  extends AbstractBasicDao<FXRate> implements FXRateDao {
	


	private static Logger logger = Logger.getLogger(FXRateDaoImpl.class);

	public FXRateDaoImpl() {
		super();
	}

	public FXRateDaoImpl(HConnection connection) {
		super(connection);
	}

}
