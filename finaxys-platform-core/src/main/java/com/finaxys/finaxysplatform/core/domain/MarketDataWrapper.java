/*
 * 
 */
package com.finaxys.finaxysplatform.core.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

// TODO: Auto-generated Javadoc
/**
 * The Class ExtractedDatas.
 */
@XmlRootElement(name = "data")
public class MarketDataWrapper<T extends MarketData> extends DataWrapper<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5764940959468511789L;

	public MarketDataWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MarketDataWrapper(List<T> list) {
		super(list);
		// TODO Auto-generated constructor stub
	}



	
}