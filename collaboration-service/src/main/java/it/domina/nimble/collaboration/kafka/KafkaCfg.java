package it.domina.nimble.collaboration.kafka;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import it.domina.nimble.collaboration.config.EPartner;
import it.domina.nimble.collaboration.kafka.bluemix.BluemixEnvironment;
import it.domina.nimble.collaboration.kafka.bluemix.MessageHubCredentials;

public class KafkaCfg {

	/*
	private static KafkaCfg instance;
	
	public static KafkaCfg getInstance() {
		if (instance==null) {
			instance = new KafkaCfg();
		}
		return instance;
	}
	
	*/
	private static final long _24H_IN_MILLISECONDS = 3600000L*24;
	
	private static final String APP_NAME = "collaboration-service";

	private static final Logger logger = Logger.getLogger(KafkaCfg.class);

    private String 		userDir;
    private Boolean 	isRunningInBluemix;

    //KAFKA PARAMS
    private String 		resourceDir;
    private String 		bootstrapServers = null;
    private String 		adminRestURL = null;
    private String 		apiKey = null;
    private String 		user = null;
    private String 		password = null;

    
    public KafkaCfg(String userdir) {
		try {
			Properties applicationProperties = new Properties();
			this.userDir = userdir;
			this.isRunningInBluemix = BluemixEnvironment.isRunningInBluemix();
	        System.out.println(userDir);
	        
	        // Check environment: Bluemix vs Local, to obtain configuration parameters
	        if (this.isRunningInBluemix) {
	
	            logger.log(Level.INFO, "Running in Bluemix mode.");
	            this.resourceDir = this.userDir + File.separator + APP_NAME + File.separator + "bin" + File.separator + "resources";
	
	            MessageHubCredentials credentials = BluemixEnvironment.getMessageHubCredentials();
	            
	            this.bootstrapServers = stringArrayToCSV(credentials.getKafkaBrokersSasl());
	            this.adminRestURL = credentials.getKafkaRestUrl();
	            this.apiKey = credentials.getApiKey();
	            this.user = credentials.getUser();
	            this.password = credentials.getPassword();
	
	        } else {
	            // If running locally, parse the command line
	            logger.log(Level.INFO, "Running in local mode.");
	            this.resourceDir = this.userDir;
	            loadClientConfiguration(applicationProperties, "kafka.properties");
	            
	            this.bootstrapServers = applicationProperties.getProperty("kafka_brokers_sasl");
	            this.adminRestURL = applicationProperties.getProperty("kafka_admin_url");
	            this.apiKey = applicationProperties.getProperty("kafka.api_key");
	            this.user = apiKey.substring(0, 16);
	            this.password = apiKey.substring(16);
	        	
	        }

	        logger.log(Level.INFO, "Kafka Endpoints: " + this.bootstrapServers);
	        logger.log(Level.INFO, "Admin REST Endpoint: " + this.adminRestURL);
	
	        //logger.log(Level.INFO, "MessageHubConsoleSample will run until interrupted.");
	    } catch (Exception e) {
	        logger.log(Level.ERROR, "Exception occurred, application will terminate", e);
	        System.exit(-1);
	    }
    	
    }
    
    public String getApiKey() {
    	return this.apiKey;
    }
    	
    public String getAdminRestURL() {
    	return this.adminRestURL;
    }
    
    /**
     * Returns all the topics available to the user in a single string
     * <p/>
     * @param restURL HTTPS endpoint URL
     * @param apiKey Message Hub API Key 
     * @return all the topics available to the user in a single string
     * @throws Exception if an unexpected error occurs
     */
    public List<String> getTopicsList() throws Exception {
    	List<String> lstOut = new Vector<String>();
        RESTRequest restApi = new RESTRequest(this.adminRestURL, this.apiKey);
        String topic = restApi.get("/admin/topics", false); 
        String[] arr = topic.split(",");
        for (int i = 0; i < arr.length; i++) {
        	lstOut.add(arr[i]);
		}
        return lstOut;
    }
    
    /**
     * Creates a topic or ignores an 'Already Exists' response
     * <p/>
     * @param topicName Name of the topic
     * @return the body of the HTTP response
     * @throws Exception if an unexpected error occurs
     */
    public String createTopic(String topicName) throws Exception {

        RESTRequest restApi = new RESTRequest(this.adminRestURL, this.apiKey);
        // Create a topic, ignore a 422 response - this means that the
        // topic name already exists.
        return restApi.post("/admin/topics",
                new CreateTopicParameters(topicName, 
                        1 /* one partition */, 
                        new CreateTopicConfig(_24H_IN_MILLISECONDS)).toString(),
                new int[] { 422 });
    }
    
