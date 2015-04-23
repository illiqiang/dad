package com.lq.msg;

import java.io.Serializable;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMessageListener<T extends Serializable> implements MessageListener {

	private final static Logger log = LoggerFactory
			.getLogger(AbstractMessageListener.class);

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message msg) {
		if (msg instanceof ObjectMessage) {
			try {
				T object = (T) ((ObjectMessage) msg).getObject();
				handle(object);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		} else {
			log.error("Receive a Illegal message, message type is {}",
					msg.getClass());
		}
	}

	public abstract void handle(T t); 
}
