package com.example.solace.demo;


import static org.junit.Assert.assertEquals;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.json.JSONObject;


//@RunWith(SpringRunner.class)
//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class SolaceTests {

	//Initialize logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	//Solace Connection Parameters
	@Value("${solace.jms.clientUsername}")
	private String solaceUsername;
	
	@Value("${solace.jms.clientPassword}")
	private String solacePassword;
	
	@Value("${solace.jms.host}")
	private String solaceHost;
	
	@Value("${solace.jms.queueName}")
	private String solaceQueue;
	
	@Value("${solace.jms.topicName}")
	private String solaceTopic;
	
	//@Ignore("IGNORE TEST BY DEFAULT")
	@Test
	public void Test1() {

		//Create JSON Object to Send
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key1", "value1");
		jsonObject.put("key2", "value2");
		jsonObject.put("key3", "value3");
		
		//Open Connection to solace broker
		ConnectionFactory connectionFactory = new JmsConnectionFactory(solaceUsername, solacePassword, solaceHost);
		JMSContext context = connectionFactory.createContext();
		
		//Create Queue
		Queue queue = context.createQueue(solaceQueue);
		
		//Create Producer
		JMSProducer producer = context.createProducer();
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//Create Consumer
		JMSConsumer consumer = context.createConsumer(queue);
		
		//Send JSON Object
		producer.send(queue, jsonObject.toString());
		
		//Read JSON Object
		int timeout = 10000; //10 seconds timeout
		String msg = consumer.receiveBody(String.class, timeout);
		
		assertEquals(jsonObject.toString(), msg);
	}
	
	
	@Test
	public void Test2() {

		//Create 5 JSON Objects to Send
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("key11", "value11");
		jsonObject1.put("key12", "value12");
		jsonObject1.put("key13", "value13");
		
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("key21", "value21");
		jsonObject2.put("key22", "value22");
		jsonObject2.put("key23", "value23");
		
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("key31", "value31");
		jsonObject3.put("key32", "value32");
		jsonObject3.put("key33", "value33");
		
		JSONObject jsonObject4 = new JSONObject();
		jsonObject4.put("key41", "value41");
		jsonObject4.put("key42", "value42");
		jsonObject4.put("key43", "value43");
		
		JSONObject jsonObject5 = new JSONObject();
		jsonObject5.put("key51", "value51");
		jsonObject5.put("key52", "value52");
		jsonObject5.put("key53", "value53");
		
		//Open Connection to solace broker
		ConnectionFactory connectionFactory = new JmsConnectionFactory(solaceUsername, solacePassword, solaceHost);
		JMSContext context = connectionFactory.createContext();
		
		//Create Queue
		Queue queue = context.createQueue(solaceQueue);
		
		//Create Producer
		JMSProducer producer = context.createProducer();
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//Create Consumer
		JMSConsumer consumer = context.createConsumer(queue);
		
		//Send JSON Object
		producer.send(queue, jsonObject1.toString());
		producer.send(queue, jsonObject2.toString());
		producer.send(queue, jsonObject3.toString());
		producer.send(queue, jsonObject4.toString());
		producer.send(queue, jsonObject5.toString());
		
		//Wait 1 minute
		try {
			//Thread.sleep(60000);
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.warn("Thread Sleep Interrupted: " + e.getMessage());
		}
		
		//Read JSON Object
		int timeout = 10000; //10 seconds timeout
		String msg1 = consumer.receiveBody(String.class, timeout);
		String msg2 = consumer.receiveBody(String.class, timeout);
		String msg3 = consumer.receiveBody(String.class, timeout);
		String msg4 = consumer.receiveBody(String.class, timeout);
		String msg5 = consumer.receiveBody(String.class, timeout);
		
		assertEquals(jsonObject1.toString(), msg1);
		assertEquals(jsonObject2.toString(), msg2);
		assertEquals(jsonObject3.toString(), msg3);
		assertEquals(jsonObject4.toString(), msg4);
		assertEquals(jsonObject5.toString(), msg5);
	}
	
	
}
