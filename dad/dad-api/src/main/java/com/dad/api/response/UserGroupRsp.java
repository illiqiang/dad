package com.dad.api.response;

import java.util.List;

import com.dad.common.entity.Group;

public class UserGroupRsp extends BasicResponse {
	private List<Group> groups;

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
}
