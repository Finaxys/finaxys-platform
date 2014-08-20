package com.finaxys.finaxysplatform.core.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
 
public class TimeAdapter
    extends XmlAdapter<String, LocalTime>{
	private DateTimeFormatter dformatter = DateTimeFormat.forPattern("HH:mm:ss");
	
    public LocalTime unmarshal(String v)   {
        return dformatter.parseDateTime(v).toLocalTime();
    }
 
    public String marshal(LocalTime v)   {
        return dformatter.print(v);
    }
 
}