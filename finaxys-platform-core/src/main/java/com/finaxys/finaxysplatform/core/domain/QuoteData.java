package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;

@XmlTransient
public abstract class QuoteData extends MarketData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866083217881607082L;


   private DateTime quoteDateTime;

	
	public QuoteData() {
		super();
	}





	public QuoteData(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime quoteDateTime) {
		super(source, inputDate, symbol, dataType);
		this.quoteDateTime = quoteDateTime;
	}





	@XmlJavaTypeAdapter(com.finaxys.finaxysplatform.core.domain.jaxb.DateTimeAdapter.class)
	public DateTime getQuoteDateTime() {
		return quoteDateTime;
	}


	public void setQuoteDateTime(DateTime quoteDateTime) {
		this.quoteDateTime = quoteDateTime;
	}

	
}
