package com.finaxys.finaxysplatform.core.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
 
public class DateTimeAdapter
    extends XmlAdapter<String, DateTime>{
	private DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
    public DateTime unmarshal(String v)   {
        return dformatter.parseDateTime(v);
    }
 
    public String marshal(DateTime v)   {
        return dformatter.print(v);
    }
 
}