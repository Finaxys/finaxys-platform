package com.finaxys.finaxysplatform.core.domain;

public class Enum {
	

	/**
	 * The Enum ContentType.
	 */
	public enum ContentType {

		/** The xml. */
		XML("xml"),
		/** The json. */
		JSON("json"),
		/** The xls. */
		XLS("xls");

		/** The name. */
		private final String name;

		/**
		 * Instantiates a new content type.
		 * 
		 * @param name
		 *            the name
		 */
		private ContentType(String name) {
			this.name = name;
		}

		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * The Enum DataClass.
	 */
	public enum DataClass {

		/** The Currency pair. */
		CurrencyPair,
		/** The Exchange. */
		Exchange,
		/** The FX rate. */
		FXRate,
		/** The Index. */
		Index,
		/** The Index quote. */
		IndexQuote,
		/** The Stock quote. */
		StockQuote,
		/** The Stock. */
		Stock,
		/** The InterbankRate. */
		InterbankRate,
		/** The InterbankRatesData. */
		InterbankRatesData,
		/** The Option. */
		Option,
		/** The Option Chain. */
		OptionChain,
		/** The Option Quote. */
		OptionQuote
	}

	/**
	 * The Enum DataType.
	 */
	public enum DataType {

		/** The eod. */
		EOD("EOD", 'e'),
		/** The intra. */
		INTRA("INTRA", 'i'),
		/** The hist. */
		HIST("HIST",'h'),
		/** The Ref. */
		REF("REF", 'r');

		/** The name. */
		private final String name;

		/** The t byte. */
		private final byte tByte;

		/**
		 * Instantiates a new data type.
		 * 
		 * @param name
		 *            the name
		 * @param id
		 *            the id
		 */
		private DataType(String name, char id) {
			this.name = name;
			this.tByte = (byte) id;
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
		 * Gets the t byte.
		 * 
		 * @return the t byte
		 */
		public byte getTByte() {
			return tByte;
		}
	}
	
	
	
	public enum Bucket {
		_0D("0D"),
		_1D("1D"),
		_2D("2D"),
		_1W("1W"),
		_2W("2W"),
		_3W("3W"),
		_1M("1M"),
		_2M("2M"),
		_3M("3M"),
		_4M("4M"),
		_5M("5M"),
		_6M("6M"),
		_7M("7M"),
		_8M("8M"),
		_9M("9M"),
		_10M("10M"),
		_11M("11M"),
		_1Y("1Y"),
		_5Y("5Y"),
		_10Y("10Y"),
		_15Y("15Y"),
		_20Y("20Y");
		
		/** The name. */
		private final String name;

		/**
		 * Instantiates a new content type.
		 * 
		 * @param name
		 *            the name
		 */
		private Bucket(String name) {
			this.name = name;
		}

		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
	}
	

}
