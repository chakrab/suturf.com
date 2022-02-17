package com.suturf.interviewquests.leetcode.arrays;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Given an integer array nums, return true if any value appears at least twice in the array, and 
 * return false if every element is distinct.
 * 
 * @author suvendra
 *
 */
public class DupeCheck {
	
	private static final Logger log = LoggerFactory.getLogger(DupeCheck.class);

	public boolean hasDupes(final int[] numbers) {
		
		final Set<Integer> uniques = new HashSet<>();
		for (int num : numbers) {
			if (!uniques.add(num)) {
				return true;
			}
		}

		return false;
	}

	public static void main (final String [] args) {
		
		final int[] nums1 = {1,1,1,3,3,4,3,2,4,2};
		final int[] nums2 = {1,5,7,3,0,4,8,2,6,9};
		final DupeCheck dc = new DupeCheck();
		log.info("{}", dc.hasDupes(nums1));
		log.info("{}", dc.hasDupes(nums2));
	}
}
