package com.finaxys.finaxysplatform.persistence.hbase;

import java.util.TreeSet;

public class RowKeyBuilder {

	private TreeSet<RowKeyField> rowKeyFields = new TreeSet<RowKeyField>();

	private RowKeyDesignStrategy strategy;

	public RowKeyBuilder withField(RowKeyField field) {
		this.rowKeyFields.add(field);
		return this;
	}

	public RowKeyBuilder withFields(TreeSet<RowKeyField> fields) {
		this.rowKeyFields.addAll(fields);
		return this;
	}

	public RowKeyBuilder withStrategy(RowKeyDesignStrategy strategy) {
		this.strategy = strategy;
		return this;
	}

	public RowKey build() {
		if(strategy == null) strategy =  new RowKeyHashStrategy();
		byte[] createdKey = strategy.makeRowKey(rowKeyFields);
		if (createdKey == null) {
			return new RowKey(new byte[0]);
		} else {
			return new RowKey(createdKey);
		}
	}

}
