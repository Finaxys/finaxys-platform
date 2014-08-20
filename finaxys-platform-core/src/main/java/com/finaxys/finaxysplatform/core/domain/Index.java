/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexInfo.
 */

@HBaseRowKey(fields = {"provider", "exchSymb", "symbol", "source", "inputDate"})
@XmlRootElement(name = "indexInfo")
@XmlType(propOrder = {"symbol","name", "exchSymb", "provider", "source", "inputDate", "dataType"})
public class Index  extends ProductData  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4969774516615065775L;

	
	/** The name. */
	private String name;
	
	/** The exch symb. */
    private String exchSymb;
	

	/**
	 * Instantiates a new index info.
	 */
	public Index() {
		super();
	}

	public Index(char source, DateTime inputDate, String symbol,
			DataType dataType, char provider, String name, String exchSymb) {
		super(source, inputDate, symbol, dataType, provider);
		this.name = name;
		this.exchSymb = exchSymb;
	}


	/**
	 * Gets the exch symb.
	 *
	 * @return the exch symb
	 */
	public String getExchSymb() {
		return exchSymb;
	}
	
	/**
	 * Sets the exch symb.
	 *
	 * @param exchSymb the new exch symb
	 */
	public void setExchSymb(String exchSymb) {
		this.exchSymb = exchSymb;
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


}
