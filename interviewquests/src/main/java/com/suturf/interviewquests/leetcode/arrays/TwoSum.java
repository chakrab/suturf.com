package com.suturf.interviewquests.leetcode.arrays;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Given an array of integers nums and an integer target, return indices of the two numbers such that 
 * they add up to target. You may assume that each input would have exactly one solution, and you may 
 * not use the same element twice. You can return the answer in any order.
 * 
 * @author suvendra
 *
 */
public class TwoSum {

	private static final Logger log = LoggerFactory.getLogger(TwoSum.class);

	// Time complexity O(n^2)
	public int[] bruteForce(final int[] numbers, final int target) {
		
		final int[] retVal = new int[2];
		for (int ix = 0; ix < numbers.length; ix++) {
			for (int jx = ix + 1; jx < numbers.length; jx++) {
				int m = numbers[ix];
				int n = numbers[jx];
				if (m + n == target) {
					retVal[0] = ix;
					retVal[1] = jx;
					log.debug("Returning {}({}) & {}({})", ix, m, jx, n);
					return retVal;
				}
			}
		}
		
		log.debug("Combinatioon not found");
		return null;
	}
	
	// Time complexity O(n)
	public int[] betterMethod(final int[] numbers, final int target) {
		
		final int[] retVal = new int[2];
		
		// Move this to a HashMap (O(n))
		final Map<Integer, Integer> intMap = new HashMap<>();
		
		// For each number find one that matches
		// eventual Time complexity O(n)
		for (int ix = 0; ix < numbers.length; ix++) {
			int diff = target - numbers[ix];
			if (intMap.containsKey(diff)) {
				retVal[0] = ix;
				retVal[1] = intMap.get(diff);
				log.debug("Returning {}({}) & {}({})", retVal[0], numbers[ix], retVal[1], diff);
				return retVal;
			} else if (diff > 0) { // Else the number is larger
				intMap.put(numbers[ix], ix);
			}
		}
		
		log.debug("Combinatioon not found");
		return null;
	}
	
	public static void main (final String [] args) {
		final int[] numbers = {1, 3, 6, 8, 4, 9};
		final TwoSum twoSum = new TwoSum();
		
		log.debug("Trying brute force....");
		twoSum.bruteForce(numbers, 5);
		twoSum.bruteForce(numbers, 2);
		
		log.debug("Trying better method....");
		twoSum.betterMethod(numbers, 5);
		twoSum.betterMethod(numbers, 2);
	}
}
