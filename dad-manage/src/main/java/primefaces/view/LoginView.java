/*
 * 文 件 名:  LoginBean.java
 * 版    权:  Shenzhen Coship Electronics Co.,Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  闵咏兵
 * 修改时间:  2012-11-16
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package primefaces.view;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import primefaces.common.Constants;

@Component
@ViewScoped
public class LoginView extends BasicView {

	public static Logger log = LoggerFactory.getLogger(LoginView.class);
	
	private static String adminPass ="123@321";

	private String username;

	private String password;

	public String login() {
		log.debug("user login, username:{}, password:{}", username, "***");
		String result = null;
		if ("admin".equals(username) && adminPass.equals(password)) {
			ExternalContext extContext = getContext();
			HttpSession session = (HttpSession) extContext.getSession(true);
			session.setAttribute(Constants.sessionKey, username);
			result = Constants.SUCCESS;
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "用户名或者密码错误",
							null));
		}
		return result;
	}

	public void clean() {
		log.debug("clean ============");
		username = null;
		password = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
