package com.suturf.interviewquests.educativeio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * You are given a dictionary of words and a large input string. You have to find out whether the 
 * input string can be completely segmented into the words of a given dictionary. The following two 
 * examples elaborate on the problem further. Given a dictionary of words.
 *
 *	apple, apple, pear, pie
 * 
 * Input string of “applepie” can be segmented into dictionary words.
 * 
 * This problem probably can be done easily using a trie
 * 
 * @author Suvendra
 *
 */
public class StringSegment {
	
	private static final Logger log = LoggerFactory.getLogger(StringSegment.class);

	public static class TrieNode {

		protected boolean isEnd;
		// We will keep what word ends here for convenience
		protected String  fullText;
		protected Map<Character, TrieNode> children;
		
		public TrieNode() {
			this.isEnd = false;
			this.fullText = null;
			children = new HashMap<>();
		}
	}
	
	private TrieNode rootNode = new TrieNode();
	
	public TrieNode insertChar(final TrieNode node, final char chr) {
		if (node.children.containsKey(chr)) {
			return node.children.get(chr);
		} else {
			final TrieNode trieNode = new TrieNode();
			node.children.put(chr, trieNode);
			return trieNode;
		}
	}

	public void insertWord(final String aWord) {
		final char[] chars = aWord.toCharArray();
		TrieNode node = rootNode;
		for (char c : chars) {
			node = insertChar(node, c);
		}
		node.isEnd = true;
		node.fullText = aWord;
	}
	
	public TrieNode createWordTrie(final String [] strs) {
		for (final String s : strs) {
			insertWord(s);
		}
		
		return rootNode;
	}
	
	public void printTrie(final TrieNode trie, final String offset) {
		for (final Map.Entry<Character, TrieNode> e : trie.children.entrySet()) {
			if (e.getValue().isEnd) {
				System.out.println(offset + e.getKey() + " <-");
			} else {
				System.out.println(offset + e.getKey());
			}
			printTrie(e.getValue(), offset + ".");
		}
	}
	
	public boolean search(final String str) {
		TrieNode node = rootNode;
		final char[] chars = str.toCharArray();
		for (final char s : chars) {
			node = node.children.get(s);
			if (node == null) {
				return false;
			}
		}
		
		return node.isEnd?true:false;
	}
	
	private void getCompleteWords(final TrieNode node, final List<String> words) {
		
		if (node == null) {
			return;
		}
		
		for (final Map.Entry<Character, TrieNode> nod : node.children.entrySet()) {

			getCompleteWords(nod.getValue(), words);
			if (nod.getValue().isEnd) {
				words.add(nod.getValue().fullText);
			}
		}
	}

	public List<String> startsWith(final String str) {
		TrieNode node = rootNode;
		final char[] chars = str.toCharArray();
		
		// Let's go down till the string
		for (final char s : chars) {
			node = node.children.get(s);
			if (node == null) {
				// No words found
				return null;
			}
		}
		
		// We have a Node from where words start
		final List<String> words = new ArrayList<>();
		getCompleteWords(node, words);
		
		return words;
	}
	
	private TrieNode findMaxWord(final String str, final StringBuilder strb) {
		TrieNode node = rootNode;
		final char[] chars = str.toCharArray();
		for (final char s : chars) {
			final TrieNode node1 = node.children.get(s);
			if (node1 == null) {
				break;
			} else {
				node = node1;
				strb.append(s);
			}
		}
		
		return node!=rootNode?node:null;
	}
	
	// Now to the problem at hand, can we make a complete word?
	public boolean canWeMakeWords(String str) {
		// First find the Max we can go...
		final StringBuilder strb = new StringBuilder();
		TrieNode resp = findMaxWord(str, strb);
		int lenToMatch = str.length();
		int lenMatched = 0;
		while (resp != null) {
			// We got partial word
			log.info("....Searching {}, found part: {}", str, strb.toString());
			lenMatched += strb.toString().length();
			str = str.substring(strb.toString().length());
			if (lenToMatch == lenMatched) {
				return true;
			}
			strb.setLength(0);
			resp = findMaxWord(str, strb);
		}
		
		return false;
	}
	

	public static void main (final String[] args) {
		final String[] strLst = {"apple", "appeal", "pear", "pie", "dessert", "desert", "deserted"};
		
		final StringSegment ss = new StringSegment();
		final TrieNode node = ss.createWordTrie(strLst);
		ss.printTrie(node, "");
		
		// Search
		log.info("Word {} present: {}", "pie", ss.search("pie"));
		log.info("Word {} present: {}", "app", ss.search("app"));
		
		// Partial Search
		final List<String> words = ss.startsWith("des");
		log.info("No of Words: {}", words.size());
		for (final String word : words) {
			log.info("\t{}", word);
		}
		
		// Word Find
		log.info("Word {} can be formed: {}", "applepie", ss.canWeMakeWords("applepie"));
		log.info("Word {} can be formed: {}", "applepear", ss.canWeMakeWords("applepeer"));
	}
}
