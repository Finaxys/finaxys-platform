/*
 * 
 */
package com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.OptionChainDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.exception.DataAccessException;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionChainDaoImpl.
 */
public class OptionChainDaoImpl extends AbstractBasicDao<OptionChain> implements OptionChainDao {

	private static Logger logger = Logger.getLogger(OptionChainDaoImpl.class);

	public OptionChainDaoImpl() {
		super();
	}

	public OptionChainDaoImpl(HConnection connection) {
		super(connection);
	}

	public List<OptionChain> list(char provider) throws DataAccessException {
		byte provByte = (byte) provider;
		byte[] prefix = new byte[1];

		int offset = 0;
		offset = Bytes.putByte(prefix, offset, provByte);

		return list(prefix);

	}

}
