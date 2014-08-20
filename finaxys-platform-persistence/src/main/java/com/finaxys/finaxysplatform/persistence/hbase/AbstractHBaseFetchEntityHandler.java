package com.finaxys.finaxysplatform.persistence.hbase;

import org.apache.hadoop.hbase.client.Result;

import com.finaxys.finaxysplatform.persistence.hbase.exception.HBaseIOException;

public abstract class AbstractHBaseFetchEntityHandler {
	protected abstract Object fetch(String tableName, Result result) throws HBaseIOException;
}
