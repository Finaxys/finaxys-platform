package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;

@XmlTransient
public abstract class MarketData extends ExtractedData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;

	private String symbol;

	private DataType dataType;

	
	public MarketData() {
		super();
	}

	public MarketData(char source, DateTime inputDate, String symbol,
			DataType dataType) {
		super(source, inputDate);
		this.symbol = symbol;
		this.dataType = dataType;
	}

	@XmlElement(name = "symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@XmlElement(name = "dataType")
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}
