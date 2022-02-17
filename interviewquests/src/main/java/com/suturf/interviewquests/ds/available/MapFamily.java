package com.suturf.interviewquests.ds.available;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Map Family contains HashMap, HashTable, TreeMap, ConcurrentHashMap, and LinkedHashMap
 * 
 * - HashMap is implemented as a hash table, and there is no ordering on keys or values
 * - TreeMap is implemented based on red-black tree structure, and it is ordered by the key
 * - LinkedHashMap preserves the insertion order
 * - Hashtable  is synchronized in contrast to HashMap
 * 
 * @author suvendra
 *
 */
public class MapFamily {

	private static final Logger log = LoggerFactory.getLogger(MapFamily.class);
	
	private static final String [][] items = {
			{"Mercury", ".387 AU"}, {"Venus", ".722 AU"}, {"Earth", "1 AU"},
			{"Mars", "1.52 AU"}, {"Jupiter", "5.20 AU"}, {"Saturn", "9.58 AU"},
			{"Uranus", "19.2 AU"}, {"Neptune", "30.1 AU"}, {"Pluto", "39.5 AU"}
	};
	
	private void printMap(final Map<String, String> map) {
		map.forEach((key, value) -> log.debug("  {} -> {}", key, value));
	}
	
	public void allMapClasses() {
		
		log.debug("---------- {} ----------", "Hash Table");
		final Map<String, String> hashTable = new Hashtable<>();
		for (int i=0; i<items.length; i++) {
			hashTable.put(items[i][0], items[i][1]);
		}
		printMap(hashTable);
		
		log.debug("---------- {} ----------", "Hash Map");
		final Map<String, String> hashMap = new HashMap<>();
		for (int i=0; i<items.length; i++) {
			hashMap.put(items[i][0], items[i][1]);
		}
		printMap(hashMap);
		
		log.debug("---------- {} ----------", "Linked Hash Map");
		final Map<String, String> lhashMap = new LinkedHashMap<>();
		for (int i=0; i<items.length; i++) {
			lhashMap.put(items[i][0], items[i][1]);
		}
		printMap(lhashMap);
		
		log.debug("---------- {} ----------", "Tree Map");
		final Map<String, String> treeMap = new TreeMap<>();
		for (int i=0; i<items.length; i++) {
			treeMap.put(items[i][0], items[i][1]);
		}
		printMap(treeMap);
	}
	
	public static void main (final String [] args) {
		final MapFamily mapF = new MapFamily();
		mapF.allMapClasses();
	}
}
