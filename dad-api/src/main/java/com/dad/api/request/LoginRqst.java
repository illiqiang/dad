package com.dad.api.request;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import com.rop.AbstractRopRequest;

public class LoginRqst extends AbstractRopRequest {

	@NotEmpty
	@Pattern(regexp = "\\w{3,32}")
	private String userName;

	@NotEmpty
	@Pattern(regexp = "\\w{6,32}")
	private String passWord;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE).toString();
	}
}
