package primefaces.view.auth;

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
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import primefaces.common.page.PageBean;
import primefaces.common.page.PageDataModel;
import primefaces.common.page.Sort;
import primefaces.service.GroupAction;

import com.dad.common.entity.Device;
import com.dad.common.entity.Group;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.service.exception.ManageConstant;

@ManagedBean
@ViewScoped
public class GroupView {

	private final static Logger log = LoggerFactory.getLogger(GroupView.class);

	@ManagedProperty(value = "#{groupAction}")
	private GroupAction groupAction;

	private LazyDataModel<Group> groupPages;

	private Group selectGroup;
	
	private Group addGroup = new Group();
	
	private List<String> selecteDevices;
	
	private List<String> deviceList;
	
	private Device selecteDevice;
	
	private List<Device> groupDevices;

	
	public List<String> getSelecteDevices() {
		return selecteDevices;
	}

	public void setSelecteDevices(List<String> selecteDevices) {
		this.selecteDevices = selecteDevices;
	}

	public Device getSelecteDevice() {
		return selecteDevice;
	}

	public void setSelecteDevice(Device selecteDevice) {
		this.selecteDevice = selecteDevice;
	}

	public List<String> getDeviceList() {
		return deviceList;
	}

	public List<Device> getGroupDevices() {
		return groupDevices;
	}

	public LazyDataModel<Group> getGroupPages() {
		return groupPages;
	}

	public void setGroupAction(GroupAction groupAction) {
		this.groupAction = groupAction;
	}

	public Group getSelectGroup() {
		return selectGroup;
	}

	public void setSelectGroup(Group selectGroup) {
		this.selectGroup = selectGroup;
		if(selectGroup != null) {
			try {
				groupDevices = groupAction.getGroupDevice(selectGroup.getGroupId());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public Group getAddGroup() {
		return addGroup;
	}

	public void setAddGroup(Group addGroup) {
		this.addGroup = addGroup;
	}
	
	@PostConstruct
	public void init() {
		
		try {
			deviceList = groupAction.getDeviceList();
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
		}
		
		groupPages = new PageDataModel<Group>() {

			private static final long serialVersionUID = 4497840920037869584L;

			@Override
			public Object getRowKey(Group object) {
				return object.getGroupId();
			}

			@Override
			public PageBean<Group> getPageResult(int first, int pageSize,
					List<Sort> multiSortMeta, Map<String, Object> filters) {
				String groupName = (String) filters.get("groupName");
				PageBean<Group> groups = new PageBean<Group>();
				try {
					groups = groupAction.getGroupPages(first, pageSize,
							groupName);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return groups;
			}
		};
	}
	
	public void initAddGroup() {
		addGroup = new Group();
	}
	
	public void addGroup() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean isadd = false;
		if (StringUtils.isNotEmpty(addGroup.getGroupName())) {
			try {
				groupAction.addGroup(addGroup);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "新增成功",
						String.format("工厂[%s] 已经成功添加", addGroup.getGroupName()));
				isadd = true;
			} catch (BusinessServiceException e) {
				isadd = false;
				if(ManageConstant.GROUP_EXISTS.equals(e.getErrorCode())) {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"新增失败", "该工厂已经存在。");
				}
			} catch (Exception e) {
				isadd = false;
				log.error(e.getMessage(), e);
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"新增失败", "服务器出现了异常，本次操作失败了。");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		} else {
			isadd = false;
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "新增失败",
					"工厂名不能为空");
		}

		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("isadd", isadd);
	}

	public void deleteGroup() {
		FacesMessage message = null;
		try {

			if (!groupAction.checkDeviceEmpty(selectGroup.getGroupId())) {
				message = new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"删除失败",
						String.format("工厂[%s] 存在关联设备，请先取消关联设备再删除工厂。", selectGroup.getGroupName()));
			} else if(!groupAction.checkUserBindEmpty(selectGroup.getGroupId())) {
				message = new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"删除失败",
						String.format("工厂[%s] 已经被其他用户绑定，请先去用户管理界面取消关联关系。", selectGroup.getGroupName()));
			} else {
				groupAction.delete(selectGroup.getGroupId());
				
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "删除成功",
						String.format("工厂[%s] 已被成功删除",
								selectGroup.getGroupName()));
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败",
					"服务器出现了异常，本次操作失败了。");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void deleteDevice() {
		FacesMessage message = null;
		try {
			groupAction.deleteGroupDevice(selectGroup.getGroupId(), selecteDevice.getDeviceId());
			groupDevices = groupAction.getGroupDevice(selectGroup.getGroupId());;
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"删除成功", String.format("工厂[%s] 和设备[%s] 解除关联成功", selectGroup.getGroupName(),selecteDevice.getDeviceId()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"操作失败", "服务器出现了异常，本次操作失败了。");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void addDevices() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean isadd = false;
		try {
			long groupId = selectGroup.getGroupId();
			if(CollectionUtils.isEmpty(selecteDevices)) {
				return;
			}
			
			groupAction.saveGroupDevices(groupId, selecteDevices);
			groupDevices = groupAction.getGroupDevice(groupId);
			
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "添加成功","成功添加了设备");
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
	
}
