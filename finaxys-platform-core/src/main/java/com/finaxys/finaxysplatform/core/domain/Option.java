package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;


@HBaseRowKey(fields = {"provider", "exchSymb", "expiration", "optionChain", "symbol", "source", "inputDate"})
@XmlRootElement(name = "option")
@XmlType(propOrder = {"symbol", "exchSymb", "optionChain", "expiration", "optionType", "strike", "provider", "source", "inputDate", "dataType"})
public class Option  extends ProductData  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8128209219568521667L;



   private String exchSymb;

   private String optionChain;
	
	private String optionType;

	private BigDecimal strike;

    private LocalDate expiration;
	


	public Option() {
		super();
	}







	public Option(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, String exchSymb,
			String optionChain, String optionType, BigDecimal strike,
			LocalDate expiration) {
		super(source, inputDate, symbol, dataType, provider);
		this.exchSymb = exchSymb;
		this.optionChain = optionChain;
		this.optionType = optionType;
		this.strike = strike;
		this.expiration = expiration;
	}







	public String getOptionChain() {
		return optionChain;
	}


	public void setOptionChain(String optionChain) {
		this.optionChain = optionChain;
	}


	public String getOptionType() {
		return optionType;
	}


	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}


	public BigDecimal getStrike() {
		return strike;
	}


	public void setStrike(BigDecimal strike) {
		this.strike = strike;
	}


	@XmlJavaTypeAdapter(com.finaxys.finaxysplatform.core.domain.jaxb.LocalDateAdapter.class)
	public LocalDate getExpiration() {
		return expiration;
	}


	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}




	public String getExchSymb() {
		return exchSymb;
	}

	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}


}
