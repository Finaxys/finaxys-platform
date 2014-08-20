/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.datanucleus.store.types.TypeManager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseException;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseRowKeyCreationException;

public class HBaseUtils {

	private static Logger logger = Logger.getLogger(HBaseUtils.class);

	public static final byte[] DEFAULT_COLUMN_FAMILY = new byte[] { (byte) 'f' };

	public static RowKey makeRowKey(Object entity, TypeManager typeMgr) throws HBaseRowKeyCreationException {
		Assert.notNull(entity, "Cannot persist null object");
		try {
			TreeSet<RowKeyField> rowkeyFields = new RowkeyFieldsLookup(entity, typeMgr).lookup();

			if (rowkeyFields == null || rowkeyFields.size() == 0)
				return new RowKeyBuilder().withField(new RowKeyField(toBytes(UUID.randomUUID().toString()), 0)).build();

			HBaseRowKey annotation = (HBaseRowKey) entity.getClass().getAnnotation(HBaseRowKey.class);
			RowKeyDesignStrategy strategy = annotation.strategy().newInstance();
			return new RowKeyBuilder().withFields(rowkeyFields).withStrategy(strategy).build();

		} catch (IllegalAccessException | InstantiationException e) {
			throw new HBaseRowKeyCreationException(e);
		}
	}

	public static Scan createScan() {
		Scan scan = new Scan();
		return scan;
	}

	public static Scan createScan(byte[] start, byte[] end) {
		Scan scan = new Scan(start, end);
		return scan;
	}

	public static Scan createScan(String prefix) {
		Scan scan = new Scan();
		org.apache.hadoop.hbase.filter.RegexStringComparator prefixFilter = new org.apache.hadoop.hbase.filter.RegexStringComparator("^" + prefix + "*");
		RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, prefixFilter);
		scan.setFilter(rowFilter);

		return scan;
	}

	public static Scan createScan(byte[] prefix) {
		Scan scan = new Scan();
		PrefixFilter prefixFilter = new org.apache.hadoop.hbase.filter.PrefixFilter(prefix);
		scan.setFilter(prefixFilter);

		return scan;
	}

	// Use type manager to resolve embedded types (used for rowkey building.
	// expl: joda DateTime) bytes converting
	public static Put createPut(Object entity, TypeManager typeMgr) throws HBaseException {
		Assert.notNull(entity, "Cannot persist null object");
		Put put = new Put(HBaseUtils.makeRowKey(entity, typeMgr).getKey());
		return put;
	}

	public static List<Field> getFields(Class<?> clazz) {
		Field f;
		List<Field> fields = new ArrayList<Field>();
		while (clazz != null) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	public static boolean isCollection(Field field) {
		return isCollection(field.getType());
	}

	public static boolean isCollection(Class<?> clazz) {
		return java.util.Collection.class.isAssignableFrom(clazz);
	}

	public static boolean isMap(Field field) {
		return isMap(field.getType());
	}

	public static boolean isMap(Class<?> clazz) {
		return java.util.Map.class.isAssignableFrom(clazz);
	}

	public static boolean isArray(Field field) {
		return field.getType().isArray();
	}

	public static boolean isArray(Class<?> clazz) {
		return clazz.isArray();
	}

	public static boolean isTransient(Field field) {
		return java.lang.reflect.Modifier.isTransient(field.getModifiers());

	}

	public static boolean isFinal(Field field) {
		return java.lang.reflect.Modifier.isFinal(field.getModifiers());

	}

	public static boolean isStatic(Field field) {
		return java.lang.reflect.Modifier.isStatic(field.getModifiers());
	}

	// No support for BigInteger ????!! Solve this
	public static byte[] toBytes(Object value) {

		Assert.notNull(value);
		if (DateTime.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes(new DateTime(value, DateTimeZone.UTC).getMillis());
		} else if (Boolean.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes((Boolean) value);
		} else if (Byte.class.isAssignableFrom(value.getClass())) {
			return new byte[] { (Byte) value };
		} else if (Character.class.isAssignableFrom(value.getClass())) {
			return ("" + value).getBytes();
		} else if (Double.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes((Double) value);
		} else if (Float.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes((Float) value);
		} else if (Integer.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes((Integer) value);
		} else if (Long.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes((Long) value);
		} else if (Short.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes((Short) value);
		} else if (BigDecimal.class.isAssignableFrom(value.getClass())) {
			return Bytes.toBytes(((BigDecimal) value).doubleValue());// double
																		// conversion
																		// just
																		// for
																		// the
																		// demo
																		// (to
																		// change)
		} else if (String.class.isAssignableFrom(value.getClass())) {
			return ((String) value).getBytes();
		} else if (Enum.class.isAssignableFrom(value.getClass())) {
			return ((Enum) value).name().getBytes();
		} else {
			logger.fatal("Persistence of column of type " + value.getClass().getName() + " but no conversion to bytes defined. Report this");
			return null;
		}

	}

	public static Object getTypedValue(Field field, byte[] value) {

		if (value != null) {
			if (String.class.isAssignableFrom(field.getType()))
				return Bytes.toString(value);
			if (Long.class.isAssignableFrom(field.getType()))
				return Bytes.toLong(value);
			if (Integer.class.isAssignableFrom(field.getType()))
				return Bytes.toInt(value);
			if (char.class.isAssignableFrom(field.getType()))
				return (char) value[0];
			if (BigDecimal.class.isAssignableFrom(field.getType()))
				return new BigDecimal(Bytes.toDouble(value));// double value
																// just for the
																// demo
			if (BigInteger.class.isAssignableFrom(field.getType()))
				return new BigInteger(value);
			if (Enum.class.isAssignableFrom(field.getType()))
				return Enum.valueOf((Class) field.getType(), Bytes.toString(value));
			return value;
		} else {
			logger.fatal("Reading of column of type " + field.getType().getName() + " but no conversion from bytes defined. Report this");
			return null;
		}

	}

}
