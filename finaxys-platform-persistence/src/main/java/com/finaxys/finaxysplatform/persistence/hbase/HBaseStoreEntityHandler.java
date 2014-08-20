package com.finaxys.finaxysplatform.persistence.hbase;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.datanucleus.store.types.TypeManager;

import com.finaxys.finaxysplatform.persistence.hbase.QueuedObject.QueuedEntity;
import com.finaxys.finaxysplatform.persistence.hbase.QueuedObject.QueuedNestedEntity;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseException;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseIOException;
import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseRowKeyCreationException;

public class HBaseStoreEntityHandler extends AbstractHBaseStoreEntityHandler {
	List<QueuedObject> queue;
	TypeManager typeMgr;

	public HBaseStoreEntityHandler(TypeManager typeMgr) {
		super();
		this.typeMgr = typeMgr;
	}

	@Override
	protected void store(Object entity, Put put) throws HBaseIOException {
		queue = new ArrayList<QueuedObject>();
		queue.add(new QueuedEntity(entity));

		QueuedObject current;
		while (!queue.isEmpty()) {
			current = queue.get(0);
			List<Field> fields = HBaseUtils.getFields(current.getObj().getClass());
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					if (verifyPreConditions(current.getObj(), field)) {
						if (HBaseUtils.isCollection(field)) {
							storeCollectionField(field, current, put);
						} else if (HBaseUtils.isMap(field)) {
							storeMapField(field, current, put);
						} else if (HBaseUtils.isArray(field)) {
							storeArrayField(field, current, put);
						} else if (typeMgr.isDefaultPersistent(field.getType())) {
							storeDefaultPersistentField(field, current, put);
						} else if (typeMgr.getTypeConverterForType(field.getType(), byte[].class) != null) {
							storeConvertableField(field, current, put);
						} else {
							storeEntityField(field, current, put);
						}
					}
				} catch (HBaseRowKeyCreationException | ArrayIndexOutOfBoundsException | IllegalArgumentException | IllegalAccessException | IOException e) {
					throw new HBaseException(e.getMessage(), e);
				}
			}
			queue.remove(0);
		}
	}

	private boolean verifyPreConditions(Object entity, Field field) throws IllegalArgumentException, IllegalAccessException {
		return !HBaseUtils.isStatic(field) && !HBaseUtils.isFinal(field) && field.get(entity) != null;
	}

	private boolean isIterable(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException {
		return HBaseUtils.isArray(clazz) && HBaseUtils.isCollection(clazz) && HBaseUtils.isMap(clazz);
	}

	private void storeCollectionField(Field field, QueuedObject current, Put put) throws IllegalArgumentException, IllegalAccessException {

		Collection coll = (Collection) field.get(current.getObj());
		// ParameterizedType genericSuperclass = (ParameterizedType)
		// field.getGenericType();
		// Class<?> collClazz = (Class<?>)
		// genericSuperclass.getActualTypeArguments()[0];
		List<String> base64Ids = new ArrayList<String>();

		Iterator collIter = coll.iterator();

		while (collIter.hasNext()) {
			Object element = collIter.next();
			byte[] rowkey = null;
			if (isIterable(element.getClass())) {
				break;
			} else if (typeMgr.isDefaultPersistent(element.getClass())) {
				rowkey = HBaseUtils.toBytes(element);
			} else if (typeMgr.getTypeConverterForType(element.getClass(), byte[].class) != null) {
				rowkey = (byte[]) typeMgr.getTypeConverterForType(element.getClass(), byte[].class).toDatastoreType(element);
			} else {
				rowkey = HBaseUtils.makeRowKey(element, typeMgr).getKey();
				queue.add(new QueuedNestedEntity(element, rowkey, field.getName()));
			}
			base64Ids.add(Base64.encodeBytes(rowkey));
		}

		JSONArray jsonValue = new JSONArray(base64Ids);
		put.add(HBaseUtils.DEFAULT_COLUMN_FAMILY, current.getQualifier(field), jsonValue.toString().getBytes());

	}

	private void storeMapField(Field field, QueuedObject current, Put put) throws IllegalArgumentException, IllegalAccessException {
		List<String> base64Ids = new ArrayList<String>();
		Map map = (Map) field.get(current.getObj());
		Iterator<Map.Entry> mapIter = map.entrySet().iterator();
		while (mapIter.hasNext()) {
			Map.Entry entry = mapIter.next();
			Object mapKey = entry.getKey();
			Object mapValue = entry.getValue();
			byte[] rowkey = null;
			if (isIterable(mapKey.getClass())) {
				break;
			} else if (typeMgr.isDefaultPersistent(mapKey.getClass())) {
				rowkey = HBaseUtils.toBytes(mapKey);
			} else if (typeMgr.getTypeConverterForType(mapKey.getClass(), byte[].class) != null) {
				rowkey = (byte[]) typeMgr.getTypeConverterForType(mapKey.getClass(), byte[].class).toDatastoreType(mapKey);
			} else {
				rowkey = HBaseUtils.makeRowKey(mapKey, typeMgr).getKey();
				queue.add(new QueuedNestedEntity(mapKey, rowkey, field.getName() + "~k"));
			}
			base64Ids.add(Base64.encodeBytes(rowkey));
			queue.add(new QueuedNestedEntity(mapValue, rowkey, field.getName() + "~v"));
		}
		JSONArray jsonValue = new JSONArray(base64Ids);
		put.add(HBaseUtils.DEFAULT_COLUMN_FAMILY, current.getQualifier(field), jsonValue.toString().getBytes());

	}

	private void storeArrayField(Field field, QueuedObject current, Put put) throws IllegalArgumentException, IllegalAccessException {
		List<String> base64Ids = new ArrayList<String>();
		Object array = field.get(current.getObj());
		for (int i = 0; i < Array.getLength(array); i++) {
			Object element = Array.get(array, i);
			if (element != null) {
				byte[] rowkey = null;
				if (isIterable(element.getClass())) {
					break;
				} else if (typeMgr.isDefaultPersistent(element.getClass())) {
					rowkey = HBaseUtils.toBytes(element);
				} else if (typeMgr.getTypeConverterForType(element.getClass(), byte[].class) != null) {
					rowkey = (byte[]) typeMgr.getTypeConverterForType(element.getClass(), byte[].class).toDatastoreType(element);
				} else {
					rowkey = HBaseUtils.makeRowKey(element, typeMgr).getKey();
					queue.add(new QueuedNestedEntity(element, rowkey, field.getName()));
				}
				base64Ids.add(Base64.encodeBytes(rowkey));
			}
		}
		JSONArray jsonValue = new JSONArray(base64Ids);
		put.add(HBaseUtils.DEFAULT_COLUMN_FAMILY, current.getQualifier(field), jsonValue.toString().getBytes());

	}

	private void storeDefaultPersistentField(Field field, QueuedObject current, Put put) throws IllegalArgumentException, IllegalAccessException, IOException {
		put.add(HBaseUtils.DEFAULT_COLUMN_FAMILY, current.getQualifier(field), HBaseUtils.toBytes(field.get(current.getObj())));
	}

	private void storeConvertableField(Field field, QueuedObject current, Put put) throws IllegalArgumentException, IllegalAccessException {
		byte[] convertedValue = (byte[]) typeMgr.getTypeConverterForType(field.getType(), byte[].class).toDatastoreType(field.get(current.getObj()));
		put.add(HBaseUtils.DEFAULT_COLUMN_FAMILY, current.getQualifier(field), convertedValue);
	}

	private void storeEntityField(Field field, QueuedObject current, Put put) throws IllegalArgumentException, IllegalAccessException {
		Object entity = field.get(current.getObj());
		byte[] rowkey = HBaseUtils.makeRowKey(entity, typeMgr).getKey();
		String base64StringRowKey = Base64.encodeBytes(rowkey);
		JSONArray jsonValue = new JSONArray(Arrays.asList(base64StringRowKey));
		put.add(HBaseUtils.DEFAULT_COLUMN_FAMILY, current.getQualifier(field), jsonValue.toString().getBytes());
		queue.add(new QueuedNestedEntity(entity, rowkey, field.getName()));

	}
}
