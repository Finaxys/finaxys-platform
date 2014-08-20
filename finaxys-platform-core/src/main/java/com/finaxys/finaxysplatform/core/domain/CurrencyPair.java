/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.RowKeyHashStrategy;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPair.
 */

@HBaseRowKey(fields = {"provider", "inputDate", "symbol", "source"}, strategy = RowKeyHashStrategy.class)
@XmlRootElement(name = "currency")
@XmlType(propOrder = { "symbol", "baseCurrency", "quoteCurrency", "provider" , "source", "inputDate", "dataType"})
public class CurrencyPair extends ProductData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3823599717828581565L;
//	private   FXRate[] rates = new FXRate[]{new FXRate('1', new DateTime(), "EURUSD",
//        		DataType.INTRA, new DateTime("2014-08-20T02:07:04+02:00"),
//        		new BigDecimal("1.3463"), new BigDecimal("1.3463"),
//        		new BigDecimal("1.3462")),new FXRate('1', new DateTime(), "EURUSD",
//    			DataType.INTRA, new DateTime("2014-08-20T02:07:04+02:00"),
//    			new BigDecimal("1.3463"), new BigDecimal("1.3463"),
//    			new BigDecimal("1.3462"))
//    };
   
	private FXRate fx = new FXRate('1', new DateTime("2014-08-20", DateTimeZone.UTC), "EURUSD",
    		DataType.INTRA, new DateTime("2014-08-20", DateTimeZone.UTC),
    		new BigDecimal("1.3463"), new BigDecimal("1.3463"),
    		new BigDecimal("1.3462"));
  
	/** The base currency. */
	private String baseCurrency;

	/** The quote currency. */
	private String quoteCurrency;



	public CurrencyPair() {
		super();
	}



	public CurrencyPair(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, String baseCurrency,
			String quoteCurrency) {
		super(source, inputDate, symbol, dataType, provider);
		this.baseCurrency = baseCurrency;
		this.quoteCurrency = quoteCurrency;
	}



	/**
	 * Gets the base currency.
	 * 
	 * @return the base currency
	 */
	@XmlElement(name = "base")
	public String getBaseCurrency() {
		return baseCurrency;
	}

	/**
	 * Sets the base currency.
	 * 
	 * @param baseCurrency
	 *            the new base currency
	 */
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	/**
	 * Gets the quote currency.
	 * 
	 * @return the quote currency
	 */
	@XmlElement(name = "quote")
	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	/**
	 * Sets the quote currency.
	 * 
	 * @param quoteCurrency
	 *            the new quote currency
	 */
	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}



	@Override
	public String toString() {
		return "CurrencyPair [baseCurrency=" + baseCurrency
				+ ", quoteCurrency=" + quoteCurrency + "]";
	}







}
