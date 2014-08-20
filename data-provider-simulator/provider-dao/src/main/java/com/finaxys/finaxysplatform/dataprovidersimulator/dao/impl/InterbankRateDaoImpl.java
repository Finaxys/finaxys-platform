package com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.InterbankRateDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.exception.DataAccessException;

public class InterbankRateDaoImpl extends AbstractBasicDao<InterbankRate> implements InterbankRateDao {

	private static Logger logger = Logger.getLogger(InterbankRateDaoImpl.class);

	public InterbankRateDaoImpl() {
		super();
	}

	public InterbankRateDaoImpl(HConnection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public List<InterbankRate> list(char provider) throws DataAccessException {
		byte provByte = (byte) provider;
		byte[] prefix = new byte[1];

		int offset = 0;
		offset = Bytes.putByte(prefix, offset, provByte);

		return list(prefix);

	}
}
