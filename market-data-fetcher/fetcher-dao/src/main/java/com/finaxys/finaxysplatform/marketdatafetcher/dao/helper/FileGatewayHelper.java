/*
 * 
 */
package com.finaxys.finaxysplatform.marketdatafetcher.dao.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ProviderHelper.
 */
public class FileGatewayHelper {

	public static final char FILE_PROVIDER_SYMB = Configuration.FILE_PROVIDER_SYMB.get().charAt(0);

	public static final String DATA_FOLDER = Configuration.DATA_FOLDER.get();

	/**
	 * Gets the path.
	 *
	 * @param folder
	 *            the folder
	 * @param file
	 *            the file
	 * @param ext
	 *            the ext
	 * @return the path
	 */
	public static String getPath(String folder, String file, String ext) {
		return folder + "/" + file + "." + ext;
	}

	/**
	 * Gets the resource file.
	 *
	 * @param path
	 *            the path
	 * @return the resource file
	 */
	public static File getResourceFile(String path) {
		File f = new File(FileGatewayHelper.class.getResource(path).getPath());
		return f;

	}

	/**
	 * To bytes.
	 *
	 * @param file
	 *            the file
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] toBytes(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}

	/**
	 * The Enum Configuration.
	 */
	public enum Configuration {

		FILE_PROVIDER_SYMB("gateway.file.symbol"),

		DATA_FOLDER("gateway.file.dataFolder");

		/** The key. */
		private final String key;

		/**
		 * Instantiates a new configuration.
		 * 
		 * @param key
		 *            the key
		 */
		Configuration(String key) {
			this.key = key;
		}

		/** The Constant logger. */
		private final static Logger logger = Logger.getLogger(Configuration.class);
		// TODo Share resources (properties files) inter modules
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
		 * @param fileName
		 *            the file name
		 */
		private static void readConfigurationFrom(String fileName) {
			logger.info("read configuration from " + fileName + " for the gateway helper");
			try (InputStream resource = Configuration.class.getResourceAsStream(fileName);) {
				Properties properties = new Properties();
				properties.load(resource); // throws a NPE if resource not
											// founds
				for (String key : properties.stringPropertyNames()) {
					Configuration c = getConfigurationKey(key);
					if (c != null)
						configuration.put(c, properties.getProperty(key));
				}
			} catch (IllegalArgumentException | IOException  e) {
				logger.error("Exception when reading configuration file " + fileName + ": " + e);
			}
		}

		/**
		 * Gets the configuration key.
		 * 
		 * @param key
		 *            the key
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
			if (c == null)
				throw new IllegalArgumentException();
			return c;
		}
	}

}
