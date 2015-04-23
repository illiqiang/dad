package com.dad.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.common.entity.User;
import com.dad.common.service.CacheService;
import com.dad.common.service.UserService;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.service.exception.ErrorConstant;
import com.dad.common.service.exception.ManageConstant;
import com.dad.service.dao.UserDao;
import com.dad.service.util.CacheKeyUtil;

public class UserServiceImpl implements UserService {
	
	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserDao userDao;
	private CacheService userCacheService;
	
	@Override
	public User login(String userName, String password) throws Exception {
		if(log.isDebugEnabled()) {
			log.debug("login({},{})", userName, password);
		}
		User user = userDao.getUserByUserName(userName);
		if(user == null) {
			throw new BusinessServiceException(ErrorConstant.USER_NOT_EXISTS, userName);
		} else if (!password.equalsIgnoreCase(user.getPassWord())) {
			throw new BusinessServiceException(ErrorConstant.PASSWORD_IS_INCORRECT);
		} else if(!user.isStatus()) {
			throw new BusinessServiceException(ErrorConstant.USER_IS_LOCKED, userName);
		}
		return user;
	}

	@Override
	public void updatePassword(long userId, String oldPass, String newPass) throws Exception {
		if(log.isDebugEnabled()) {
			log.debug("updatePassword({},{},{})", userId, oldPass, newPass );
		}
		User user = userDao.getUserByUserId(userId);
		if(user == null) {
			throw new BusinessServiceException(ErrorConstant.USER_NOT_EXISTS);
		} else if(!oldPass.equalsIgnoreCase(user.getPassWord())) {
			throw new BusinessServiceException(ErrorConstant.OLDPASS_IS_INCORRECT);
		} else {
			userDao.updateUserPassword(userId, newPass);
		}
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserCacheService(CacheService userCacheService) {
		this.userCacheService = userCacheService;
	}

	@Override
	public void setSession(String session, Map<String, Object> attrs,
			int timeToLiveSeconds) throws Exception {
		userCacheService.set(CacheKeyUtil.getSessionKey(session), attrs, timeToLiveSeconds);
	}

	@Override
	public Map<String, Object> getSession(String session)
			throws Exception {
		return userCacheService.get(CacheKeyUtil.getSessionKey(session));
	}

	@Override
	public void removeSession(String session) throws Exception {
		userCacheService.remove(CacheKeyUtil.getSessionKey(session));
	}

	@Override
	public boolean exitsSession(String session) throws Exception {
		return userCacheService.isKeyInCache(CacheKeyUtil.getSessionKey(session));
	}

	@Override
	public void setUserOnline(long userId, boolean online) throws Exception {
		userDao.insertOrUpdateUserStatus(userId, online);
	}

	@Override
	public List<Long> onlineUserIds() throws Exception {
		return userDao.getOnlineUser();
	}

	/**
	 * manage start
	 */
	
	
	@Override
	public void addUser(User user) throws Exception {
		User olduser = userDao.getUserByUserName(user.getUserName());
		if(olduser != null) {
			throw new BusinessServiceException(ManageConstant.USER_EXISTS);
		}
		userDao.addUser(user);
	}

	@Override
	public void updateUser(long userId, String password, boolean status)
			throws Exception {
		userDao.updateUser(userId, password, status);
	}

	@Override
	public List<User> getUsers(int first, int pages)
			throws Exception {
		return userDao.getUsers(first, pages);
	}

	@Override
	public int getUserSize() throws Exception {
		return userDao.getUserSize();
	}

	@Override
	public void deleteUser(long userId) throws Exception {
		userDao.deleteUser(userId);
	}

	@Override
	public User getUserByUserName(String userName) throws Exception {
		return userDao.getUserByUserName(userName);
	}

	/**
	 * manage end
	 */
}
