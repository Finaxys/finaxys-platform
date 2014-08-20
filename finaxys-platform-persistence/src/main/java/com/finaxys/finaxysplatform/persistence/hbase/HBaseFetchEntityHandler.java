package com.finaxys.finaxysplatform.persistence.hbase;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.datanucleus.store.types.TypeManager;

import com.finaxys.finaxysplatform.persistence.hbase.QueuedObject.QueuedEntity;
import com.finaxys.finaxysplatform.persistence.hbase.QueuedObject.QueuedNestedEntity;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseIOException;

public class HBaseFetchEntityHandler extends AbstractHBaseFetchEntityHandler {
	List<QueuedObject> queue;
	TypeManager typeMgr;

	public HBaseFetchEntityHandler(TypeManager typeMgr) {
		super();
		this.typeMgr = typeMgr;
	}

	@Override
	protected Object fetch(String tableName, Result result) throws HBaseIOException {
		queue = new ArrayList<QueuedObject>();

		Object entity = null;
		try {
			entity = Class.forName(tableName).newInstance();
			queue.add(new QueuedEntity(entity));
			byte[] value;
			QueuedObject current;
			while (!queue.isEmpty()) {
				current = queue.get(0);
				List<Field> fields = HBaseUtils.getFields(entity.getClass());
				for (Field field : fields) {
					value = result.getValue(HBaseUtils.DEFAULT_COLUMN_FAMILY, current.getQualifier(field));

					if (verifyPreConditions(value, field)) {
						if (HBaseUtils.isCollection(field)) {
							// fetchCollectionField(field, current, value);
						} else if (HBaseUtils.isMap(field)) {
							// fetchMapField(field, current, value);
						} else if (HBaseUtils.isArray(field)) {
							fetchArrayField(field, current, value);
						} else if (typeMgr.isDefaultPersistent(field.getType())) {
							fetchDefaultPersistentField(field, current, value);
						} else if (typeMgr.getTypeConverterForType(field.getType(), byte[].class) != null) {
							fetchConvertableField(field, current, value);
						} else {
							fetchEntityField(field, current, value);
						}
					}

				}
			}
			return entity;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | JSONException e) {
			throw new HBaseIOException();
		}

	}

	private boolean verifyPreConditions(Object value, Field field) throws IllegalArgumentException, IllegalAccessException {
		return !HBaseUtils.isStatic(field) && !HBaseUtils.isFinal(field) && value != null;
	}

	private boolean isIterable(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException {
		return HBaseUtils.isArray(clazz) && HBaseUtils.isCollection(clazz) && HBaseUtils.isMap(clazz);
	}

	private void fetchCollectionField(Field field, QueuedObject current, byte[] value) throws IllegalArgumentException, IllegalAccessException, JSONException,
			InstantiationException {

		JSONArray jsonArray = new JSONArray(new String(value));
		Collection coll = (Collection) field.getType().newInstance();
		field.setAccessible(true);
		field.set(current.getObj(), coll);
		ParameterizedType genericType = (ParameterizedType) field.getGenericType();
		Class<?> collClazz = (Class<?>) genericType.getActualTypeArguments()[0];
		for (int i = 0; i < jsonArray.length(); i++) {
			Object element = collClazz.newInstance();
			String base64StringRowKey = (String) jsonArray.get(i);
			byte[] rowkey = Base64.decode(base64StringRowKey);
			if (typeMgr.isDefaultPersistent(element.getClass())) {
				element = HBaseUtils.getTypedValue(field, rowkey);
			} else if (typeMgr.getTypeConverterForType(element.getClass(), byte[].class) != null) {
				element = typeMgr.getTypeConverterForType(element.getClass(), byte[].class).toMemberType(rowkey);
			} else {
				queue.add(new QueuedNestedEntity(element, rowkey, field.getName()));
			}
			coll.add(element);
		}
	}

	private void fetchMapField(Field field, QueuedObject current, byte[] value) throws IllegalArgumentException, IllegalAccessException, JSONException, InstantiationException {
		JSONArray jsonArray = new JSONArray(new String(value));
		Map map = (Map) field.getType().newInstance();
		field.set(current.getObj(), map);
		field.setAccessible(true);
		ParameterizedType genericType = (ParameterizedType) field.getGenericType();
		Class<?> keyClazz = (Class<?>) genericType.getActualTypeArguments()[0];
		Class<?> valClazz = (Class<?>) genericType.getActualTypeArguments()[1];
		for (int i = 0; i < jsonArray.length(); i++) {
			Object key = keyClazz.newInstance();
			Object val = valClazz.newInstance();
			String base64StringRowKey = (String) jsonArray.get(i);
			byte[] rowkey = Base64.decode(base64StringRowKey);
			if (typeMgr.isDefaultPersistent(keyClazz)) {
				key = HBaseUtils.getTypedValue(field, rowkey);
			} else if (typeMgr.getTypeConverterForType(key.getClass(), byte[].class) != null) {
				key = typeMgr.getTypeConverterForType(key.getClass(), byte[].class).toMemberType(rowkey);
			} else {
				queue.add(new QueuedNestedEntity(key, rowkey, field.getName() + "~k"));
			}
			queue.add(new QueuedNestedEntity(val, rowkey, field.getName() + "~v"));
			map.put(key, val);
		}
	}

	private void fetchArrayField(Field field, QueuedObject current, byte[] value) throws InstantiationException, IllegalAccessException, JSONException {

		JSONArray jsonArray = new JSONArray(new String(value));
		Object array = Array.newInstance(field.getType().getComponentType(), jsonArray.length());
		field.setAccessible(true);
		field.set(current.getObj(), array);
		for (int i = 0; i < jsonArray.length(); i++) {
			Object element = field.getType().getComponentType().newInstance();
			String base64StringRowKey = (String) jsonArray.get(i);
			byte[] rowkey = Base64.decode(base64StringRowKey);
			if (typeMgr.isDefaultPersistent(element.getClass())) {
				element = HBaseUtils.getTypedValue(field, rowkey);
			} else if (typeMgr.getTypeConverterForType(element.getClass(), byte[].class) != null) {
				element = typeMgr.getTypeConverterForType(element.getClass(), byte[].class).toMemberType(rowkey);
			} else {
				queue.add(new QueuedNestedEntity(element, rowkey, field.getName()));
			}
			Array.set(array, i, element);
		}
	}

	private void fetchDefaultPersistentField(Field field, QueuedObject current, byte[] value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		field.setAccessible(true);
		field.set(current.getObj(), HBaseUtils.getTypedValue(field, value));
	}

	private void fetchConvertableField(Field field, QueuedObject current, byte[] value) throws IllegalArgumentException, IllegalAccessException {
		Object convertedValue = typeMgr.getTypeConverterForType(field.getType(), byte[].class).toMemberType(value);
		field.setAccessible(true);
		field.set(current.getObj(), value);
	}

	private void fetchEntityField(Field field, QueuedObject current, byte[] value) throws JSONException, InstantiationException, IllegalAccessException {
		Object entity = field.getType().newInstance();
		field.setAccessible(true);
		field.set(current.getObj(), entity);
		String base64StringRowKey = (String) new JSONArray(new String(value)).get(0);
		byte[] rowkey = Base64.decode(base64StringRowKey);
		queue.add(new QueuedNestedEntity(entity, rowkey, field.getName()));
	}
}
