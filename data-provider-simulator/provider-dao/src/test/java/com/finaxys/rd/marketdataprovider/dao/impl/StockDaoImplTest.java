package com.finaxys.rd.marketdataprovider.dao.impl;

import org.apache.hadoop.hbase.client.HConnection;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.finaxys.finaxysplatform.core.domain.Stock;
import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.AbstractBasicDao;
import com.finaxys.finaxysplatform.dataprovidersimulator.dao.impl.StockDaoImpl;

@RunWith(MockitoJUnitRunner.class)
public class StockDaoImplTest extends HBaseBasicDaoImplTest<Stock> {

	@Override
	protected Stock getFixtureBean() {
		Stock stock = new Stock();
		stock.setSymbol("TIF");
		stock.setCompanyName("Tiffany & Co.");
		stock.setExchSymb("NY");
		stock.setProvider('1');
		stock.setSource('0');
		stock.setDataType(DataType.REF);
		stock.setInputDate(new DateTime());
		return stock;
		}

	@Override
	protected AbstractBasicDao<Stock> getTarget(HConnection connection) {
		return new StockDaoImpl(connection);
	}

	

}