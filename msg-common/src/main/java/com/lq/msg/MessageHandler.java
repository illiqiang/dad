package com.lq.msg;

import java.io.Serializable;

public interface MessageHandler<T extends Serializable> {
	public void hander(T msg);
}
