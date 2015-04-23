package com.dad.service.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.dad.common.entity.User;

public class UserDaoImpl implements UserDao {

	private JdbcTemplate baseTemplate;

	private static final String getUserByUserNameSql = "select user_id,user_name,pass_word,status from tb_user where user_name=?";
	private static final String getUserByUserIdSql = "select user_id,user_name,pass_word,status from tb_user where user_id=?";
	private static final String updateUserPasswordSql = "update tb_user set pass_word =? where user_id=?";
	private static final String insertUserStatusSql = "INSERT IGNORE INTO tb_user_status(user_id,online) VALUES(?,?)";
	private static final String updateUserStatusSql = "update tb_user_status set online=? , last_update_time=? where user_id=?";
	private static final String getOnlineUserIdSql = "SELECT user_id FROM tb_user_status WHERE online=1";
	
	@Override
	public User getUserByUserName(String userName) {
		User user = baseTemplate.query(getUserByUserNameSql,
				new Object[] { userName }, new ResultSetExtractor<User>() {

					@Override
					public User extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						User user = null;
						if (rs.next()) {
							user = new User();
							user.setUserId(rs.getLong(1));
							user.setUserName(rs.getString(2));
							user.setPassWord(rs.getString(3));
							user.setStatus(rs.getBoolean(4));
						}
						return user;
					}
				});

		return user;
	}

	@Override
	public void updateUserPassword(long userId, String password) {
		baseTemplate.update(updateUserPasswordSql, new Object[] { password,
				userId });
	}

	public void setBaseTemplate(JdbcTemplate baseTemplate) {
		this.baseTemplate = baseTemplate;
	}

	@Override
	public User getUserByUserId(long userId) {
		User user = baseTemplate.query(getUserByUserIdSql,
				new Object[] { userId }, new ResultSetExtractor<User>() {

					@Override
					public User extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						User user = null;
						if (rs.next()) {
							user = new User();
							user.setUserId(rs.getLong(1));
							user.setUserName(rs.getString(2));
							user.setPassWord(rs.getString(3));
							user.setStatus(rs.getBoolean(4));
						}
						return user;
					}
				});
		return user;
	}

	@Override
	public void insertOrUpdateUserStatus(final long userId, final boolean online) {
		int count = baseTemplate.update(updateUserStatusSql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setBoolean(1, online);
				ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				ps.setLong(3, userId);
			}
		});
		
		if(count == 0) {
			baseTemplate.update(insertUserStatusSql, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, userId);
					ps.setBoolean(2, online);
				}
			});
		}
	}

	@Override
	public List<Long> getOnlineUser() {
		return baseTemplate.query(getOnlineUserIdSql, new RowMapper<Long>(){

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong(1);
			}});
	}

	private static final String addUserSql = "INSERT INTO tb_user(user_name,pass_word,status) VALUES(?,?,?)";
	private static final String updateUserSql = "UPDATE tb_user SET pass_word=?,status=? WHERE user_id = ?";
	private static final String getUsersSql = "SELECT user_id,user_name,pass_word,status FROM tb_user LIMIT ?,?";
	private static final String getUserSizeSql = "SELECT COUNT(*) FROM tb_user ";
	private static final String deleteUserSql = "DELETE FROM tb_user WHERE user_id=? ";
	@Override
	public void addUser(final User user) throws Exception {
		baseTemplate.update(addUserSql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUserName());
				ps.setString(2, user.getPassWord());
				ps.setBoolean(3, user.isStatus());
			}
		});
		
	}

	@Override
	public void updateUser(final long userId, final String password, final boolean status)
			throws Exception {
		baseTemplate.update(updateUserSql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, password);
				ps.setBoolean(2, status);
				ps.setLong(3, userId);
			}
		});
	}

	@Override
	public List<User> getUsers(int first, int pages)
			throws Exception {
		return baseTemplate.query(getUsersSql, new Object[]{first, pages}, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setUserId(rs.getLong(1));
				user.setUserName(rs.getString(2));
				user.setPassWord(rs.getString(3));
				user.setStatus(rs.getBoolean(4));
				return user;
			}
		});
	}

	@Override
	public int getUserSize() throws Exception {
		return baseTemplate.queryForObject(getUserSizeSql, Integer.class);
	}

	@Override
	public void deleteUser(long userId) throws Exception {
		baseTemplate.update(deleteUserSql, userId);
	}

}
