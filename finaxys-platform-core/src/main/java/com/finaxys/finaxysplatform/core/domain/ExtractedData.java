package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

@XmlTransient
public abstract class ExtractedData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2214146820730387828L;

	private char source;

	private DateTime inputDate;

	public ExtractedData() {
		super();
	}

	public ExtractedData(char source, DateTime inputDate) {
		super();
		this.source = source;
		this.inputDate = inputDate;
	}

	@XmlElement(name = "source")
	public char getSource() {
		return source;
	}

	public void setSource(char source) {
		this.source = source;
	}

	@XmlElement(name = "inputDate")
	@XmlJavaTypeAdapter(com.finaxys.finaxysplatform.core.domain.jaxb.DateTimeAdapter.class)
	public DateTime getInputDate() {
		return inputDate;
	}

	public void setInputDate(DateTime inputDate) {
		this.inputDate = inputDate;
	}

}
