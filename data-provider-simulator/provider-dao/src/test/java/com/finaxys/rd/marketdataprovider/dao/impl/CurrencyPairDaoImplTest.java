package com.finaxys.rd.marketdataprovider.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.finaxys.finaxysplatform.core.domain.CurrencyPair;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.AbstractBasicDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.CurrencyPairDaoImpl;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyPairDaoImplTest extends HBaseBasicDaoImplTest<CurrencyPair> {

	@Override
	protected CurrencyPair getFixtureBean() {
		return new CurrencyPair('0', new DateTime(), "EURUSD", DataType.REF, '1', "EUR", "USD");
	}

	@Override
	protected AbstractBasicDao<CurrencyPair> getTarget(HConnection connection) {
		return new CurrencyPairDaoImpl(connection);
	}

	

}