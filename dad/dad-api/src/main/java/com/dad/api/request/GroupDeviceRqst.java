package com.dad.api.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class GroupDeviceRqst extends AbstractRopRequest {

	@NotNull
	private Long groupId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}