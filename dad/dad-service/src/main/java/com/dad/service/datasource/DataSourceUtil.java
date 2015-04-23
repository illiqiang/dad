package com.dad.service.datasource;

import java.util.ArrayList;
import java.util.List;

public class DataSourceUtil {
	
	private final static ThreadLocal<String> local = new ThreadLocal<String>();
	
	private static List<String> supportDb = new ArrayList<String>();
	
	public static void setDataSourceKey(String dsKey) {
		local.set(dsKey);
	}

	public static String getDataSourceKey() {
		return local.get();
	}

	public static void removeDataSourceKey() {
		local.remove();
	}
	
	public static boolean checkSupport(String dbKey) {
		return supportDb.contains(dbKey);
	}
	
	public static void addSupportDb(String dbKey) {
		supportDb.add(dbKey);
	}
}
