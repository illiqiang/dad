package com.dad.api.component;

import java.util.HashMap;
import java.util.Map;

import com.rop.CommonConstant;
import com.rop.session.Session;

public class DadSession implements Session {

	private static final long serialVersionUID = -6640527110387761334L;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	public static String TIMESTAMPKEY = "@obd_session+timestamp";
	
	public DadSession() {
		setAttribute(TIMESTAMPKEY, System.currentTimeMillis());
	}
	public void setAllAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public void setAttribute(String name, Object obj) {
		attributes.put(name, obj);
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	public void setTimestamp(long timestamp) {
		attributes.put(TIMESTAMPKEY, timestamp);
	}

	@Override
	public Map<String, Object> getAllAttributes() {
		Map<String, Object> tempAttributes = new HashMap<String, Object>(
				attributes.size());
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			if (!CommonConstant.SESSION_CHANGED.equals(entry.getKey())) {
				tempAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		return tempAttributes;
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public boolean isChanged() {
		return attributes.containsKey(CommonConstant.SESSION_CHANGED);
	}

	public void markChanged() {
		attributes.put(CommonConstant.SESSION_CHANGED, Boolean.TRUE);
	}
}
