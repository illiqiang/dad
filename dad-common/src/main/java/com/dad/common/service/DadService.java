package com.dad.common.service;

import java.util.List;

import com.dad.common.entity.Device;
import com.dad.common.entity.DevicePollutants;
import com.dad.common.entity.Group;
import com.dad.common.entity.Pollutants;
import com.dad.common.entity.PollutantsCountData;

public interface DadService {
	
	/**
	 * 判断设备是否存在，如果存在修改设备信息，设置上线
	 * @param device
	 * @return
	 * @throws Exception
	 */
	public boolean checkAndUpdateDevice(Device device) throws Exception;
	
	public void setDeviceOnline(String deviceId, boolean isOnline) throws Exception;
	
	public void saveDevicePltByCountDatas(List<PollutantsCountData> counts) throws Exception;
	
	public List<DevicePollutants> getDevicePollutants(String deviceId) throws Exception;
	
	public DevicePollutants getDevicePollutants(String deviceId, String dataCode)  throws Exception;
	
	public List<Group> getGroupsByUser(long userId) throws Exception;
	
	public List<Device> getDeviceByGroup(long groupId) throws Exception;
	
	public void setPollutantWarn(String deviceId, String dataCode, Double min, Double max) throws Exception;
	
	public List<Long> getDeviceUserIds(String deviceId) throws Exception;
	
	public List<String> getOnlineDeviceIds() throws Exception;
	
	//manage
	public List<Group> getGroups() throws Exception;
	
	public void saveUserGroups(List<Long> groupIds, long userId) throws Exception;
	
	public void updateGroup(Group g) throws Exception;
	
	public void deleteUserGroup(long userId, long groupId) throws Exception;
	
	public List<Group> getGroupByPage(int first, int pages) throws Exception;
	
	public int getGroupSize() throws Exception;
	
	public Group getGroupByName(String name) throws Exception;
	
	public List<Long> getGroupUserIds(long groupId) throws Exception;
	
	public void addGroup(Group g) throws Exception;
	
	public void deleteGroup(long groupId) throws Exception;
	
	public List<String> getDeviceIdList() throws Exception;
	
	public void saveGroupDevices(long groupId, List<String> devices) throws Exception;
	
	public void deleteGroupDevice(long groupId, String deviceId) throws Exception;
	
	public List<Device> getDeviceByPage(int first, int pages) throws Exception;
	public Device getDeviceById(String deviceId) throws Exception;
	public int getDeviceSize() throws Exception;
	public void addDevice(Device d) throws Exception;
	public List<Long> getGroupIdByDevice(String deviceId) throws Exception;
	public void deleteDevice(String deviceId) throws Exception;
	
	
	//Pollutant
	public List<Pollutants> getPollutantsByPage(int first, int pages) throws Exception;
	public Pollutants getPollutantById(String dataCode) throws Exception;
	public int getPollutantSize() throws Exception;
	public void addPollutant(Pollutants d) throws Exception;
	public void updatePollutant(Pollutants d) throws Exception;
	public void deletePollutant(String dataCode) throws Exception;
	
}
