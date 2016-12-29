package com.acloudfan.mhub;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * Main class for the Message Hub Consumer
 */

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.internals.FutureRecordMetadata;

public class Producer {
	static private KafkaProducer<byte[], byte[]> kafkaProducer;
	static private String topic="";
	
	
	// Main method
	public static void main(String[] args)
	throws Exception {
		// Suppress warnings - ignore
		Util.suppressSLF4JWarnings();
		// This sets the jaas.conf as a system property
		Util.setJaasProperty();
		
		// Check if the topic is provided
		if(args.length < 1){
			System.out.println("Usage:  > java -jar consumer.jar  topic");
			System.exit(1);
		}
		
		// Set the topic and group Id
		topic = args[0];
	
		//#1 Initialize the consumer
		init();

		/**
		 * Loop - gets the input from user and sends a message to the specified topic
		 * Loop - ends when the user input=exit
		 */
		//#2 Scan for message input and send message to topic
		while(true){
			
			String readMsg = Util.getUserInput();
			
			// Create the ProducerRecord
			ProducerRecord<byte[], byte[]> record = new ProducerRecord<byte[], byte[]>(topic,readMsg.getBytes("UTF-8"));

			//#2.1 Send the message
			Future<RecordMetadata> fut =  kafkaProducer.send(record);
			
			//#2.2 Get the meta-data for the message sent
			RecordMetadata  metadata = fut.get();
			
			// Print information on the message sent
			System.out.println("Sent {offset="+metadata.offset()+", data="+readMsg+"}");
			
			if(readMsg.equals("exit")){
				break;
			}
		}
		
		
		//#3 close producer
		shutdown();
	}
	
	/**
	 * 1. Initialize the KafkaConsumer
	 */
	private static void init(){
		// If you want to change the producer props change it in ProducerConfig Class
		// or you may change it in producer.properties
		Properties props = ProducerConfig.getProperties();
		
		// You can override any property here
		//props.setProperty("prop", "value");
		
		// Create an instance of the consumer
		kafkaProducer=new KafkaProducer<byte[], byte[]>(props);
	}
	
	/**
	 * 3. Unsubscribe & close
	 */
	private static void shutdown(){
		kafkaProducer.close();
		System.out.println("Producer closed");
	}
	
}
