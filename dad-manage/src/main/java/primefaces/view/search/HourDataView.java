package primefaces.view.search;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.common.entity.Device;
import com.dad.common.entity.DevicePollutants;
import com.dad.common.entity.Group;
import com.dad.common.entity.PollutantsCountData;
import com.dad.common.service.DadDataService;
import com.dad.common.service.DadService;
import com.dad.common.util.DateUtil;
import com.dad.common.util.StType;

@ManagedBean
@ViewScoped
public class HourDataView {
	
	private final static Logger log = LoggerFactory.getLogger(HourDataView.class);
	
	@ManagedProperty(value = "#{dadDataService}")
	private DadDataService dadDataService;
	
	@ManagedProperty(value = "#{dadService}")
	private DadService dadService;
	
	private String deviceId;
	private String dataCode;
	private Date date;
	private List<PollutantsCountData> hourDatas;
	
	private List<Group> groups;
	private Long groupId;
	private List<DevicePollutants> plts;
	private List<Device> devices;
	
	@PostConstruct
	public void init() {
		try {
			groups = dadService.getGroups();
		} catch (Exception e) {
			FacesMessage message = null;
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误","服务器异常，请重新刷新页面");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void groupSelect() {
		try {
			devices = dadService.getDeviceByGroup(groupId);
			if(CollectionUtils.isNotEmpty(devices)) {
				for(Device d:devices) {
					String typeName = StType.getNameByCode(d.getSt());
					d.setPw(typeName+"_"+d.getDeviceId());
				}
			}
			
		} catch (Exception e) {
			FacesMessage message = null;
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误","服务器异常，请重新刷新页面");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void deviceSelect() {
		try {
			plts = dadService.getDevicePollutants(deviceId);
		} catch (Exception e) {
			FacesMessage message = null;
			log.error(e.getMessage(), e);
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误","服务器异常，请重新刷新页面");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public List<DevicePollutants> getPlts() {
		return plts;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDadService(DadService dadService) {
		this.dadService = dadService;
	}
	
	public void refreshData() {
		FacesMessage message = null;
		if(StringUtils.isEmpty(deviceId)) {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "验证失败","设备号不能为空");
		} else if(StringUtils.isEmpty(dataCode)) {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "验证失败","污染物代码不能为空");
		} else if(date == null) {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "验证失败","日期不能为空");
		} else {
			try {
				hourDatas = dadDataService.getHourDatas(deviceId, dataCode, DateUtil.getStringByDate(date, DateUtil.dayFmt));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				message = new FacesMessage(FacesMessage.SEVERITY_WARN, "查询失败","服务器异常");
			}
		}
		
		if(message != null) {
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}


	public void setDadDataService(DadDataService dadDataService) {
		this.dadDataService = dadDataService;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<PollutantsCountData> getHourDatas() {
		return hourDatas;
	}
	
}
