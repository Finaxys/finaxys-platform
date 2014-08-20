/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;

// TODO: Auto-generated Javadoc
/**
 * The Class Stock.
 */
@HBaseRowKey(fields = {"provider", "exchSymb", "symbol", "source", "inputDate"})
@XmlRootElement(name = "stock")
@XmlType(propOrder = { "symbol", "exchSymb", "companyName", "start", "end", "sector", "industry", "fullTimeEmployees", "provider", "source", "inputDate", "dataType"})
public class Stock extends ProductData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3735824421169372229L;

	/** The exch symb. */
	private String exchSymb;

	/** The company name. */
	private String companyName;

	/** The start. */
	private DateTime start;

	/** The end. */
	private DateTime end;

	/** The sector. */
	private String sector;

	/** The industry. */
	private String industry;

	/** The full time employees. */
	private Integer fullTimeEmployees;

	
	/**
	 * Instantiates a new stock.
	 */
	public Stock() {
		super();
	}


	public Stock(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, String exchSymb,
			String companyName, DateTime start, DateTime end, String sector,
			String industry, Integer fullTimeEmployees) {
		super(source, inputDate, symbol, dataType, provider);
		this.exchSymb = exchSymb;
		this.companyName = companyName;
		this.start = start;
		this.end = end;
		this.sector = sector;
		this.industry = industry;
		this.fullTimeEmployees = fullTimeEmployees;
	}


	/**
	 * Gets the company name.
	 * 
	 * @return the company name
	 */
	@XmlElement(name = "CompanyName")
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * Sets the company name.
	 * 
	 * @param companyName
	 *            the new company name
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * Gets the start.
	 * 
	 * @return the start
	 */
	@XmlElement(name = "start")
	public DateTime getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 * 
	 * @param start
	 *            the new start
	 */
	public void setStart(DateTime start) {
		this.start = start;
	}

	/**
	 * Gets the end.
	 * 
	 * @return the end
	 */
	@XmlElement(name = "end")
	public DateTime getEnd() {
		return end;
	}

	/**
	 * Sets the end.
	 * 
	 * @param end
	 *            the new end
	 */
	public void setEnd(DateTime end) {
		this.end = end;
	}

	/**
	 * Gets the sector.
	 * 
	 * @return the sector
	 */
	@XmlElement(name = "Sector")
	public String getSector() {
		return sector;
	}

	/**
	 * Sets the sector.
	 * 
	 * @param sector
	 *            the new sector
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * Gets the industry.
	 * 
	 * @return the industry
	 */
	@XmlElement(name = "Industry")
	public String getIndustry() {
		return industry;
	}

	/**
	 * Sets the industry.
	 * 
	 * @param industry
	 *            the new industry
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}

	/**
	 * Gets the full time employees.
	 * 
	 * @return the full time employees
	 */
	@XmlElement(name = "FullTimeEmployees")
	public Integer getFullTimeEmployees() {
		return fullTimeEmployees;
	}

	/**
	 * Sets the full time employees.
	 * 
	 * @param fullTimeEmployees
	 *            the new full time employees
	 */
	public void setFullTimeEmployees(Integer fullTimeEmployees) {
		this.fullTimeEmployees = fullTimeEmployees;
	}

	/**
	 * Gets the exch symb.
	 * 
	 * @return the exch symb
	 */
	@XmlElement(name = "ExchSymb")
	public String getExchSymb() {
		return exchSymb;
	}

	/**
	 * Sets the exch symb.
	 * 
	 * @param exchSymb
	 *            the new exch symb
	 */
	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
	}

	
}
