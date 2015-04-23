package com.dad.service.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.dad.common.entity.PollutantsCountData;
import com.dad.service.datasource.DataSourceUtil;
import com.dad.service.dbrouter.TableEntity;
import com.dad.service.dbrouter.TableRouter;

public class DayDataDaoImpl implements DayDataDao {

	public static Logger unknowdblog = LoggerFactory.getLogger("unknowdb");
	public static Logger log = LoggerFactory.getLogger(DayDataDaoImpl.class);

	private JdbcTemplate dynamicTemplate;

	private static String insertSql = "INSERT IGNORE INTO tb_day_data(device_id,data_code,data_time,cou,min,max,avg,zs_min,zs_max,zs_avg) values(?,?,?,?,?,?,?,?,?,?)";

	private static String getDayDataSql = "SELECT device_id,data_code,data_time,cou,min,max,avg,zs_min,zs_max,zs_avg FROM tb_day_data WHERE device_id=? AND data_code=? AND data_time BETWEEN ? AND ?";
	
	@Override
	public void saveDayDatas(final List<PollutantsCountData> counts,
			String dataTime) {
		TableEntity table = TableRouter.routerTable(dataTime, null);
		if (table == null) {
			unknowdblog.info("saveHourDatas({})", counts);
			log.error("{} not have db router!", dataTime);
			return;
		}

		DataSourceUtil.setDataSourceKey(table.getDbKey());
		dynamicTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				PollutantsCountData data = counts.get(i);
				ps.setString(1, data.getDeviceId());
				ps.setString(2, data.getCode());
				ps.setTimestamp(3, new Timestamp(data.getDataTime().getTime()));
				if (data.getCou() == null) {
					ps.setNull(4, Types.DOUBLE);
				} else {
					ps.setDouble(4, data.getCou());
				}

				if (data.getMin() == null) {
					ps.setNull(5, Types.DOUBLE);
				} else {
					ps.setDouble(5, data.getMin());
				}

				if (data.getMax() == null) {
					ps.setNull(6, Types.DOUBLE);
				} else {
					ps.setDouble(6, data.getMax());
				}

				if (data.getAvg() == null) {
					ps.setNull(7, Types.DOUBLE);
				} else {
					ps.setDouble(7, data.getAvg());
				}

				if (data.getZsMin() == null) {
					ps.setNull(8, Types.DOUBLE);
				} else {
					ps.setDouble(8, data.getZsMin());
				}

				if (data.getZsMax() == null) {
					ps.setNull(9, Types.DOUBLE);
				} else {
					ps.setDouble(9, data.getZsMax());
				}

				if (data.getZsAvg() == null) {
					ps.setNull(10, Types.DOUBLE);
				} else {
					ps.setDouble(10, data.getZsAvg());
				}
			}

			@Override
			public int getBatchSize() {
				return counts.size();
			}
		});
	}
	
	public List<PollutantsCountData> getDayDatas(final String deviceId,
			final String dataCode, final Date start, final Date end,
			String monthTime) {
		TableEntity table = TableRouter.routerTable(monthTime, null);
		if (table == null) {
			log.error("{} no table router!", monthTime);
			return null;
		}
		DataSourceUtil.setDataSourceKey(table.getDbKey());
		return dynamicTemplate.query(getDayDataSql,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, deviceId);
						ps.setString(2, dataCode);
						ps.setTimestamp(3, new Timestamp(start.getTime()));
						ps.setTimestamp(4, new Timestamp(end.getTime()));
					}
				}, new RowMapper<PollutantsCountData>() {
					@Override
					public PollutantsCountData mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						PollutantsCountData plt = new PollutantsCountData();
						plt.setDeviceId(rs.getString(1));
						plt.setCode(rs.getString(2));
						plt.setDataTime(rs.getTimestamp(3));
						Object v4 = rs.getObject(4);
						if (v4 != null) {
							plt.setCou((Double) v4);
						}
						Object v5 = rs.getObject(5);
						if (v5 != null) {
							plt.setMin((Double) v5);
						}

						Object v6 = rs.getObject(6);
						if (v6 != null) {
							plt.setMax((Double) v6);
						}
						Object v7 = rs.getObject(7);
						if (v7 != null) {
							plt.setAvg((Double) v7);
						}
						Object v8 = rs.getObject(8);
						if (v8 != null) {
							plt.setZsMin((Double)v8);
						}
						Object v9 = rs.getObject(9);
						if (v9 != null) {
							plt.setZsMax((Double)v9);
						}
						Object v10 = rs.getObject(10);
						if (v10 != null) {
							plt.setZsAvg((Double)v10);
						}
						return plt;
					}

				});
	}


	public void setDynamicTemplate(JdbcTemplate dynamicTemplate) {
		this.dynamicTemplate = dynamicTemplate;
	}

}
