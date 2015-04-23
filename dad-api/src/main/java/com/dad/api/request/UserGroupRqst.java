package com.dad.api.request;

import javax.validation.constraints.NotNull;

import com.rop.AbstractRopRequest;

public class UserGroupRqst extends AbstractRopRequest {

	@NotNull
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
