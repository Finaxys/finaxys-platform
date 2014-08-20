/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;

// TODO: Auto-generated Javadoc
/**
 * The Class Exchange.
 */
@HBaseRowKey(fields = {"provider", "sourceSymbol", "symbol", "source", "inputDate"})
@XmlRootElement(name = "exchange")
@XmlType(propOrder = {"symbol", "sourceSymbol", "provider", "source", "name", "type", "continent", "country", "currency", "openTime", "closeTime", "delay", "inputDate", "dataType"})
public class Exchange extends MarketData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2241449587715451361L;


	private String sourceSymbol;
	
	private char provider;
	
	/** The name. */
	private String name;
	
	/** The type. */
	private String type;
	
	/** The continent. */
	private String continent;
	
	/** The country. */
	private String country;
	
	/** The currency. */
	private String currency;
	
	/** The open time. */
	private LocalTime openTime;
	
	/** The close time. */
	private LocalTime closeTime;
	
	private Integer delay;
	
	
	/**
	 * Instantiates a new exchange.
	 */
	public Exchange() {
		super();
	}

	public Exchange(char source, DateTime inputDate, String symbol,
			DataType dataType, String sourceSymbol, char provider, String name,
			String type, String continent, String country, String currency,
			LocalTime openTime, LocalTime closeTime, Integer delay) {
		super(source, inputDate, symbol, dataType);
		this.sourceSymbol = sourceSymbol;
		this.provider = provider;
		this.name = name;
		this.type = type;
		this.continent = continent;
		this.country = country;
		this.currency = currency;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.delay = delay;
	}


	public String getSourceSymbol() {
		return sourceSymbol;
	}

	public void setSourceSymbol(String sourceSymbol) {
		this.sourceSymbol = sourceSymbol;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the continent.
	 *
	 * @return the continent
	 */
	public String getContinent() {
		return continent;
	}

	/**
	 * Sets the continent.
	 *
	 * @param continent the new continent
	 */
	public void setContinent(String continent) {
		this.continent = continent;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Gets the currency.
	 *
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency.
	 *
	 * @param currency the new currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * Gets the open time.
	 *
	 * @return the open time
	 */ 
	@XmlJavaTypeAdapter(com.finaxys.finaxysplatform.core.domain.jaxb.TimeAdapter.class)	
	public LocalTime getOpenTime() {
		return openTime;
	}

	/**
	 * Sets the open time.
	 *
	 * @param openTime the new open time
	 */
	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	/**
	 * Gets the close time.
	 *
	 * @return the close time
	 */
	@XmlJavaTypeAdapter(com.finaxys.finaxysplatform.core.domain.jaxb.TimeAdapter.class)	
	public LocalTime getCloseTime() {
		return closeTime;
	}

	/**
	 * Sets the close time.
	 *
	 * @param closeTime the new close time
	 */
	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}



	/**
	 * Gets the provider.
	 *
	 * @return the provider
	 */
	public char getProvider() {
		return provider;
	}

	/**
	 * Sets the provider.
	 *
	 * @param provider the new provider
	 */
	public void setProvider(char provider) {
		this.provider = provider;
	}



	public Integer getDelay() {
		return delay;
	}



	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	

}
