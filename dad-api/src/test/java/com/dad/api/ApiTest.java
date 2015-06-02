package com.dad.api;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dad.api.request.RtdListRqst;
import com.dad.api.response.BasicResponse;
import com.dad.api.response.GroupDeviceRsp;
import com.dad.api.response.LoginRsp;
import com.dad.api.response.MonthDataRsp;
import com.dad.api.response.QuarterDataRsp;
import com.dad.api.response.RtdListRsp;
import com.dad.api.response.UserGroupRsp;
import com.rop.client.ClientRequest;
import com.rop.client.CompositeResponse;
import com.rop.client.DefaultRopClient;
import com.rop.client.RopClient;

public class ApiTest {
	//private static String host = "http://localhost:8080/router";
	private static String host = "http://120.25.231.113:3680/router";
	private static String app_key = "dad_android";
	private static String appSecret = "12@adaa322e";
	private static String v = "1.0";

	public static String sessionId = null;
	private static <T> CompositeResponse<T> ropGet(String sessionId,
			Map<String, Object> params, Class<T> rsp, String method) {
		RopClient ropClient = new DefaultRopClient(host, app_key, appSecret);
		if (sessionId != null) {
			ropClient.setSessionId(sessionId);
		}

		ClientRequest request = ropClient.buildClientRequest();
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				request.addParam(key, params.get(key));
			}
		}
		@SuppressWarnings("unchecked")
		CompositeResponse<T> compositeResp = request.get(rsp, method, v);
		return compositeResp;
	}

	private void httpTest(Map<String, String> params) {

		StringBuilder url = new StringBuilder(host).append("?");
		TreeMap<String, String> ap = new TreeMap<>();
		
		ap.put("appKey", app_key);
		ap.put("v", v);
		ap.putAll(params);
		StringBuilder sb = new StringBuilder(appSecret);
		for (String key : ap.keySet()) {
			sb.append(key).append(ap.get(key));
			url.append(key).append("=").append(ap.get(key)).append("&");
		}
		sb.append(appSecret);
		System.out.println(sb.toString());
		url.append("sign").append("=").append(HttpUtil.getSha1(sb.toString()));

		String l = url.toString();
		System.out.println(l);
		
		HttpUtil.get(l);

	}
	
	private static <T> CompositeResponse<T> ropPost(String sessionId,
			Map<String, Object> params, Class<T> rsp, String method) {
		RopClient ropClient = new DefaultRopClient(host, app_key, appSecret);
		if (sessionId != null) {
			ropClient.setSessionId(sessionId);
		}

		ClientRequest request = ropClient.buildClientRequest();
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				request.addParam(key, params.get(key));
			}
		}
		@SuppressWarnings("unchecked")
		CompositeResponse<T> compositeResp = request.post(rsp, method, v);
		return compositeResp;
	}

	@BeforeClass
	public static void login() {
		Map<String, Object> loginParams = new HashMap<String, Object>();
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("userName", "test");
		bindParams.put("passWord", "e10adc3949ba59abbe56e057f20f883e");
		CompositeResponse<LoginRsp> loginRsp = ropGet(null,
				bindParams, LoginRsp.class, "user.login");
		sessionId = loginRsp.getSuccessResponse().getSessionId();
		System.out.println("login end==================");
	}

	@Test
	public void loginout() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("userId", "100000");
		CompositeResponse<LoginRsp> loginRsp = ropGet(sessionId,
				bindParams, LoginRsp.class, "user.logout");

	}
	
	@Test
	public void logoutHttp() {
		Map<String, String> bindParams = new HashMap<String, String>();
		bindParams.put("userId", "100000");
		bindParams.put("method", "user.logout");
		bindParams.put("sessionId", sessionId);
		httpTest(bindParams);
	}
	
	@Test
	public void resetPass(){
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("userId", "100000");
		bindParams.put("oldPassword", "123456122");
		bindParams.put("newPassword", "123456122");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "user.restpassword");
	}
	
	@Test
	public void usergroup() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("userId", "100001");
		CompositeResponse<UserGroupRsp> loginRsp = ropGet(sessionId,
				bindParams, UserGroupRsp.class, "dad.usergroup");
	}
	
	@Test
	public void usergroupHttp() {
		Map<String, String> bindParams = new HashMap<String, String>();
		bindParams.put("userId", "100001");
		bindParams.put("method", "dad.usergroup");
		bindParams.put("sessionId", sessionId);
		httpTest(bindParams);
	}

	@Test
	public void groupdevice() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("groupId", "100003");
		CompositeResponse<GroupDeviceRsp> loginRsp = ropGet(sessionId,
				bindParams, GroupDeviceRsp.class, "dad.groupdevice");
	}
	
	@Test
	public void pollutants() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "dad.pollutants");
	}
	
	@Test
	public void setpollutants() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		bindParams.put("dataCode", "011");
		bindParams.put("min", "18.1");
		bindParams.put("max", "20.3");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "dad.setpollutants");
	}
	
	@Test
	public void rtddata() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		bindParams.put("dataCode", "011");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "dad.rtddata");
	}
	
	@Test
	public void minutedata() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		bindParams.put("dataCode", "01");
		bindParams.put("hour", "2015033118");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "dad.minutedata");
	}
	
	@Test
	public void minutedatabyday() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		bindParams.put("dataCode", "01");
		bindParams.put("day", "20150531");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "dad.minutedatabyday");
	}
	
	@Test
	public void hourdata() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		bindParams.put("dataCode", "011");
		bindParams.put("day", "20150203");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "dad.hourdata");
	}
	
	@Test
	public void daydata() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		bindParams.put("dataCode", "011");
		bindParams.put("month", "201502");
		CompositeResponse<BasicResponse> loginRsp = ropGet(sessionId,
				bindParams, BasicResponse.class, "dad.daydata");
	}
	
	@Test
	public void monthdata() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		bindParams.put("dataCode", "01");
		bindParams.put("year", "2015");
		CompositeResponse<MonthDataRsp> loginRsp = ropGet(sessionId,
				bindParams, MonthDataRsp.class, "dad.monthdata");
	}
	
	@Test
	public void quarterdata() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000004");
		bindParams.put("dataCode", "01");
		bindParams.put("year", "2015");
		CompositeResponse<QuarterDataRsp> loginRsp = ropGet(sessionId,
				bindParams, QuarterDataRsp.class, "dad.quarterdata");
	}
	
	@Test
	public void rtdlist() {
		Map<String, Object> bindParams = new HashMap<String, Object>();
		bindParams.put("deviceId", "88888880000001");
		CompositeResponse<RtdListRsp> loginRsp = ropGet(sessionId,
				bindParams, RtdListRsp.class, "dad.rtdlist");
	}
}
