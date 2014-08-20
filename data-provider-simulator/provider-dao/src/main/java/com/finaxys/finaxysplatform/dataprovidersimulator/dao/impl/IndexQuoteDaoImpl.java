/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.IndexQuote;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.IndexQuoteDao;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuoteDaoImpl.
 */
public class IndexQuoteDaoImpl extends AbstractBasicDao<IndexQuote> implements IndexQuoteDao {

	private static Logger logger = Logger.getLogger(IndexQuoteDaoImpl.class);

	public IndexQuoteDaoImpl() {
		super();
	}

	public IndexQuoteDaoImpl(HConnection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

}
