package com.dad.api;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dad.api.component.DadSession;
import com.dad.api.request.LoginRqst;
import com.dad.api.request.LogoutRqst;
import com.dad.api.request.RestPasswordRqst;
import com.dad.api.response.BasicResponse;
import com.dad.api.response.LoginRsp;
import com.dad.common.entity.User;
import com.dad.common.service.UserService;
import com.dad.common.service.exception.BusinessServiceException;
import com.rop.annotation.NeedInSessionType;
import com.rop.annotation.ServiceMethod;
import com.rop.annotation.ServiceMethodBean;
import com.rop.response.BusinessServiceErrorResponse;
import com.rop.response.ServiceUnavailableErrorResponse;

@ServiceMethodBean
public class UserWebApi {
	
	private static Logger log = LoggerFactory.getLogger(UserWebApi.class);
	
	@Autowired
	private UserService userService;
	
	@ServiceMethod(method = "user.login", version = "1.0", needInSession = NeedInSessionType.NO)
	public Object login(LoginRqst request) {
		if(log.isDebugEnabled()) {
			log.debug("login({}, {})", request.getUserName(), request.getPassWord());
		}
		Object resp = null;
		try {
			User user = userService.login(request.getUserName(), request.getPassWord());
			String sessionId = UUID.randomUUID().toString();
			LoginRsp response = new LoginRsp();
			response.setUserId(user.getUserId());
			response.setSessionId(sessionId);
			resp = response;
			
			DadSession session = new DadSession();
			request.getRopRequestContext().addSession(sessionId, session);
			
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		return resp;
		
	}
	
	@ServiceMethod(method = "user.logout", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object logout(LogoutRqst request) {
		if(log.isDebugEnabled()) {
			log.debug("logout({})", request.getUserId());
		}
		request.getRopRequestContext().removeSession();
		return new BasicResponse();
	}
	
	@ServiceMethod(method = "user.restpassword", version = "1.0", needInSession = NeedInSessionType.YES)
	public Object restPassword(RestPasswordRqst request) {
		if(log.isDebugEnabled()) {
			log.debug("restPassword({}, {}, {})", request.getUserId(), request.getOldPassword(), request.getNewPassword());
		}
		Object resp = null;
		try {
			userService.updatePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
			resp = new BasicResponse();
		} catch (BusinessServiceException e) {
			resp = new BusinessServiceErrorResponse(request.getRopRequestContext(),  e.getErrorCode(), e.getParams());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resp = new ServiceUnavailableErrorResponse(request
					.getRopRequestContext().getMethod(), request
					.getRopRequestContext().getLocale());
		}
		
		return resp;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
