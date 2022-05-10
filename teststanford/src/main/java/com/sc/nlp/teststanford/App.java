package com.sc.nlp.teststanford;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {

	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	private static String cusReview1 = "This hair oil is the best in Paris and worked great on my hair. I will definitely buy it again.";
	private static String cusReview2 = "The movie sucked. One of the worst movies in my life.";
	private static String cusReview3 = "This airlines was great a year back. But now the service is deteriorating";
	
	public static void main(String[] args) {
		final SentimentAnalyzer sa = new SentimentAnalyzer();
		sa.getAnnotators(cusReview1);
		LOG.info("--------------------------------");
		sa.getSentiment(cusReview1);
		LOG.info("--------------------------------");
		sa.getSentiment(cusReview2);
		LOG.info("--------------------------------");
		sa.getSentiment(cusReview3);
	}
}
