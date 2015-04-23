package com.dad.service.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.dad.common.entity.Device;
import com.dad.common.entity.DevicePollutants;
import com.dad.common.entity.Group;

public class DeviceDaoImpl implements DeviceDao {

	public static Logger log = LoggerFactory.getLogger(DeviceDaoImpl.class);

	private JdbcTemplate baseTemplate;

	private static final String getDevicePltListSql = "SELECT d.device_id,d.data_code,d.warn_min,d.warn_max,p.data_name,p.data_unit FROM tb_device_pollutants d LEFT JOIN tb_pollutants p ON d.data_code= p.data_code WHERE device_id=?";

	private static final String getDevicePltSql = "SELECT  device_id, data_code, warn_min, warn_max FROM tb_device_pollutants WHERE device_id=? AND data_code=?";
	
	private static final String saveDevicePltSql = "INSERT IGNORE INTO tb_device_pollutants(device_id,data_code,warn_min,warn_max) VALUES(?,?,?,?)";

	private static final String getDeviceSql = "select device_id, st, pw from tb_device where device_id = ?";

	private static final String updateDeviceSql = "update tb_device set st = ? ,pw=? where device_id = ?";

	private static final String updateDeviceOnlineSql = "update tb_device_status set online = ?,last_update_time=? where device_id = ?";
	private static final String insertDeviceOnlineSql = "INSERT IGNORE INTO tb_device_status(device_id,online) VALUES(?,?)";

	private static final String getGroupsByUserSql = "SELECT u.group_id, g.group_name,(SELECT COUNT(1) FROM tb_group_device d WHERE d.group_id=u.group_id) FROM tb_user_group u LEFT JOIN tb_group g ON u.group_id=g.group_id WHERE u.user_id = ?";

	private static final String getDeviceByGroupSql = "SELECT d.device_id,d.st FROM tb_group_device g LEFT JOIN tb_device d ON g.device_id=d.device_id WHERE g.group_id =?";
	
	private static final String getDeviceUserIdSql = "SELECT u.user_id FROM tb_group_device d INNER JOIN tb_user_group u ON d.group_id=u.group_id WHERE device_id=?";
	
	private static final String getOnlineDeviceIdSql = "SELECT device_id FROM `tb_device_status` WHERE online =1";
	
	public void setBaseTemplate(JdbcTemplate baseTemplate) {
		this.baseTemplate = baseTemplate;
	}
	
