package com.example.solace.demo;


import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;


@SpringBootApplication
@EnableScheduling
public class DemoApplication  implements CommandLineRunner {

	
	//@Autowired
	//private JmsTemplate jmsTemplate;
	
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
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	public void run(String... varl) throws Exception {
		//LOGGER.info("property yourSecretPropertyName in Azure Key Vault: {}", mySecretProperty);

		//System.out.println("property yourSecretPropertyName in Azure Key Vault: " + mySecretProperty);

		ConnectionFactory connectionFactory = new JmsConnectionFactory(solaceUsername, solacePassword, solaceHost);
		JMSContext context = connectionFactory.createContext();

		//Create producer
		final String TOPIC_NAME = "TOTO";
		final String QUEUE_NAME = "SpringTestQueue";

		//Topic topic = context.createTopic(TOPIC_NAME);
		//Queue queue = context.createQueue(QUEUE_NAME);
		Queue queue = context.createQueue(solaceQueue);
		TextMessage message = context.createTextMessage("Hello world!");
		JMSProducer producer = context.createProducer();
		//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		//producer.send(topic, message);
		producer.send(queue, message);
		
		//Create consumer 1
		Thread consumerThread1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				final String TOPIC_NAME = "TOTO";
				final String QUEUE_NAME = "SpringTestQueue";
				boolean keepRunning = true;
				//Topic topic = context.createTopic(TOPIC_NAME);
				//Queue queue = context.createQueue(QUEUE_NAME);
				Queue queue = context.createQueue(solaceQueue);
				//JMSConsumer consumer = context.createConsumer(topic);
				JMSConsumer consumer = context.createConsumer(queue);
				
				String message = null;
				while(keepRunning) {
					message = null;
					 message = consumer.receiveBody(String.class);
					 
					 if(message != null) {
						 System.out.println("Received Message: " + message);
						 System.out.flush();
					 }
					 
					 //Sleep
					 try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						System.out.println("Consumer Thread Exit");
					}
				}
				
			}
		});
		
		//Start consumer
		consumerThread1.start();
		
		int i = 0, count = 10;
		for(i = 0; i < count; i++) {
			//producer.send(topic, "Hello " + i);
			producer.send(queue, "Hello " + i);
		}
		
		//Wait on consumer
		consumerThread1.join();
	}
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////
	///    Solace SMF Protocol Integration using solace-jms-spring-boot-starter      ///
	////////////////////////////////////////////////////////////////////////////////////
	/*
	// Consumer
	@JmsListener(destination = "SpringTestQueue")
	public void handle(Message message) {

		Date receiveTime = new Date();

		if (message instanceof TextMessage) {
			TextMessage tm = (TextMessage) message;
			try {
				System.out.println(
						"Message Received at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(receiveTime)
								+ " with message content of: " + tm.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(message.toString());
		}
	}
	
	
	// Producer
	@Value("SpringTestQueue")
	private String queueName;
	
	@PostConstruct
	private void customizeJmsTemplate() {
		// Update the jmsTemplate's connection factory to cache the connection
		CachingConnectionFactory ccf = new CachingConnectionFactory();
		ccf.setTargetConnectionFactory(jmsTemplate.getConnectionFactory());
		jmsTemplate.setConnectionFactory(ccf);

		// By default Spring Integration uses Queues, but if you set this to true you
		// will send to a PubSub+ topic destination
		jmsTemplate.setPubSubDomain(false);
	}

	@Scheduled(fixedRate = 500)
	public void sendEvent() throws Exception {
		String msg = "Hello World " + System.currentTimeMillis();
		System.out.println("==========SENDING MESSAGE========== " + msg);
		jmsTemplate.convertAndSend(queueName, msg);
	}
	*/
	
	
}
