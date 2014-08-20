package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;


@HBaseRowKey(fields = {"dataType", "expiration", "symbol", "quoteDateTime", "source", "inputDate"})
@XmlRootElement(name = "oquote")
@XmlType(propOrder = {"symbol", "dataType", "source", "quoteDateTime", "optionChain", "optionType", "price", "change", "prevClose", "open", "bid", "ask", "strike", "expiration", "volume", "openInterest",  "inputDate"})
public class OptionQuote extends QuoteData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2196303346943684110L;



//    @HBaseRowKeyField(className = "OptionQuote", order = 2)
	private String optionChain;
	
	private String optionType;
	
	private BigDecimal price;
	
	private BigDecimal change;
	
	private BigDecimal prevClose;

	private BigDecimal open;

	private BigDecimal bid;

	private BigDecimal ask;

	private BigDecimal strike;

	private LocalDate expiration;

	private BigInteger volume;

	private Integer openInterest;
	
	
	
	
	public OptionQuote() {
		super();
	}




	public OptionQuote(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime quoteDateTime, String optionChain,
			String optionType, BigDecimal price, BigDecimal change,
			BigDecimal prevClose, BigDecimal open, BigDecimal bid,
			BigDecimal ask, BigDecimal strike, LocalDate expiration,
			BigInteger volume, Integer openInterest) {
		super(source, inputDate, symbol, dataType, quoteDateTime);
		this.optionChain = optionChain;
		this.optionType = optionType;
		this.price = price;
		this.change = change;
		this.prevClose = prevClose;
		this.open = open;
		this.bid = bid;
		this.ask = ask;
		this.strike = strike;
		this.expiration = expiration;
		this.volume = volume;
		this.openInterest = openInterest;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public BigDecimal getPrevClose() {
		return prevClose;
	}

	public void setPrevClose(BigDecimal prevClose) {
		this.prevClose = prevClose;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public BigDecimal getAsk() {
		return ask;
	}

	public void setAsk(BigDecimal ask) {
		this.ask = ask;
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

	public BigInteger getVolume() {
		return volume;
	}

	public void setVolume(BigInteger volume) {
		this.volume = volume;
	}

	public Integer getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(Integer openInterest) {
		this.openInterest = openInterest;
	}






}
