package com.finaxys.finaxysplatform.core.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
 
public class LocalDateAdapter
    extends XmlAdapter<String, LocalDate>{
	private DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	
    public LocalDate unmarshal(String v)   {
        return dformatter.parseDateTime(v).toLocalDate();
    }
 
    public String marshal(LocalDate v)   {
        return dformatter.print(v);
    }
 
}