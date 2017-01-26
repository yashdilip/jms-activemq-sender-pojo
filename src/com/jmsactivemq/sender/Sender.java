package com.jmsactivemq.sender;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.jmsactivemq.sender.model.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class Sender {

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;
	
	public Sender(){}
	
	public void sendMessage(){
		try{
			System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
			factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("MYQUEUE");
			producer = session.createProducer(destination);
			//TextMessage message = session.createTextMessage();
			//message.setText("Hello. Messaging using JMS and ActiveMQ");
			//producer.send(message);
			
			/*Sending message using XML*/
			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("user", com.jmsactivemq.sender.model.User.class);
			User user = new User("test1", "Dilip Nepali", "25");
			TextMessage message = session.createTextMessage(xstream.toXML(user));
			producer.send(message);			
			System.out.println("Message Send: "+ message.getText());
			
			/*User user = new User("test1", "Dilip Nepali", "25");
			ObjectMessage obj = session.createObjectMessage();
			obj.setObject(user);
			producer.send(obj);
			System.out.println("User Object Send: "+ user.toString());*/
			
		} catch (JMSException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sender sender = new Sender();
		sender.sendMessage();
	}

}
