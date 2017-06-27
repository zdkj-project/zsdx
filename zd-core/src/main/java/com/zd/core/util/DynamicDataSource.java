package com.zd.core.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 得到DataSource
 * @author huangzc
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
	

	@Override
	protected Object determineCurrentLookupKey() {
		return DBContextHolder.getDBType();
	}

}
