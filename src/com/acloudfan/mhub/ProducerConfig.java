package com.acloudfan.mhub;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * 
 * @author acloudfan
 *
 *         This is where the consumer configuration is carried out. Properties
 *         object created here is used in KafkaConsumer instance
 */
public class ProducerConfig {
	// Following information comes from the MessageHub Credentials file
	// CHANGE THIS INFORMATION TO POINT TO YOUR MESSAGE HUB INSTANCE ON BLUEMIX
//	static	String	apiUrl="https://kafka-rest-prod01.messagehub.services.us-south.bluemix.net:443";
//	static  String  apiKey="eaAHkjjCrkC6r60apV5WLKUhX2Txb0k19d8TXufTpsXFTEe2";
//	
//	static  String  apiUser="eaAHkjjCrkC6r60a";
//	static  String  apiPassword="pV5WLKUhX2Txb0k19d8TXufTpsXFTEe2";
	
	static String[] brokers=
		{"kafka01-prod01.messagehub.services.us-south.bluemix.net:9093",
	     "kafka02-prod01.messagehub.services.us-south.bluemix.net:9093",
	     "kafka03-prod01.messagehub.services.us-south.bluemix.net:9093",
	     "kafka04-prod01.messagehub.services.us-south.bluemix.net:9093",
	     "kafka05-prod01.messagehub.services.us-south.bluemix.net:9093"};

	/** 
	 * Sets the properties for producer
	 * http://kafka.apache.org/07/configuration.html
	 */
	public  static Properties getProperties(){
		Properties	props = readLocalProps();
		
		if(props == null){
			System.out.println("Producer using HARDcoded properties");
			props = new Properties();
		} else {
			return props;
		}
		
		props.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		// type can async or sync
		props.setProperty("producer.type", "sync");
		props.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.setProperty("bootstrap.servers", "");

		props.setProperty("client.id", "message-hub-sample");
		props.setProperty("acks", "-1");
		
		// set the broker
		// Also check the property broker.list
		props.put("bootstrap.servers", brokers[0]);
		
		// Security configuration
		props.setProperty("security.protocol", "SASL_SSL");
		props.setProperty("sasl.mechanism", "PLAIN");
		props.setProperty("ssl.protocol", "TLSv1.2");
		props.setProperty("ssl.enabled.protocols", "TLSv1.2");
		props.setProperty("ssl.truststore.location", "C:\\Program Files\\Java\\jre1.8.0_71\\lib\\security\\cacerts");
		props.setProperty("ssl.truststore.password", "changeit");
		props.setProperty("ssl.truststore.type", "JKS");
		props.setProperty("ssl.endpoint.identification.algorithm", "HTTPS");
		
		return props;
	}
	
	
	/**
	 * Check if local consumer.properties file available - use it instead of hardcoded props
	 */
	private static Properties readLocalProps(){
		Properties props = new Properties();
        InputStream propsStream;
		try {
			String userdir = System.getProperty("user.dir");
            propsStream = new FileInputStream( userdir+ File.separator + "producer.properties");
            props.load(propsStream);
            propsStream.close();
        } catch (IOException e) {
            // ignore exception
            return null;
        }
		System.out.println("Consumer properties read from local file");
		return props;
	}
}
