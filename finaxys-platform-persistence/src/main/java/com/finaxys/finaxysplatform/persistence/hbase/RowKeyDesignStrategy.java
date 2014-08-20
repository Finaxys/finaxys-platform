/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase;

import java.util.TreeSet;


public interface RowKeyDesignStrategy {

	public byte[] makeRowKey(TreeSet<RowKeyField> keyFields);

}
