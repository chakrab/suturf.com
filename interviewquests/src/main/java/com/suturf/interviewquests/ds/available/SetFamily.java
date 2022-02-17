package com.suturf.interviewquests.ds.available;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Set Family contains HashSet, LinkedHashSet, and TreeSet.
 * 
 * - HashSet: You cannot have duplicate keys, order is not maintained
 * - LinkedHashSet - Cannot have duplicates, but maintains insertion order
 * - TreeSet - Cannot insert duplicates, sorts all keys
 * 
 * @author suvendra
 *
 */
public class SetFamily {

	private static final Logger log = LoggerFactory.getLogger(SetFamily.class);
	
	private final String [] strs = {"Lemon", "Orange", "Mango", "Pineapple", "Grapes", "Banana",
			"Peach", "Kiwi", "Strawberry", "Raspberry"};
	
	public void testTheSets() {
		
		log.debug("--------- HASH Set ----------");
		final Set<String> hashSet = new HashSet<>();
		for (final String str : strs) {
			hashSet.add(str);
		}
		hashSet.stream().forEach(x -> log.debug(" --> {}", x));
		
		log.debug("--------- LINKEDHASH Set ----------");
		final Set<String> lhashSet = new LinkedHashSet<>();
		for (final String str : strs) {
			lhashSet.add(str);
		}
		lhashSet.stream().forEach(x -> log.debug(" --> {}", x));
		
		log.debug("--------- TREE Set ----------");
		final Set<String> treeSet = new TreeSet<>();
		for (final String str : strs) {
			treeSet.add(str);
		}
		treeSet.stream().forEach(x -> log.debug(" --> {}", x));
	}
	
	public static void main (final String [] args) {
		
		final SetFamily sf = new SetFamily();
		sf.testTheSets();
	}
}
