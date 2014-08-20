/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.finaxys.finaxysplatform.persistence.hbase.RowKeyDesignStrategy;
import com.finaxys.finaxysplatform.persistence.hbase.RowKeyHashStrategy;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HBaseRowKey {

	String[] fields();
	Class<? extends RowKeyDesignStrategy> strategy() default RowKeyHashStrategy.class;
}
