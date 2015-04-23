package primefaces.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import primefaces.common.Util;
import primefaces.common.page.PageBean;

import com.dad.common.entity.Group;
import com.dad.common.entity.User;
import com.dad.common.service.DadService;
import com.dad.common.service.UserService;

@Service
public class UserAction {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DadService dadService;
	
	public List<String> getList() throws Exception {
		List<Group> grps = dadService.getGroups();
		List<String> ls = new ArrayList<>();
		for(Group g : grps) {
			ls.add(String.format("%s(%s)", g.getGroupName(),g.getGroupId()));
		}
		return ls;
	}
	
	public List<Group> getUserGroup(Long userId) throws Exception {
		return dadService.getGroupsByUser(userId);
	}
	
	public void saveUserGroups(Long userId, List<Long> groupIds) throws Exception {
		dadService.saveUserGroups(groupIds, userId);
	}
	
	public void deleteUserGroup(long userId, long groupId) throws Exception {
		dadService.deleteUserGroup(userId, groupId);
	}
	
	public void delete(long userId) throws Exception {
		userService.deleteUser(userId);
	}
	
	public boolean checkGroupsEmpty(long userId)  throws Exception {
		return CollectionUtils.isEmpty(dadService.getGroupsByUser(userId));
	}
	
	public void addUsers(User u) throws Exception {
		u.setPassWord(Util.getMd5(u.getPassWord()));
		u.setStatus(true);
		userService.addUser(u);
	}
	
	public void updateUser(User u) throws Exception {
		userService.updateUser(u.getUserId(), Util.getMd5(u.getPassWord()), u.isStatus());
	}
	
	public PageBean<User> getByPage(int first, int pageSize, String userName) throws Exception {
		PageBean<User> pages = new PageBean<User>();
		int size = 0;
		List<User> users = new ArrayList<>();
		if(StringUtils.isBlank(userName)) {
			users = userService.getUsers(first, pageSize);
			if(CollectionUtils.isNotEmpty(users)) {
				size = userService.getUserSize();
			}
		} else {
			
			User u = userService.getUserByUserName(userName);
			if(u != null) {
				users.add(u);
				size = 1;
			}
		}
		
		pages.setDatas(users);
		pages.setTotalSize(size);
		return pages;
	}
	
}
