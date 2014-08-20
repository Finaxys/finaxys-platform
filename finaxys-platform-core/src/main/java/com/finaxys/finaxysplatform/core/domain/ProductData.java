package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;

@XmlTransient
public abstract class ProductData extends MarketData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;

	
	private char provider;

	public ProductData() {
		super();
	}

	public ProductData(char source, DateTime inputDate, String symbol, DataType dataType, char provider) {
		super(source, inputDate, symbol, dataType);
		this.provider = provider;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

}
