package primefaces.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import primefaces.common.Constants;

public class LoginFilter implements Filter {

	public static Logger log = LoggerFactory.getLogger(LoginFilter.class);
	public List<String> needLoginUris;
		
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		needLoginUris = new ArrayList<>();
		needLoginUris.add("/main/main.do");
		needLoginUris.add("/manage/user.do");
		needLoginUris.add("/manage/group.do");
		needLoginUris.add("/manage/device.do");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI();
		log.debug("doFilter {}", path);
		for(String uri : needLoginUris) {
			if(path.indexOf(uri) > -1){
				HttpServletResponse res = (HttpServletResponse) response;
				HttpSession session = req.getSession();
				String user = (String) session.getAttribute(Constants.sessionKey);
				if(user == null) {
					log.debug("User is not logged!");
					res.sendRedirect(req.getContextPath()+Constants.loginView);
					return;
				}
				break;
			}
		}
		
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		log.debug("LoginFilter destroy");
	}

}