	@Override
	public List<DevicePollutants> getDevicePollutants(String deviceId) {

		List<DevicePollutants> plts = baseTemplate.query(getDevicePltListSql,
				new Object[] { deviceId }, new RowMapper<DevicePollutants>() {
					@Override
					public DevicePollutants mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						DevicePollutants dlt = new DevicePollutants();
						dlt.setDeviceId(rs.getString(1));
						dlt.setDataCode(rs.getString(2));
						Object v3 = rs.getObject(3);
						if (v3 != null) {
							dlt.setWarnMin((Double) v3);
						}
						Object v4 = rs.getObject(4);
						if (v4 != null) {
							dlt.setWarnMax((Double) v4);
						}
						dlt.setDataName(rs.getString(5));
						dlt.setDataUnit(rs.getString(6));
						return dlt;
					}

				});
		return plts;
	}

	@Override
	public void saveDevicePollutants(final List<DevicePollutants> pollutants) {
		baseTemplate.batchUpdate(saveDevicePltSql,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						DevicePollutants plt = pollutants.get(i);
						ps.setString(1, plt.getDeviceId());
						ps.setString(2, plt.getDataCode());
						if (plt.getWarnMin() == null) {
							ps.setNull(3, Types.DOUBLE);
						} else {
							ps.setDouble(3, plt.getWarnMin());
						}

						if (plt.getWarnMax() == null) {
							ps.setNull(4, Types.DOUBLE);
						} else {
							ps.setDouble(4, plt.getWarnMax());
						}
					}

					@Override
					public int getBatchSize() {
						return pollutants.size();
					}
				});
	}
	
	@Override
	public DevicePollutants getDevicePollutants(String deviceId, String dataCode) {
		DevicePollutants plt = baseTemplate.query(getDevicePltSql, new Object[]{deviceId, dataCode},new ResultSetExtractor<DevicePollutants>() {

			@Override
			public DevicePollutants extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				DevicePollutants d = null;
				if (rs.next()) {
					d = new DevicePollutants();
					d.setDeviceId(rs.getString(1));
					d.setDataCode(rs.getString(2));
					Object v3 = rs.getObject(3);
					if(v3 != null) {
						d.setWarnMin((double)v3);
					}
					Object v4 = rs.getObject(4);
					if(v4 != null) {
						d.setWarnMax((double)v4);
					}
				}
				return d;
			}});
		return plt;
	}

	@Override
	public Device getDevice(String deviceId) {

		Device device = baseTemplate.query(getDeviceSql,
				new Object[] { deviceId }, new ResultSetExtractor<Device>() {

					@Override
					public Device extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						Device d = null;
						if (rs.next()) {
							d = new Device();
							d.setDeviceId(rs.getString(1));
							d.setSt(rs.getString(2));
							d.setPw(rs.getString(3));
						}
						return d;
					}
				});

		return device;
	}

	@Override
	public void updateDevice(final Device device) {
		baseTemplate.update(updateDeviceSql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, device.getSt());
				ps.setString(2, device.getPw());
				ps.setString(3, device.getDeviceId());
			}
		});
	}

	@Override
	public void insertOrUpdateDeviceOnline(final String deviceId, final boolean isOnline) {
		int count = baseTemplate.update(updateDeviceOnlineSql,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setBoolean(1, isOnline);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));;
						ps.setString(3, deviceId);
					}
				});
		
		if(count == 0) {
			baseTemplate.update(insertDeviceOnlineSql,
					new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps)
						throws SQLException {
					ps.setString(1, deviceId);
					ps.setBoolean(2, isOnline);
				}
			});
		}
	}

	@Override
	public List<Group> getGroupsByUser(long userId) {
		List<Group> groups = baseTemplate.query(getGroupsByUserSql,
				new Object[] { userId }, new RowMapper<Group>() {
					@Override
					public Group mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Group g = new Group();
						g.setGroupId(rs.getLong(1));
						g.setGroupName(rs.getString(2));
						g.setDeviceSize(rs.getInt(3));
						return g;
					}
				});
		return groups;
	}

	@Override
	public List<Device> getDeviceByGroup(long groupId) {
		return baseTemplate.query(getDeviceByGroupSql, new Object[] { groupId },
				new RowMapper<Device>() {

					@Override
					public Device mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Device d = new Device();
						d.setDeviceId(rs.getString(1));
						d.setSt(rs.getString(2));
						return d;
					}
				});
	}

	@Override
	public void updatePollutantWarn(String deviceId, String dataCode,
			Double min, Double max) {
		StringBuilder sql = new StringBuilder("update tb_device_pollutants set ");
		Object[] args = null;
		if(min != null && max != null) {
			sql.append("warn_min=? ,warn_max=?");
			args = new Object[]{min,max,deviceId,dataCode};
		} else if(min != null && max ==null) {
			sql.append("warn_min=?");
			args = new Object[]{min,deviceId,dataCode};
		} else {
			sql.append("warn_max=?");
			args = new Object[]{max,deviceId,dataCode};
		}
		sql.append(" where device_id=? and data_code=?");
		baseTemplate.update(sql.toString(), args);
		
	}


	@Override
	public List<Long> getDeviceUserIds(String deviceId) {
		
		List<Long>  result = baseTemplate.query(getDeviceUserIdSql, new Object[]{deviceId}, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong(1);
			}
		});
		return result;
	}

	@Override
	public List<String> getOnlineDeviceIds() {
		return baseTemplate.query(getOnlineDeviceIdSql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}
	
	
	private static final String getGroupSql = "SELECT group_id, group_name FROM  tb_group";
	private static final String getGroupPagesSql = "SELECT group_id, group_name FROM  tb_group limit ?,?";
	private static final String getGroupSizeSql = "SELECT count(1) FROM  tb_group";
	private static final String getGroupByNameSql = "SELECT group_id, group_name FROM  tb_group where group_name=?";
	private static final String saveUserGroupSql = "INSERT IGNORE INTO tb_user_group(user_id,group_id) VALUES(?,?)";
	private static final String deleteUserGroupSql = "DELETE FROM tb_user_group WHERE user_id=? AND group_id=?"; 
	private static final String getGroupUserIdSql = "select user_id from tb_user_group where group_id=?";

	@Override
	public List<Group> getGroups() {
		return baseTemplate.query(getGroupSql, new RowMapper<Group>() {

			@Override
			public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
				Group g = new Group();
				g.setGroupId(rs.getLong(1));
				g.setGroupName(rs.getString(2));
				return g;
			}
			
		});
	}

	@Override
	public void saveUserGroups(final List<Long> groupIds, final long userId) {
		baseTemplate.batchUpdate(saveUserGroupSql,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setLong(1, userId);;
						ps.setLong(2, groupIds.get(i));
					}

					@Override
					public int getBatchSize() {
						return groupIds.size();
					}
				});
	}

	@Override
	public void deleteUserGroup(long userId, long groupId) {
		baseTemplate.update(deleteUserGroupSql, new Object[]{userId, groupId});
	}

	@Override
	public List<Group> getGroupByPage(int first, int pages) {
		return baseTemplate.query(getGroupPagesSql, new Object[]{first,pages},  new RowMapper<Group>() {

			@Override
			public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
				Group g = new Group();
				g.setGroupId(rs.getLong(1));
				g.setGroupName(rs.getString(2));
				return g;
			}
			
		});
		
	}

	@Override
	public Group getGroupByName(String name) {
		return baseTemplate.query(getGroupByNameSql,
				new Object[] { name }, new ResultSetExtractor<Group>() {

					@Override
					public Group extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						Group g = null;
						if (rs.next()) {
							g = new Group();
							g.setGroupId(rs.getLong(1));
							g.setGroupName(rs.getString(2));
						}
						return g;
					}
				});

	}

	@Override
	public int getGroupSize() {
		return baseTemplate.queryForObject(getGroupSizeSql, Integer.class);
	}

	@Override
	public List<Long> getGroupUserIds(long groupId) throws Exception {
		return baseTemplate.queryForList(getGroupUserIdSql, Long.class, new Object[]{groupId});
	}

	private final static String addGroupSql = "insert into tb_group(group_name) values(?)";
	
	@Override
	public void addGroup(Group g) throws Exception {
		baseTemplate.update(addGroupSql, new Object[]{g.getGroupName()});
	}
	
	private final static String deleteGroupSql = "delete from tb_group where group_id=?";
	
	@Override
	public void deleteGroup(long groupId) throws Exception {
		baseTemplate.update(deleteGroupSql,new Object[]{groupId});
	}

	private final static String getDeviceIdList = "select device_id from tb_device";
	@Override
	public List<String> getDeviceIdList() throws Exception {
		return baseTemplate.queryForList(getDeviceIdList, String.class);
	}

	private final static String saveGroupDeviceSql = "INSERT IGNORE INTO tb_group_device(group_id,device_id) values(?,?)";
	@Override
	public void saveGroupDevices(final long groupId,final List<String> devices)
			throws Exception {
		baseTemplate.batchUpdate(saveGroupDeviceSql,new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setLong(1, groupId);;
				ps.setString(2, devices.get(i));
			}

			@Override
			public int getBatchSize() {
				return devices.size();
			}
		});
	}

	private final static String deleteGroupDeviceSql = "delete from tb_group_device where group_id =? and device_id=?";
	@Override
	public void deleteGroupDevice(long groupId, String deviceId)
			throws Exception {
		baseTemplate.update(deleteGroupDeviceSql, new Object[]{groupId, deviceId});
	}

	private final static String getDeviceByPageSql = "select device_id,st,pw from tb_device limit ?,?";
	@Override
	public List<Device> getDeviceByPage(int first, int pages) throws Exception {
		return baseTemplate.query(getDeviceByPageSql, new Object[]{first, pages}, new RowMapper<Device>() {

			@Override
			public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
				Device d = new Device();
				d.setDeviceId(rs.getString(1));
				d.setSt(rs.getString(2));
				d.setPw(rs.getString(3));
				return d;
			}
		});
	}

	private final static String getDeviceSizeSql = "select count(*) from tb_device";
	@Override
	public int getDeviceSize() throws Exception {
		return baseTemplate.queryForObject(getDeviceSizeSql, Integer.class);
	}

	private final static String addDeviceSql = "insert into tb_device(device_id) values(?)";
	@Override
	public void addDevice(Device d) throws Exception {
		baseTemplate.update(addDeviceSql, new Object[]{d.getDeviceId()});
	}

	private final static String getGroupIdByDeviceSql = "select group_id from tb_group_device where device_id=?";
	@Override
	public List<Long> getGroupIdByDevice(String deviceId) throws Exception {
		return baseTemplate.queryForList(getGroupIdByDeviceSql, Long.class, new Object[]{deviceId});
	}

	private final static String deleteDeviceSql = "delete from tb_device where device_id=?";
	@Override
	public void deleteDevice(String deviceId) throws Exception {
		baseTemplate.update(deleteDeviceSql, new Object[]{deviceId});
	}

}