    public Properties getClientConfiguration() {
        Properties result = new Properties();
        result.put("bootstrap.servers", this.bootstrapServers);
        InputStream propsStream;

        try {
            propsStream = new FileInputStream(resourceDir + File.separator + "consumer.properties");
            result.load(propsStream);
            propsStream.close();
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not load properties from file");
            return result;
        }
        //Adding in credentials for MessageHub auth
        String saslJaasConfig = result.getProperty("sasl.jaas.config");
        saslJaasConfig = saslJaasConfig.replace("USERNAME", this.user).replace("PASSWORD", this.password);
        result.setProperty("sasl.jaas.config", saslJaasConfig);
        return result;
    }
    
    public KafkaProducer<String, String> newProducer(EPartner p, String topic){
        Properties clientProperties = new Properties();
        clientProperties.put("bootstrap.servers", this.bootstrapServers);
        Properties producerProperties = getClientConfiguration(clientProperties, "producer.properties", user, password);
        //producerProperties.setProperty("client.id", "kafka_producer_" + p.getUserId());
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(producerProperties);
        try {
            // Checking for topic existence.
            // If the topic does not exist, the kafkaProducer will retry for about 60 secs
            // before throwing a TimeoutException
            // see configuration parameter 'metadata.fetch.timeout.ms'
            List<PartitionInfo> partitions = producer.partitionsFor(topic);
            logger.log(Level.INFO, partitions.toString());
        } catch (TimeoutException kte) {
            logger.log(Level.ERROR, "Topic '" + topic + "' may not exist - application will terminate");
            producer.close();
            throw new IllegalStateException("Topic '" + topic + "' may not exist - application will terminate", kte);
        }
    	return producer;
    }

    public KafkaConsumer<String, String> newConsumer(EPartner p, String topic){
        Properties clientProperties = new Properties();
        clientProperties.put("bootstrap.servers", this.bootstrapServers);
        Properties consumerProperties = getClientConfiguration(clientProperties, "consumer.properties", user, password);
        consumerProperties.setProperty("client.id", "kafka_consumer_" + p.getUserId());
        //consumerProperties.setProperty("group.id", p.getUserId()+"_group");
        // Create a Kafka consumer with the provided client configuration
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(consumerProperties);

        // Checking for topic existence before subscribing
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);
        if (partitions == null || partitions.isEmpty()) {
            logger.log(Level.ERROR, "Topic '" + topic + "' does not exists - application will terminate");
            consumer.close();
            throw new IllegalStateException("Topic '" + topic + "' does not exists - application will terminate");
        } else {
            logger.log(Level.INFO, partitions.toString());
        }
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }

    private Properties getClientConfiguration(Properties commonProps, String fileName, String user, String password) {
        Properties result = new Properties();
        InputStream propsStream;

        try {
            propsStream = new FileInputStream(this.resourceDir + File.separator + fileName);
            result.load(propsStream);
            propsStream.close();
        } catch (IOException e) {
            logger.log(Level.ERROR, "Could not load properties from file");
            return result;
        }

        result.putAll(commonProps);
        //Adding in credentials for MessageHub auth
        String saslJaasConfig = result.getProperty("sasl.jaas.config");
        saslJaasConfig = saslJaasConfig.replace("USERNAME", user).replace("PASSWORD", password);
        result.setProperty("sasl.jaas.config", saslJaasConfig);
        return result;
    }
    
    
    
    private void loadClientConfiguration(Properties applicationProperties, String fileName) {
    
        InputStream propsStream;
        try {
            propsStream = new FileInputStream(this.resourceDir + File.separator + fileName);
            applicationProperties.load(propsStream);
            propsStream.close();
        } catch (IOException e) {
            logger.log(Level.ERROR, e);
        }
    }

    /*
     * Return a CSV-String from a String array
     */
	private static String stringArrayToCSV(String[] sArray) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sArray.length; i++) {
            sb.append(sArray[i]);
            if (i < sArray.length -1) sb.append(",");
        }
        return sb.toString();
    }



}
