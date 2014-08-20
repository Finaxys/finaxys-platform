package com.finaxys.finaxysplatform.core.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

import com.finaxys.finaxysplatform.core.domain.Enum.DataType;
import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;


@HBaseRowKey(fields = {"dataType", "symbol", "currency", "bucket", "rateDateTime", "source", "inputDate"})
@XmlRootElement(name = "interbankRateData")
@XmlType(propOrder = { "symbol", "currency", "bucket", "dataType", "source", "rateDateTime", "value", "inputDate" })
public class InterbankRateData  extends RateData  implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 8587455397446182450L;

	private String currency;
	
	private String bucket;
	
	private BigDecimal value = new BigDecimal(0);

	public InterbankRateData() {
		super();
	}


	public InterbankRateData(char source, DateTime inputDate, String symbol,
			DataType dataType, DateTime rateDateTime, String currency,
			String bucket, BigDecimal value) {
		super(source, inputDate, symbol, dataType, rateDateTime);
		this.currency = currency;
		this.bucket = bucket;
		this.value = value;
	}


	@XmlElement(name = "Currency", required = true, nillable=false)
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@XmlElement(name = "Bucket", required = true, nillable=false)
	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	

	@XmlElement(name = "Value", required = true, nillable=false)
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}


}
