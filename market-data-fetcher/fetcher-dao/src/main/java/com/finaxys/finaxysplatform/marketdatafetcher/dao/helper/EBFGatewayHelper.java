/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.finaxys.finaxysplatform.core.domain.Enum.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Class ProviderHelper.
 */
public class EBFGatewayHelper {

	public static final char EBF_PROVIDER_SYMB = Configuration.EBF_PROVIDER_SYMB.get().charAt(0);
	
	private static final String EBF_EURIBOR_URI = Configuration.EBF_EURIBOR_URI.get();
	

	
	public static URI contructEBFHistEuriborUri(Integer year, ContentType format) throws URISyntaxException {
		
		return new URI(EBF_EURIBOR_URI+ year + "." + format.getName());
	}

	
	/**
	 * The Enum Configuration.
	 */
	public enum Configuration {
		
		EBF_PROVIDER_SYMB("gateway.ebf.symbol"),
		
		EBF_EURIBOR_URI("gateway.ebf.euriborUri");
		
	    /** The key. */
    	private final String key;


	    /**
    	 * Instantiates a new configuration.
    	 *
    	 * @param key the key
    	 */
    	Configuration(String key) {
	        this.key = key;
	    }
	    
    	/** The Constant logger. */
    	private final static  Logger logger = Logger.getLogger(Configuration.class);
	    //TODo Share resources (properties files) inter modules
	    /** The Constant CONFIG_FILE. */
    	private final static String CONFIG_FILE = "/gateway.properties";
	    
    	/** The Constant configuration. */
    	private final static Map<Configuration, String> configuration = new EnumMap<>(Configuration.class);

	    static {
	        readConfigurationFrom(CONFIG_FILE);
	    }

	    /**
    	 * Read configuration from.
    	 *
    	 * @param fileName the file name
    	 */
    	private static void readConfigurationFrom(String fileName)  {
    		logger.info("read configuration from " + fileName + " for the gateway helper");
	        try (InputStream resource = Configuration.class.getResourceAsStream(fileName);) {
	            Properties properties = new Properties();
	            properties.load(resource); //throws a NPE if resource not founds
	            for (String key : properties.stringPropertyNames()) {
	            	Configuration c = getConfigurationKey(key);
	            	if(c!=null)
	                configuration.put(c, properties.getProperty(key));
	            }
	        } catch (IllegalArgumentException | IOException  e) {
	           logger.error("Exception when reading configuration file " + fileName + ": " + e);
	        }
	    }

	    /**
    	 * Gets the configuration key.
    	 *
    	 * @param key the key
    	 * @return the configuration key
    	 */
    	private static Configuration getConfigurationKey(String key) {
	        for (Configuration c : values()) {
	            if (c.key.equals(key)) {
	                return c;
	            }
	        }
	        return null;
	    }

	

	    /**
    	 * Gets the.
    	 *
    	 * @return the property corresponding to the key or null if not found
    	 */
	    public String get() {
	        String c = configuration.get(this);
	        if(c == null) throw new IllegalArgumentException();
	        return c;
	    }
	}
}
