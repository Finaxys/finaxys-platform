package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;


@HBaseRowKey(fields = {"provider", "symbol", "expiration", "source", "inputDate"})
@XmlRootElement(name = "optionChain")
@XmlType(propOrder = {"symbol", "expiration", "provider", "source", "inputDate", "dataType"})
public class OptionChain  extends ProductData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8241203495855981504L;


    private LocalDate expiration;
	

	public OptionChain() {
		super();
	}


	public OptionChain(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, LocalDate expiration) {
		super(source, inputDate, symbol, dataType, provider);
		this.expiration = expiration;
	}


	@XmlJavaTypeAdapter(com.finaxys.finaxysplatform.core.domain.jaxb.MonthLocalDateAdapter.class)
	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}




	

}
