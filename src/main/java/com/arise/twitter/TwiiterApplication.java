package com.arise.twitter;

import java.util.List;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arise.kafka.IKafkaConstants;
import com.arise.kafka.KafkaConsumer;
import com.arise.kafka.ProducerCreator;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@SpringBootApplication
public class TwiiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwiiterApplication.class, args);

		System.out.println("Hello SANSAN");

		List<Status> statuses = null;
		ConfigurationBuilder cf = new ConfigurationBuilder();
		cf.setDebugEnabled(true).setOAuthConsumerKey("abc").setOAuthConsumerSecret("abc").setOAuthAccessToken("abc")
				.setOAuthAccessTokenSecret("abc");

		TwitterFactory tf = new TwitterFactory(cf.build());
		twitter4j.Twitter twitter = tf.getInstance();

		try {
			statuses = twitter.getHomeTimeline();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Producer<Long, String> producer = ProducerCreator.createProducer();
		statuses.forEach(status -> {
			// System.out.println(status.getUser().getName() + " " +
			// status.getText());
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
					status.getText());

			// try {
			producer.send(record);
		});

		// KafkaProducer.runProducer();
		KafkaConsumer.runConsumer();
	}
}
