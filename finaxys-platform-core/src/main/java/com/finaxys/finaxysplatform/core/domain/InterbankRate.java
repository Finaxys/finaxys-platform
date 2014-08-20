package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;

@HBaseRowKey(fields = {"provider", "symbol", "currency", "source", "inputDate"})
@XmlRootElement(name = "rate")
@XmlType(propOrder = {"symbol", "currency", "provider", "source", "inputDate", "dataType"})
public class InterbankRate  extends ProductData  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5710626964653341196L;

	private String currency;

	/**
	 * Instantiates a new rate.
	 */
	public InterbankRate() {
	  super();
	}

	public InterbankRate(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, String currency) {
		super(source, inputDate, symbol, dataType, provider);
		this.currency = currency;
	}

	/**
	 * Gets the currency.
	 *
	 * @return the currency
	 */
	@XmlElement(name = "Currency")
	public String getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency.
	 *
	 * @param name the new currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}



	
}
