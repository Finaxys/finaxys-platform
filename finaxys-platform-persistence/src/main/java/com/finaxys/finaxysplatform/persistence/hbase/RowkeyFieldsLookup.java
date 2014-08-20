package com.finaxys.finaxysplatform.persistence.hbase;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.beanutils.PropertyUtils;
import org.datanucleus.store.types.TypeManager;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.persistence.hbase.annotation.HBaseRowKey;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseRowKeyBadTypeException;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseRowKeyLookupException;

public class RowkeyFieldsLookup {

	Object entity;
	TreeSet<RowKeyField> fields;
	List<String> fieldsNames;
	TypeManager typeMgr;

	public RowkeyFieldsLookup(Object entity, TypeManager typeMgr) {
		Assert.notNull(entity);
		this.entity = entity;
		this.typeMgr = typeMgr;
		Class<?> clazz = entity.getClass();
		if (clazz.isAnnotationPresent(HBaseRowKey.class)) {
			Annotation annotation = clazz
					.getAnnotation(HBaseRowKey.class);
			HBaseRowKey hbaseRowKey = (HBaseRowKey) annotation;
			fieldsNames = Arrays.asList(hbaseRowKey.fields());
		}
	}

	public TreeSet<RowKeyField> lookup() throws HBaseRowKeyLookupException {
		if(fieldsNames == null || fieldsNames.size() == 0) return null;
		fields = new TreeSet<RowKeyField>();
		Class<?> clazz = entity.getClass();
		try {
			return this.lookup(clazz);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new HBaseRowKeyLookupException();
		}
	}

	private TreeSet<RowKeyField> lookup(Class<?> clazz)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (clazz == null || fields.size() == fieldsNames.size())
			return fields;


		byte[] rowkeyField = null; 
		for (Field field : clazz.getDeclaredFields()) {
			if (fieldsNames.contains(field.getName())) {
				if (typeMgr.isDefaultPersistent(field.getType())) {
					rowkeyField = HBaseUtils.toBytes(PropertyUtils
							.getSimpleProperty(entity, field.getName()));
				}
				else if (typeMgr.getTypeConverterForType(field.getType(), byte[].class) != null) {
					rowkeyField = (byte[]) typeMgr.getTypeConverterForType(field.getType(), byte[].class).toDatastoreType(PropertyUtils
							.getSimpleProperty(entity, field.getName()));
				}else throw new HBaseRowKeyBadTypeException();
				
				fields.add(new RowKeyField(rowkeyField, fieldsNames
						.indexOf(field.getName())));
			}

		}
		return this.lookup(clazz.getSuperclass());

	}
}
