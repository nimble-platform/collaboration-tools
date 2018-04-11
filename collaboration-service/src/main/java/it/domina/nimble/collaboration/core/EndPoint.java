package it.domina.nimble.collaboration.core;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.nimble.collaboration.ServiceConfig;
import it.domina.nimble.collaboration.config.EPartner;
import it.domina.nimble.collaboration.services.type.CollabMessageType;
import it.domina.nimble.collaboration.services.type.ReadMessageType;

public class EndPoint {

	private static final Logger logger = Logger.getLogger(EndPoint.class);

	
	private Connector conn; 
	private EPartner partner; 
    private final KafkaProducer<String, String> kafkaProducer;
    private final KafkaConsumer<String, String> kafkaConsumer;

    public EndPoint (Connector cn, EPartner p) throws Exception{
    	this.conn = cn;
    	this.partner = p;
    	this.kafkaProducer = ServiceConfig.getInstance().getKafkaConfig().newProducer(p, this.conn.getKafkaTopic());
    	this.kafkaConsumer = ServiceConfig.getInstance().getKafkaConfig().newConsumer(p, this.conn.getKafkaTopic());
    	readMsg(new ReadMessageType());
    }
    
    public EPartner getPartner() {
    	return this.partner;
    }
    
    public Boolean sendMsg(CollabMessageType msg) {
    	try {
    		
    		if (msg.getUniqueID()==null) {};
    		String key = msg.getUniqueID();
    		String message = msg.toString();

            ProducerRecord<String, String> record = new ProducerRecord<String, String>(conn.getKafkaTopic(), key, message);
            // Send record asynchronously
            Future<RecordMetadata> future = this.kafkaProducer.send(record);
            // Synchronously wait for a response from Message Hub / Kafka on every message produced.
            // For high throughput the future should be handled asynchronously.
            RecordMetadata recordMetadata = future.get(5000, TimeUnit.MILLISECONDS);
            
            logger.log(Level.INFO, "Message produced, offset: " + recordMetadata.offset());
            return true;
		} catch (Exception e) {
			logger.log(Level.ERROR,e);
			return false;
		}
    }
	
    public Boolean readMsg(ReadMessageType params) {

        try {
            // Poll on the Kafka consumer, waiting up to 3 secs if there's nothing to consume.
            ConsumerRecords<String, String> records = this.kafkaConsumer.poll(3000);
            
            if (records.isEmpty()) {
                logger.log(Level.INFO, "No messages consumed");
            } else {
                // Iterate through all the messages received and print their content
                for (ConsumerRecord<String, String> record : records) {
                	logger.log(Level.INFO, "Message consumed: " + record.toString());
                    CollabMessageType msg = CollabMessageType.mapJson(record.value());
                    params.getMessages().add(msg);
                }
            }
            
            return true;
            
		} catch (Exception e) {
			logger.log(Level.ERROR,e);
			return false;
        } 
    }

    public void close() {
    	this.conn = null;
    	this.kafkaConsumer.close();
    	this.kafkaProducer.close(5000, TimeUnit.MILLISECONDS);
    }
    
}
