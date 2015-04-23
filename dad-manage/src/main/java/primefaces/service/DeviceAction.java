package primefaces.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import primefaces.common.page.PageBean;

import com.dad.common.entity.Device;
import com.dad.common.service.DadService;

@Service
public class DeviceAction {
	
	@Autowired
	private DadService dadService;
	
	public PageBean<Device> getDevicePages(int first, int pageSize, String deviceId) throws Exception {
		PageBean<Device> pages = new PageBean<Device>();
		int size = 0;
		List<Device> devices = new ArrayList<>();
		if(StringUtils.isBlank(deviceId)) {
			devices = dadService.getDeviceByPage(first, pageSize);
			if(CollectionUtils.isNotEmpty(devices)) {
				size = dadService.getDeviceSize();
			}
		} else {
			Device d = dadService.getDeviceById(deviceId);
			if(d != null) {
				devices.add(d);
				size = 1;
			}
		}
		pages.setDatas(devices);
		pages.setTotalSize(size);
		return pages;
	}

	public void addDevice(Device d) throws Exception {
		dadService.addDevice(d);
	}

	public boolean checkDeviceGroupEmpty(String deviceId) throws Exception {
		return CollectionUtils.isEmpty(dadService.getGroupIdByDevice(deviceId));
	}

	public boolean checkDeviceUse(String deviceId) throws Exception {
		Device device = dadService.getDeviceById(deviceId);
		return StringUtils.isNotEmpty(device.getSt());
	}

	public void delete(String deviceId) throws Exception {
		dadService.deleteDevice(deviceId);
	}
}
