package com.dad.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class RestPasswordRqst extends AbstractRopRequest {
	
	@NotNull
	private Long userId;
	
	@NotEmpty
	@Pattern(regexp = "\\w{6,32}")
	private String oldPassword;
	
	@NotEmpty
	@Pattern(regexp = "\\w{6,32}")
	private String newPassword;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
