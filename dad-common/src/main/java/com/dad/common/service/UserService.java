package com.dad.common.service;

import java.util.List;
import java.util.Map;

import com.dad.common.entity.User;

public interface UserService {
	public User login(String userName, String password) throws Exception;
	public void updatePassword(long userId, String oldPass, String newPass) throws Exception;
	public void setUserOnline(long userId, boolean online) throws Exception;
	
	public List<Long> onlineUserIds() throws Exception;
	
	public void setSession(String session, Map<String, Object> attrs, int timeToLiveSeconds) throws Exception;
	public Map<String, Object> getSession(String session) throws Exception;
	public void removeSession(String session) throws Exception;
	public boolean exitsSession(String session) throws Exception;
	
	//manage
	public User getUserByUserName(String userName) throws Exception;
	public void addUser(User user) throws Exception;
	public void updateUser(long userId, String password, boolean status) throws Exception;
	public List<User> getUsers(int first, int pages) throws Exception;
	public int getUserSize() throws Exception;
	public void deleteUser(long userId) throws Exception;
}
