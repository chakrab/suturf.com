package com.sc.nlp.teststanford;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.ejml.simple.SimpleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyzer {
	private StanfordCoreNLP CORENLP;
	private static final Logger LOG = LoggerFactory.getLogger(SentimentAnalyzer.class);

	public SentimentAnalyzer() {
		
	}
	
	public void getAnnotators(final String text) {
		
		final Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, pos, lemma, ner, coref");
		CORENLP = new StanfordCoreNLP(props);
		
		final Annotation doc = new Annotation(text);
		CORENLP.annotate(doc);
		
		for (final CorefChain cc : doc.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
			LOG.info(cc.toString());
		}
		
		final List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
		for(final CoreMap sentence: sentences) {
			for (final CoreLabel word: sentence.get(TokensAnnotation.class)) {
				LOG.info("Word: {}, POS: {}, NER: {}", 
						word.get(TextAnnotation.class),
						word.get(PartOfSpeechAnnotation.class),
						word.get(NamedEntityTagAnnotation.class));
			}
			
			final Tree tree = sentence.get(TreeAnnotation.class);
			LOG.info(tree.toString());
			
			final SemanticGraph graph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			final Collection<TypedDependency> deps = graph.typedDependencies();
			for (TypedDependency td : deps) {
				LOG.info(td.toString());
		    }
		}
	}

	public void getSentiment(final String text) {
		final Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		CORENLP = new StanfordCoreNLP(props);
		
		int posSentimentVal = 0, ntrSentimentValue = 0, negSentimentValue = 0;
		
		final Annotation doc = CORENLP.process(text);
		for(final CoreMap sentence : doc.get(SentencesAnnotation.class)) {
			int sentimentVal;
			String sentimentName;
			LOG.info(sentence.toString());

			final Tree tree = sentence.get(SentimentAnnotatedTree.class);
			sentimentVal = RNNCoreAnnotations.getPredictedClass(tree);
			if (sentimentVal < 2) negSentimentValue += 1;
			else if (sentimentVal > 2) posSentimentVal += 1;
			else ntrSentimentValue += 1;

			sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
			LOG.info("Sentiment: {}, value: {}", sentimentVal, sentimentName);
		}
		
		LOG.info("Positive: {}, Neutral: {}, Negative: {}", posSentimentVal, ntrSentimentValue, negSentimentValue); 
	}
}
