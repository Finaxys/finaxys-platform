/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;

// TODO: Auto-generated Javadoc
/**
 * The Class FXRate.
 */
@HBaseRowKey(fields = {"dataType", "symbol", "rateDateTime", "source", "inputDate"})
@XmlRootElement(name = "fxRate")
@XmlType(propOrder = { "symbol", "source", "rateDateTime", "dataType", "rate", "ask", "bid", "inputDate"})
public class FXRate  extends RateData  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2335287449316671097L;

	
	/** The rate. */
	private BigDecimal rate;
	
	/** The ask. */
	private BigDecimal ask; 
	
	/** The bid. */
	private BigDecimal bid; 


	/**
	 * Instantiates a new FX rate.
	 */
	public FXRate() {
		super();
	}



	public FXRate(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime rateDateTime, BigDecimal rate,
			BigDecimal ask, BigDecimal bid) {
		super(source, inputDate, symbol, dataType, rateDateTime);
		this.rate = rate;
		this.ask = ask;
		this.bid = bid;
	}



	/**
	 * Gets the rate.
	 *
	 * @return the rate
	 */
	@XmlElement(name = "Rate")
	public BigDecimal getRate() {
		return rate;
	}

	/**
	 * Sets the rate.
	 *
	 * @param rate the new rate
	 */
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	/**
	 * Gets the ask.
	 *
	 * @return the ask
	 */
	@XmlElement(name = "Ask")
	public BigDecimal getAsk() {
		return ask;
	}

	/**
	 * Sets the ask.
	 *
	 * @param ask the new ask
	 */
	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	/**
	 * Gets the bid.
	 *
	 * @return the bid
	 */
	@XmlElement(name = "Bid")
	public BigDecimal getBid() {
		return bid;
	}

	/**
	 * Sets the bid.
	 *
	 * @param bid the new bid
	 */
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

}
