package primefaces.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import primefaces.common.page.PageBean;

import com.dad.common.entity.Device;
import com.dad.common.entity.Group;
import com.dad.common.service.DadService;

@Service
public class GroupAction {
	
	@Autowired
	private DadService dadService;
	
	public PageBean<Group> getGroupPages(int first, int pageSize, String groupName) throws Exception {
		PageBean<Group> pages = new PageBean<Group>();
		int size = 0;
		List<Group> groups = new ArrayList<>();
		if(StringUtils.isBlank(groupName)) {
			groups = dadService.getGroupByPage(first, pageSize);
			if(CollectionUtils.isNotEmpty(groups)) {
				size = dadService.getGroupSize();
			}
		} else {
			Group g = dadService.getGroupByName(groupName);
			if(g != null) {
				groups.add(g);
				size = 1;
			}
		}
		
		pages.setDatas(groups);
		pages.setTotalSize(size);
		return pages;
	}
	
	public boolean checkDeviceEmpty(long groupId) throws Exception {
		return CollectionUtils.isEmpty(dadService.getDeviceByGroup(groupId));
	}
	
	
	public boolean checkUserBindEmpty(long groupId) throws Exception {
		return CollectionUtils.isEmpty(dadService.getGroupUserIds(groupId));
	}
	
	public void delete(long groupId) throws Exception {
		dadService.deleteGroup(groupId);
	}
	
	public void addGroup(Group g) throws Exception{
		dadService.addGroup(g);
	}
	
	public List<String> getDeviceList() throws Exception {
		return dadService.getDeviceIdList();
	}
	
	public void saveGroupDevices(long groupId, List<String> devices) throws Exception {
		dadService.saveGroupDevices(groupId, devices);
	}
	
	public List<Device> getGroupDevice(long groupId) throws Exception {
		return dadService.getDeviceByGroup(groupId);
	}
	
	public void deleteGroupDevice(long groupId, String deviceId) throws Exception {
		dadService.deleteGroupDevice(groupId, deviceId);
	}
}
