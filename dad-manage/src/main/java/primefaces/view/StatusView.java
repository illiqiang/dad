package primefaces.view;

import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import primefaces.common.Constants;

@Component
@SessionScoped
public class StatusView extends BasicView {
	
	public static Logger log = LoggerFactory.getLogger(StatusView.class);
	
	public String userName() {
		ExternalContext extContext = getContext();
		HttpSession session = (HttpSession) extContext.getSession(true);
		String user = (String) session.getAttribute("username");
		return user;
	}

	public String loginOut() {
		log.debug("loginOut");
		ExternalContext extContext = getContext();
		HttpSession session = (HttpSession) extContext.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		
		return Constants.SUCCESS;
	}
}
