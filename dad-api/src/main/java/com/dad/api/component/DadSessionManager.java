package com.dad.api.component;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dad.common.service.UserService;
import com.rop.session.Session;
import com.rop.session.SessionManager;

@Component
public class DadSessionManager implements SessionManager{
	
	private static Logger log = LoggerFactory.getLogger(DadSessionManager.class);
	
	@Autowired
	private UserService userService;
	
	@Value(value = "${api.session.timeout}")
	private int timeOut;
	
	public DadSessionManager(){
		timeOut = 604800;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void addSession(String sessionId, Session session) {
		try {
			userService.setSession(sessionId, session.getAllAttributes(), timeOut);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException();
		}
	}

	@Override
	public Session getSession(String sessionId) {
		try {
			Map<String, Object> attrs = userService.getSession(sessionId);
			if(MapUtils.isNotEmpty(attrs)) {
				DadSession session = new DadSession();
				session.setAllAttributes(attrs);
				Long timestamp = (Long) attrs.get(DadSession.TIMESTAMPKEY);
				//用户在一天内访问过,重新设置session的过期时间
				long currentTime = System.currentTimeMillis();
				if(timestamp !=null && currentTime - timestamp > 86400000) {
					session.setTimestamp(currentTime);
					session.markChanged();			
				}
				return session;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	public void removeSession(String sessionId) {
		try {
			userService.removeSession(sessionId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException();
		}
	}

}
