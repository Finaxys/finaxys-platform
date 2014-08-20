package com.finaxys.rd.marketdataprovider.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.finaxys.finaxysplatform.core.domain.OptionChain;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.AbstractBasicDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.OptionChainDaoImpl;

@RunWith(MockitoJUnitRunner.class)
public class OptionChainDaoImplTest extends HBaseBasicDaoImplTest<OptionChain> {

	@Override
	protected OptionChain getFixtureBean() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM");
		return  new OptionChain('1', new DateTime(), "YHOO",
				DataType.REF, '1', formatter.parseDateTime("2014-07")
						.toLocalDate());
		}

	@Override
	protected AbstractBasicDao<OptionChain> getTarget(HConnection connection) {
		return new OptionChainDaoImpl(connection);
	}

	

}