package com.dad.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dad.common.entity.Device;
import com.dad.common.entity.DevicePollutants;
import com.dad.common.entity.Group;
import com.dad.common.entity.Pollutants;
import com.dad.common.entity.PollutantsCountData;
import com.dad.common.service.CacheService;
import com.dad.common.service.DadService;
import com.dad.common.service.exception.BusinessServiceException;
import com.dad.common.service.exception.ManageConstant;
import com.dad.service.dao.DeviceDao;
import com.dad.service.dao.PollutantDao;
import com.dad.service.util.CacheKeyUtil;

public class DadServiceImpl implements DadService {

	private static Logger log = LoggerFactory.getLogger(DadServiceImpl.class);

	private CacheService dadCacheService;

	private DeviceDao deviceDao;
	
	private PollutantDao pollutantDao;

	@Override
	public boolean checkAndUpdateDevice(Device device) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("updateAndCheckDevice({})", device);
		}
		String deviceKey = CacheKeyUtil.getDeviceKey(device.getDeviceId());
		Device oldDevice = dadCacheService.get(deviceKey);
		if (oldDevice == null) {
			oldDevice = deviceDao.getDevice(device.getDeviceId());
			if (oldDevice == null) {
				return false;
			} 
		}

		deviceDao.updateDevice(device);
		dadCacheService.set(deviceKey, device);
		return true;
	}

	public void saveDevicePltByCountDatas(List<PollutantsCountData> counts)
			throws Exception {
		String deviceId = null;
		List<DevicePollutants> newPollutants = null;
		for (PollutantsCountData c : counts) {
			deviceId = c.getDeviceId();
			if (getDevicePollutants(deviceId, c.getCode()) == null) {
				DevicePollutants newPlt = new DevicePollutants();
				newPlt.setDeviceId(deviceId);
				newPlt.setDataCode(c.getCode());

				if (newPollutants == null) {
					newPollutants = new ArrayList<>();
				}
				newPollutants.add(newPlt);
			}
		}

		if (newPollutants != null) {
			deviceDao.saveDevicePollutants(newPollutants);
			dadCacheService.remove(CacheKeyUtil.getDevicePltListKey(deviceId));
		}
	}

	public DevicePollutants getDevicePollutants(String deviceId, String dataCode)
			throws Exception {
		String pltKey = CacheKeyUtil.getDevicePltKey(deviceId, dataCode);
		DevicePollutants plt = dadCacheService.get(pltKey);
		if (plt == null) {
			plt = deviceDao.getDevicePollutants(deviceId, dataCode);
			if (plt != null) {
				dadCacheService.set(pltKey, plt);
			}
		}
		return plt;
	}

	public List<DevicePollutants> getDevicePollutants(String deviceId)
			throws Exception {

		return deviceDao.getDevicePollutants(deviceId);
	}

	@Override
	public void setDeviceOnline(String deviceId, boolean isOnline)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("setDeviceOnline({},{})", deviceId, isOnline);
		}
		deviceDao.insertOrUpdateDeviceOnline(deviceId, isOnline);
	}

	@Override
	public List<Group> getGroupsByUser(long userId) throws Exception {
		return deviceDao.getGroupsByUser(userId);
	}

	@Override
	public void setPollutantWarn(String deviceId, String dataCode, Double min,
			Double max) throws Exception {
		deviceDao.updatePollutantWarn(deviceId, dataCode, min, max);
		dadCacheService
				.remove(CacheKeyUtil.getDevicePltKey(deviceId, dataCode));
		dadCacheService.remove(CacheKeyUtil.getDevicePltListKey(deviceId));
	}

	@Override
	public List<Long> getDeviceUserIds(String deviceId) throws Exception {
		String key = CacheKeyUtil.getDeviceUserKey(deviceId);
		List<Long> userIds = dadCacheService.get(key);
		if (CollectionUtils.isEmpty(userIds)) {
			userIds = deviceDao.getDeviceUserIds(deviceId);
			dadCacheService.set(key, userIds==null?new ArrayList<>():userIds);
		}
		return userIds;
	}
	
	private void removeDeviceUserCache(String deviceId) throws Exception {
		String key = CacheKeyUtil.getDeviceUserKey(deviceId);
		dadCacheService.remove(key);
	}

	@Override
	public List<Device> getDeviceByGroup(long groupId) throws Exception {
		return deviceDao.getDeviceByGroup(groupId);
	}

	public void setDadCacheService(CacheService dadCacheService) {
		this.dadCacheService = dadCacheService;
	}

	public void setDeviceDao(DeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}

	@Override
	public List<String> getOnlineDeviceIds() throws Exception {
		return deviceDao.getOnlineDeviceIds();
	}

	@Override
	public List<Group> getGroups() throws Exception {
		return deviceDao.getGroups();
	}

	@Override
	public void saveUserGroups(List<Long> groupIds, long userId) throws Exception {
		deviceDao.saveUserGroups(groupIds, userId);
		for(Long gid :groupIds) {
			List<Device> ds = getDeviceByGroup(gid);
			if(CollectionUtils.isNotEmpty(ds)) {
				for(Device d:ds) {
					removeDeviceUserCache(d.getDeviceId());
				}
			}
		}
	}

	@Override
	public void deleteUserGroup(long userId, long groupId) throws Exception {
		deviceDao.deleteUserGroup(userId, groupId);
		List<Device> ds = getDeviceByGroup(groupId);
		if(CollectionUtils.isNotEmpty(ds)) {
			for(Device d:ds) {
				removeDeviceUserCache(d.getDeviceId());
			}
		}
	}

	@Override
	public List<Group> getGroupByPage(int first, int pages) throws Exception {
		return deviceDao.getGroupByPage(first, pages);
	}

	@Override
	public Group getGroupByName(String name) throws Exception {
		return deviceDao.getGroupByName(name);
	}

	@Override
	public int getGroupSize() throws Exception {
		return deviceDao.getGroupSize();
	}

	@Override
	public List<Long> getGroupUserIds(long groupId) throws Exception {
		return deviceDao.getGroupUserIds(groupId);
	}

	@Override
	public void addGroup(Group g) throws Exception {
		Group oldg = deviceDao.getGroupByName(g.getGroupName());
		if(oldg != null) {
			throw new BusinessServiceException(ManageConstant.GROUP_EXISTS);
		}
		deviceDao.addGroup(g);
	}

	@Override
	public void deleteGroup(long groupId) throws Exception {
		deviceDao.deleteGroup(groupId);
	}

	@Override
	public List<String> getDeviceIdList() throws Exception {
		return deviceDao.getDeviceIdList();
	}

	@Override
	public void saveGroupDevices(long groupId, List<String> devices)
			throws Exception {
		deviceDao.saveGroupDevices(groupId, devices);
		//情况设备的用户缓存
		for(String deviceId: devices) {
			removeDeviceUserCache(deviceId);
		}
	}

	@Override
	public void deleteGroupDevice(long groupId, String deviceId)
			throws Exception {
		deviceDao.deleteGroupDevice(groupId, deviceId);
		//情况设备的用户缓存
		removeDeviceUserCache(deviceId);
	}

	@Override
	public List<Device> getDeviceByPage(int first, int pages) throws Exception {
		return deviceDao.getDeviceByPage(first, pages);
	}

	@Override
	public Device getDeviceById(String deviceId) throws Exception {
		return deviceDao.getDevice(deviceId);
	}

	@Override
	public int getDeviceSize() throws Exception {
		return deviceDao.getDeviceSize();
	}

	@Override
	public void addDevice(Device d) throws Exception {
		Device olddevice = deviceDao.getDevice(d.getDeviceId());
		if(olddevice != null) {
			throw new BusinessServiceException(ManageConstant.DEVICE_EXISTS);
		}
		deviceDao.addDevice(d);
	}

	@Override
	public List<Long> getGroupIdByDevice(String deviceId) throws Exception {
		return deviceDao.getGroupIdByDevice(deviceId);
	}

	@Override
	public void deleteDevice(String deviceId) throws Exception {
		deviceDao.deleteDevice(deviceId);
	}
	
	

	
	@Override
	public List<Pollutants> getPollutantsByPage(int first, int pages)
			throws Exception {
		return pollutantDao.getPollutantsByPage(first, pages);
	}

	@Override
	public Pollutants getPollutantById(String dataCode) throws Exception {
		String cKey = CacheKeyUtil.getPltInfoKey(dataCode);
		Pollutants plt = dadCacheService.get(cKey);
		if(plt == null) {
			plt = pollutantDao.getPollutantById(dataCode);
			if(plt != null) {
				dadCacheService.set(cKey, plt);
			}
		}
		return plt;
	}
	
	private void removePltInfoCache(String dataCode) throws Exception {
		dadCacheService.remove(CacheKeyUtil.getPltInfoKey(dataCode));
	} 

	@Override
	public int getPollutantSize() throws Exception {
		return pollutantDao.getPollutantSize();
	}

	@Override
	public void addPollutant(Pollutants d) throws Exception {
		Pollutants p = pollutantDao.getPollutantById(d.getDataCode());
		if(p == null) {
			pollutantDao.addPollutant(d);
		} else {
			throw new BusinessServiceException(ManageConstant.PLT_EXISTS);
		}
		
	}

	@Override
	public void updatePollutant(Pollutants d) throws Exception {
		pollutantDao.updatePollutant(d);
		removePltInfoCache(d.getDataCode());
	}

	@Override
	public void deletePollutant(String dataCode) throws Exception {
		pollutantDao.deletePollutant(dataCode);
		removePltInfoCache(dataCode);
	}

	public void setPollutantDao(PollutantDao pollutantDao) {
		this.pollutantDao = pollutantDao;
	}

	@Override
	public void updateGroup(Group g) throws Exception {
		deviceDao.updateGroup(g);
	}

}
