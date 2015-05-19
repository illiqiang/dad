package com.dad.service.dbrouter;

import com.dad.service.datasource.DataSourceUtil;

public class TableRouter {
	
	public static String minuteTable = "tb_minute_data%s";
	public static String hourTable = "tb_hour_data%s";
	
	public static TableEntity routerTable(String dataTime, String tableTemplet) {
		String year = dataTime.substring(0,4);
		if(DataSourceUtil.checkSupport(year)) {
			TableEntity table = new TableEntity();
			table.setDbKey(year);
			if(tableTemplet != null) {
				String month = dataTime.substring(4,6);
				table.setTableName(String.format(tableTemplet, month));
			}
			return table;
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println("2015".substring(0,4));
		System.out.println("20150612".substring(4,6));
	}
}
