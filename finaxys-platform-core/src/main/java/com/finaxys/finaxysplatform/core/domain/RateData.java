package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;

@XmlTransient
public abstract class RateData extends MarketData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;


	private DateTime rateDateTime;
	
	
	public RateData() {
		super();
	}


	public RateData(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime rateDateTime) {
		super(source, inputDate, symbol, dataType);
		this.rateDateTime = rateDateTime;
	}


	@XmlJavaTypeAdapter(com.finaxys.finaxysplatform.core.domain.jaxb.DateTimeAdapter.class)
	public DateTime getRateDateTime() {
		return rateDateTime;
	}


	public void setRateDateTime(DateTime rateDateTime) {
		this.rateDateTime = rateDateTime;
	}

	

}
