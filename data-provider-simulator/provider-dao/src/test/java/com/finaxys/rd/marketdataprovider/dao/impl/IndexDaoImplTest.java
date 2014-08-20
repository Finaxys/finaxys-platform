package com.finaxys.rd.marketdataprovider.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.finaxys.finaxysplatform.core.domain.Index;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.AbstractBasicDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.IndexDaoImpl;

@RunWith(MockitoJUnitRunner.class)
public class IndexDaoImplTest extends HBaseBasicDaoImplTest<Index> {

	@Override
	protected Index getFixtureBean() {
		return new Index('0', new DateTime(), "^DJX", DataType.REF, '1',
			"1/100 DOW JONES INDUSTRIAL AVER", "NY");
	}

	@Override
	protected AbstractBasicDao<Index> getTarget(HConnection connection) {
		return new IndexDaoImpl(connection);
	}

	

}