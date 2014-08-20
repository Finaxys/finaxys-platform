/*
 * 
 */


package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;


// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuote.
 */
@HBaseRowKey(fields = {"dataType", "symbol", "exchSymb", "quoteDateTime", "source", "inputDate"})
@XmlRootElement(name = "indexQuote")
@XmlType(propOrder = { "symbol", "exchSymb", "quoteDateTime", "source", "dataType", "lastTradePriceOnly", "change", "daysHigh", "daysLow", "volume", "open", "close", "adjClose", "inputDate" })
public class IndexQuote  extends QuoteData  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6697935490907016258L;


   	private String exchSymb;
	
	/** The last trade price only. */
	private BigDecimal lastTradePriceOnly;
	
	
	/** The change. */
	private BigDecimal change = new BigDecimal(0);
	
	/** The open. */
	private BigDecimal open = new BigDecimal(0);
	
	/** The days high. */
	private BigDecimal daysHigh = new BigDecimal(0);
	
	/** The days low. */
	private BigDecimal daysLow = new BigDecimal(0);
	
	/** The volume. */
	private BigInteger volume = new BigInteger("0");
	
	/** The days low. */
	private BigDecimal close = new BigDecimal(0);

	/** The days low. */
	private BigDecimal adjClose = new BigDecimal(0);


	/**
	 * Instantiates a new index quote.
	 */
	public IndexQuote() {
		super();
	}

	public IndexQuote(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime quoteDateTime, String exchSymb,
			BigDecimal lastTradePriceOnly, BigDecimal change, BigDecimal open,
			BigDecimal daysHigh, BigDecimal daysLow, BigInteger volume,
			BigDecimal close, BigDecimal adjClose) {
		super(source, inputDate, symbol, dataType, quoteDateTime);
		this.exchSymb = exchSymb;
		this.lastTradePriceOnly = lastTradePriceOnly;
		this.change = change;
		this.open = open;
		this.daysHigh = daysHigh;
		this.daysLow = daysLow;
		this.volume = volume;
		this.close = close;
		this.adjClose = adjClose;
	}




	/**
	 * Gets the last trade price only.
	 *
	 * @return the last trade price only
	 */
	@XmlElement(name = "LastTradePriceOnly")
	public BigDecimal getLastTradePriceOnly() {
		return lastTradePriceOnly;
	}

	/**
	 * Sets the last trade price only.
	 *
	 * @param lastTradePriceOnly the new last trade price only
	 */
	public void setLastTradePriceOnly(BigDecimal lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}


	/**
	 * Gets the change.
	 *
	 * @return the change
	 */
	@XmlElement(name = "Change")
	public BigDecimal getChange() {
		return change;
	}

	/**
	 * Sets the change.
	 *
	 * @param change the new change
	 */
	public void setChange(BigDecimal change) {
		this.change = change;
	}

	/**
	 * Gets the open.
	 *
	 * @return the open
	 */
	@XmlElement(name = "Open")
	public BigDecimal getOpen() {
		return open;
	}

	/**
	 * Sets the open.
	 *
	 * @param open the new open
	 */
	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	/**
	 * Gets the days high.
	 *
	 * @return the days high
	 */
	@XmlElement(name = "DaysHigh")
	public BigDecimal getDaysHigh() {
		return daysHigh;
	}

	/**
	 * Sets the days high.
	 *
	 * @param daysHigh the new days high
	 */
	public void setDaysHigh(BigDecimal daysHigh) {
		this.daysHigh = daysHigh;
	}

	/**
	 * Gets the days low.
	 *
	 * @return the days low
	 */
	@XmlElement(name = "DaysLow")
	public BigDecimal getDaysLow() {
		return daysLow;
	}

	/**
	 * Sets the days low.
	 *
	 * @param daysLow the new days low
	 */
	public void setDaysLow(BigDecimal daysLow) {
		this.daysLow = daysLow;
	}

	/**
	 * Gets the volume.
	 *
	 * @return the volume
	 */
	@XmlElement(name = "Volume")
	public BigInteger getVolume() {
		return volume;
	}

	/**
	 * Sets the volume.
	 *
	 * @param volume the new volume
	 */
	public void setVolume(BigInteger volume) {
		this.volume = volume;
	}



	
	@XmlElement(name = "Close", defaultValue="0")
	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	@XmlElement(name = "AdjClose", defaultValue="0")
	public BigDecimal getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(BigDecimal adjClose) {
		this.adjClose = adjClose;
	}



	public String getExchSymb() {
		return exchSymb;
	}



	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}


}
