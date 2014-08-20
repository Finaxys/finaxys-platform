package com.finaxys.rd.marketdataprovider.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.finaxys.finaxysplatform.core.domain.InterbankRate;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.AbstractBasicDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.InterbankRateDaoImpl;

@RunWith(MockitoJUnitRunner.class)
public class InterbankRateDaoImplTest extends HBaseBasicDaoImplTest<InterbankRate> {

	@Override
	protected InterbankRate getFixtureBean() {
		return new InterbankRate('0', new DateTime(), "EONIA", DataType.REF, '1', "EUR");
	}

	@Override
	protected AbstractBasicDao<InterbankRate> getTarget(HConnection connection) {
		return new InterbankRateDaoImpl(connection);
	}

}