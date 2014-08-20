package com.finaxys.finaxysplatform.persistence.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang.NotImplementedException;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.log4j.Logger;
import org.datanucleus.store.types.TypeManager;
import org.springframework.util.Assert;

import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseIOException;

public class HBasePersistenceManager extends AbstractHBasePersistenceManager {

	private static Logger logger = Logger.getLogger(HBasePersistenceManager.class);

	HConnection connection;
	TypeManager typeMgr;
	HBaseStoreEntityHandler seh;
	HBaseFetchEntityHandler feh;


	public HBasePersistenceManager(HBaseConfiguration configuration, TypeManager typeMgr) throws ZooKeeperConnectionException {
		super();
		this.connection = HConnectionManager.createConnection(configuration);
		this.typeMgr = typeMgr;
		seh = new HBaseStoreEntityHandler(typeMgr);
		feh = new HBaseFetchEntityHandler(typeMgr);
	}

	public HBasePersistenceManager(TypeManager typeMgr) throws ZooKeeperConnectionException {
		super();
		this.typeMgr = typeMgr;
		this.connection = HConnectionManager.createConnection(HBaseConfiguration.create());
		seh = new HBaseStoreEntityHandler(typeMgr);
		feh = new HBaseFetchEntityHandler(typeMgr);
	}

	private void executeAdd(byte[] tableName, Put put) throws HBaseIOException {

		HTableInterface table = null;
		try {
			table = connection.getTable(tableName);
			table.put(put);
		} catch (IOException e) {
			throw new HBaseIOException(e);
		} finally {
			if (table != null)
				try {
					table.close();
				} catch (IOException e) {
					logger.error("Exception when releasing hbase table access.");
				}
		}
	}
	
	private List<Object> executeScan(byte[] tableName, Scan scan) throws HBaseIOException {

		HTableInterface table = null;
		try {
			table = connection.getTable(tableName);
			ResultScanner results = table.getScanner(scan);
			List<Object> list = new ArrayList<Object>();
			Object entity = null;
			Iterator<Result> iterator = results.iterator();
			Result result = null;
			while (iterator.hasNext()) {
				result = iterator.next();
				entity = feh.fetch( new String(tableName), result);
				if (entity != null)
					list.add(entity);
			}
			return list;
		} catch (IOException e) {
			throw new HBaseIOException(e);
		} finally {
			if (table != null)
				try {
					table.close();
				} catch (IOException e) {
					logger.error("Exception when releasing hbase table access.");
				}
		}
	}


	@Override
	public void persist(Object entity) {
		Assert.notNull(entity, "Cannot persist null object");
		Put put = HBaseUtils.createPut(entity, typeMgr);
		seh.store(entity, put);
		executeAdd(entity.getClass().getSimpleName().getBytes(), put);
	}

	@Override
	public <T> T merge(T entity) {
		throw new NotImplementedException();
	}

	@Override
	public void remove(Object entity) {
		throw new NotImplementedException();
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		throw new NotImplementedException();
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		throw new NotImplementedException();
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		throw new NotImplementedException();
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		throw new NotImplementedException();
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		throw new NotImplementedException();
	}

	@Override
	public void flush() {
		throw new NotImplementedException();

	}

	@Override
	public void setFlushMode(FlushModeType flushMode) {
		throw new NotImplementedException();

	}

	@Override
	public FlushModeType getFlushMode() {
		throw new NotImplementedException();
	}

	@Override
	public void lock(Object entity, LockModeType lockMode) {
		throw new NotImplementedException();

	}

	@Override
	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		throw new NotImplementedException();

	}

	@Override
	public void refresh(Object entity) {
		throw new NotImplementedException();

	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		throw new NotImplementedException();

	}

	@Override
	public void refresh(Object entity, LockModeType lockMode) {
		throw new NotImplementedException();

	}

	@Override
	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		throw new NotImplementedException();

	}

	@Override
	public void clear() {
		throw new NotImplementedException();

	}

	@Override
	public void detach(Object entity) {
		throw new NotImplementedException();

	}

	@Override
	public boolean contains(Object entity) {
		throw new NotImplementedException();
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		throw new NotImplementedException();
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		throw new NotImplementedException();

	}

	@Override
	public Map<String, Object> getProperties() {
		throw new NotImplementedException();
	}

	@Override
	public Query createQuery(String qlString) {
		throw new NotImplementedException();
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		throw new NotImplementedException();
	}

	@Override
	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		throw new NotImplementedException();
	}

	@Override
	public Query createNamedQuery(String name) {
		throw new NotImplementedException();
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		throw new NotImplementedException();
	}

	@Override
	public Query createNativeQuery(String sqlString) {
		throw new NotImplementedException();
	}

	@Override
	public Query createNativeQuery(String sqlString, Class resultClass) {
		throw new NotImplementedException();
	}

	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		throw new NotImplementedException();
	}

	@Override
	public void joinTransaction() {
		throw new NotImplementedException();
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		throw new NotImplementedException();
	}

	@Override
	public Object getDelegate() {
		throw new NotImplementedException();
	}

	@Override
	public void close() {
		throw new NotImplementedException();

	}

	@Override
	public boolean isOpen() {
		throw new NotImplementedException();
	}

	@Override
	public EntityTransaction getTransaction() {
		throw new NotImplementedException();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		throw new NotImplementedException();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		throw new NotImplementedException();
	}

	@Override
	public Metamodel getMetamodel() {
		throw new NotImplementedException();
	}

}
