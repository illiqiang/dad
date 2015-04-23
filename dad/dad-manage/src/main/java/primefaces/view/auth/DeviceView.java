package primefaces.view.auth;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import primefaces.common.page.PageBean;
import primefaces.common.page.PageDataModel;
import primefaces.common.page.Sort;
import primefaces.service.DeviceAction;

import com.dad.common.entity.Device;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.service.exception.ManageConstant;

@ManagedBean
@ViewScoped
public class DeviceView {

	private final static Logger log = LoggerFactory.getLogger(DeviceView.class);
	
	@ManagedProperty(value = "#{deviceAction}")
	private DeviceAction deviceAction;
	
	private LazyDataModel<Device> devicePages;
	
	private Device selectDevice;
	
	private Device addDevice = new Device();
	
	@PostConstruct
	public void init() {
		
		devicePages = new PageDataModel<Device>() {

			private static final long serialVersionUID = 4497840920037869584L;

			@Override
			public Object getRowKey(Device object) {
				return object.getDeviceId();
			}

			@Override
			public PageBean<Device> getPageResult(int first, int pageSize,
					List<Sort> multiSortMeta, Map<String, Object> filters) {
				String deviceId = (String) filters.get("deviceId");
				PageBean<Device> groups = new PageBean<Device>();
				try {
					groups = deviceAction.getDevicePages(first, pageSize, deviceId);
					
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return groups;
			}
		};
	}
	
	public void initAddDevice() {
		addDevice = new Device();
	}
	
	public void addDevice() {
		RequestContext context = RequestContext.getCurrentInstance();
		FacesMessage message = null;
		boolean isadd = false;
		if (StringUtils.isNotEmpty(addDevice.getDeviceId())) {
			try {
				deviceAction.addDevice(addDevice);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "新增成功",
						String.format("设备[%s] 已经成功添加", addDevice.getDeviceId()));
				isadd = true;
			} catch (BusinessServiceException e) {
				isadd = false;
				if(ManageConstant.DEVICE_EXISTS.equals(e.getErrorCode())) {
					message = new FacesMessage(FacesMessage.SEVERITY_WARN,
							"新增失败", "该设备已经存在。");
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
					"设备号不能为空");
		}

		FacesContext.getCurrentInstance().addMessage(null, message);
		context.addCallbackParam("isadd", isadd);
	}

	public void deleteDevice() {
		FacesMessage message = null;
		try {

			if (!deviceAction.checkDeviceGroupEmpty(selectDevice.getDeviceId())) {
				message = new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"删除失败",
						String.format("设备[%s] 存在关联工厂，请先去工厂管理页面取消关联。", selectDevice.getDeviceId()));
			}  else if(deviceAction.checkDeviceUse(selectDevice.getDeviceId())){
				message = new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"删除失败",
						String.format("设备[%s] 正在使用中，你不能删除他。", selectDevice.getDeviceId()));
			}else {
				deviceAction.delete(selectDevice.getDeviceId());
				
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "删除成功",
						String.format("设备[%s] 已被成功删除",
								selectDevice.getDeviceId()));
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败",
					"服务器出现了异常，本次操作失败了。");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Device getSelectDevice() {
		return selectDevice;
	}

	public void setSelectDevice(Device selectDevice) {
		this.selectDevice = selectDevice;
	}

	public Device getAddDevice() {
		return addDevice;
	}

	public void setAddDevice(Device addDevice) {
		this.addDevice = addDevice;
	}

	public LazyDataModel<Device> getDevicePages() {
		return devicePages;
	}

	public void setDeviceAction(DeviceAction deviceAction) {
		this.deviceAction = deviceAction;
	}
	
	
}
