/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.Index;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.IndexDao;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.exception.DataAccessException;
import com.finaxys.finaxysplatform.marketdatafetcher.dao.helper.DaoHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexDaoImpl.
 */
public class IndexDaoImpl extends AbstractBasicDao<Index> implements IndexDao {

	private static Logger logger = Logger.getLogger(IndexDaoImpl.class);

	public IndexDaoImpl() {
		super();
	}

	public IndexDaoImpl(HConnection connection) {
		super(connection);
	}

	public List<Index> list(char provider, String exchSymb) throws DataAccessException {
		byte provByte = (byte) provider;
		byte[] exchSymbHash = DaoHelper.md5sum(exchSymb);
		byte[] prefix = new byte[exchSymbHash.length + 1];

		int offset = 0;
		offset = Bytes.putByte(prefix, offset, provByte);
		Bytes.putBytes(prefix, offset, exchSymbHash, 0, exchSymbHash.length);

		return list(prefix);

	}
}