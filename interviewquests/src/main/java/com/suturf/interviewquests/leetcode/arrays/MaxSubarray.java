package com.suturf.interviewquests.leetcode.arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Given an integer array nums, find the contiguous subarray (containing at least one number) which has 
 * the largest sum and return its sum.
 * 
 * Kadane's Algorithm
 * ------------------
 * Kadane's algorithm is a popular solution to the maximum subarray problem and this solution is based on 
 * dynamic programming.
 * 
 * @author suvendra
 *
 */
public class MaxSubarray {
	
	private static final Logger log = LoggerFactory.getLogger(MaxSubarray.class);

	public int maxSubArray(final int [] numbers) {
		
		int size = numbers.length;
		int start = 0;
	    int end = 0;
	    
	    int maxSoFar = numbers[0];
	    int maxEndingHere = numbers[0];
	    
	    for (int i = 0; i < size; i++) {
	    	if (numbers[i] > maxEndingHere + numbers[i]) {
	    		start = i;
	    		maxEndingHere = numbers[i];
	    	} else {
	    		maxEndingHere = maxEndingHere + numbers[i];
	    	}
	    	
	    	if (maxSoFar < maxEndingHere) {
	    		maxSoFar = maxEndingHere;
	    		end = i;
	    	}
	    }
	    
	    log.info("Maximum Subarray start/ end {} and {}", Math.min(start, end), end);
	    return maxSoFar;
	}
	
	public static void main (final String[] args) {
		final int[] num1 = {-2,1,-3,4,-1,2,1,-5,4};
		final int[] num2 = {5,4,-1,7,8};
		
		final MaxSubarray msa = new MaxSubarray();
		log.info("{}", msa.maxSubArray(num1));
		log.info("{}", msa.maxSubArray(num2));
	}
}
