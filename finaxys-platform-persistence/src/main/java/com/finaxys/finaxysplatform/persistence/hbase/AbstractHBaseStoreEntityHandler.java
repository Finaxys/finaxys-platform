package com.finaxys.finaxysplatform.persistence.hbase;

import org.apache.hadoop.hbase.client.Put;

import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseIOException;

public abstract class AbstractHBaseStoreEntityHandler {

	protected abstract void store(Object entity, Put put) throws HBaseIOException; 
}
