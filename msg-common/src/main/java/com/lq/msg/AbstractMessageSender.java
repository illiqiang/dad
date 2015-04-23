package com.lq.msg;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public abstract class AbstractMessageSender<T extends Serializable> {

	protected JmsTemplate jmsTemplate;

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void send(final T msg) {
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage jmsMessage = session.createObjectMessage(msg);
				return jmsMessage;
			}
		});
	}
}
