package com.dad.service.dao;

import java.util.List;

import com.dad.common.entity.User;

public interface UserDao {
	public User getUserByUserName(String userName);
	public User getUserByUserId(long userId);
	public void updateUserPassword(long userId, String password);
	
	public void insertOrUpdateUserStatus(long userId, boolean online);
	public List<Long> getOnlineUser();
	
	// manage
	public void addUser(User user) throws Exception;
	public void updateUser(long userId, String password, boolean status) throws Exception;
	public List<User> getUsers(int first, int pages) throws Exception;
	public int getUserSize() throws Exception;
	public void deleteUser(long userId) throws Exception;
}
