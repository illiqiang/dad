package com.dad.service.dao;

import java.util.List;

import com.dad.common.entity.Device;
import com.dad.common.entity.DevicePollutants;
import com.dad.common.entity.Group;

public interface DeviceDao {
	public List<DevicePollutants> getDevicePollutants(String deviceId);
	public DevicePollutants getDevicePollutants(String deviceId, String dataCode);
	public void updatePollutantWarn(String deviceId, String dataCode, Double min, Double max);
	public void saveDevicePollutants(List<DevicePollutants> pollutants);
	public Device getDevice(String deviceId);
	public void updateDevice(Device device);
	public void insertOrUpdateDeviceOnline(String deviceId, boolean isOnline);
	public List<Group> getGroupsByUser(long userId);
	public List<Device> getDeviceByGroup(long groupId);
	
	public List<Long> getDeviceUserIds(String deviceId);
	
	public List<String> getOnlineDeviceIds();
	
	
	//manage
	public List<Group> getGroups();
	public void saveUserGroups(List<Long> groupIds, long userId);
	public void deleteUserGroup(long userId, long groupId);
	public List<Group> getGroupByPage(int first, int pages);
	public Group getGroupByName(String name);
	public int getGroupSize();
	public List<Long> getGroupUserIds(long groupId) throws Exception;
	public void addGroup(Group g) throws Exception;
	public void updateGroup(Group g) throws Exception;
	public void deleteGroup(long groupId) throws Exception;
	public List<String> getDeviceIdList() throws Exception;
	public void saveGroupDevices(long groupId, List<String> devices) throws Exception;
	public void deleteGroupDevice(long groupId, String deviceId) throws Exception;
	
	public List<Device> getDeviceByPage(int first, int pages) throws Exception;
	public int getDeviceSize() throws Exception;
	public void addDevice(Device d) throws Exception;
	public List<Long> getGroupIdByDevice(String deviceId) throws Exception;
	public void deleteDevice(String deviceId) throws Exception;
}
