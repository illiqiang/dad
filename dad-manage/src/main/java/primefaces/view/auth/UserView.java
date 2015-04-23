package primefaces.view.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import primefaces.common.page.PageBean;
import primefaces.common.page.PageDataModel;
import primefaces.common.page.Sort;
import primefaces.service.UserAction;
import primefaces.view.BasicView;

import com.dad.common.entity.Group;
import com.dad.common.entity.User;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.service.exception.ManageConstant;

@ManagedBean
@ViewScoped
public class UserView extends BasicView {

	private final static Logger log = LoggerFactory.getLogger(UserView.class);

	@ManagedProperty(value = "#{userAction}")
	private UserAction userAction;

	private LazyDataModel<User> userPages;

	private User selectedUser;

	private User addUser = new User();
	
	private List<String> groupList;
	
	private List<String> selecteGroups;
	
	private List<Group> userGroups;
	
	private Group selectedGroup;
	
	@PostConstruct
	public void init() {
		try {
			groupList = userAction.getList();
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
		}
		userPages = new PageDataModel<User>() {

			private static final long serialVersionUID = 4497840920037869584L;

			@Override
			public Object getRowKey(User object) {
				return object.getUserId();
			}

			@Override
			public PageBean<User> getPageResult(int first, int pageSize,
					List<Sort> multiSortMeta, Map<String, Object> filters) {
				String userName = (String) filters.get("userName");
				PageBean<User> users = new PageBean<User>();
				try {
					users = userAction.getByPage(first, pageSize, userName);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return users;
			}
		};
	}

	public void addUser() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean isadd = false;
		if (StringUtils.isNotEmpty(addUser.getUserName())
				&& StringUtils.isNotEmpty(addUser.getPassWord())) {
			
			try {
				userAction.addUsers(addUser);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "新增成功",
						String.format("用户[%s] 已经成功添加", addUser.getUserName()));
				isadd = true;
			} catch (BusinessServiceException e) {
				isadd = false;
				if(ManageConstant.USER_EXISTS.equals(e.getErrorCode())) {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"新增失败", "该用户已经存在。");
				}
			} catch (Exception e) {
				isadd = false;
				log.error(e.getMessage(), e);
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"新增失败", "服务器出现了异常，本次操作失败了。");
			}
			
		} else {
			isadd = false;
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "新增 失败",
					"用户名或者密码不能为空");
		}

		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("isadd", isadd);
	}
	
	public void addGroups() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean isadd = false;
		try {
			long userId = selectedUser.getUserId();
			if(CollectionUtils.isEmpty(selecteGroups)) {
				return;
			}
			List<Long> groupIds = new ArrayList<>();
			for(String g : selecteGroups) {
				Long gId = Long.valueOf(g.substring(g.lastIndexOf("(")+ 1, g.lastIndexOf(")")));
				groupIds.add(gId);
			}
			userAction.saveUserGroups(userId, groupIds);
			userGroups = userAction.getUserGroup(userId);
			
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "添加成功","成功添加了工厂");
			isadd = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			isadd = false;
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"添加失败", "服务器出现了异常，本次操作失败了。");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("isadd", isadd);
	}
	
	public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = null;
		User u = (User) event.getObject();
		
		if(StringUtils.isBlank(u.getPassWord())) {
			msg = new FacesMessage("修改失败", "密码不可以为空");
		} else {
			try {
				userAction.updateUser(u);
				msg = new FacesMessage("修改成功", String.format("成功修改了用户[%s] 的信息", u.getUserName()));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"修改失败", "服务器出现了异常，本次操作失败了。");
			} 
		}
       
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }


	public void deleteUser() {
		FacesMessage message = null;
		try {
			if("test".equals(selectedUser.getUserName())) {
				message = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"删除失败", String.format("用户[%s]是系统预留用户，建议不要删除", selectedUser.getUserName()));
			} else {
				if(userAction.checkGroupsEmpty(selectedUser.getUserId())) {
					userAction.delete(selectedUser.getUserId());
					message = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"删除成功", String.format("用户[%s]已经被成功删除", selectedUser.getUserName()));
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"删除失败", String.format("用户[%s]存在关联工厂，请先取消关联关系再删除用户", selectedUser.getUserName()));
				}
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"操作失败", "服务器出现了异常，本次操作失败了。");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void deleteGroup() {
		FacesMessage message = null;
		try {
			userAction.deleteUserGroup(selectedUser.getUserId(), selectedGroup.getGroupId());
			userGroups = userAction.getUserGroup(selectedUser.getUserId());
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"删除成功", String.format("用户%s和工厂%s解除关联成功", selectedUser.getUserName(), selectedGroup.getGroupName()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"操作失败", "服务器出现了异常，本次操作失败了。");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}


	public User getAddUser() {
		return addUser;
	}

	public void setAddUser(User addUser) {
		this.addUser = addUser;
	}

	public void initAddUser() {
		addUser = new User();
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
		if(selectedUser != null) {
			try {
				userGroups = userAction.getUserGroup(selectedUser.getUserId());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public LazyDataModel<User> getUserPages() {
		return userPages;
	}

	public void onRowSelect(SelectEvent event) {
		System.out.println(1111);
	}

	public void setUserAction(UserAction userAction) {
		this.userAction = userAction;
	}

	public List<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<String> groupList) {
		this.groupList = groupList;
	}

	public List<String> getSelecteGroups() {
		return selecteGroups;
	}

	public void setSelecteGroups(List<String> selecteGroups) {
		this.selecteGroups = selecteGroups;
	}

	public List<Group> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<Group> userGroups) {
		this.userGroups = userGroups;
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

}
