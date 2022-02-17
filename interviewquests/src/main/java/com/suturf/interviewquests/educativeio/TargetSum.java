package com.suturf.interviewquests.educativeio;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Given an array of integers and a value, determine if there are any two integers in the array 
 * whose sum is equal to the given value. Return true if the sum exists and return false if it does not.
 * 
 * @author Suvendra
 *
 */
public class TargetSum {

	private static final Logger log = LoggerFactory.getLogger(TargetSum.class);

	public boolean targetSumFinder(final int[] arr, final int theSum) {
		
		final Set<Integer> visited = new HashSet<>();
		for (int num : arr) {
			int num_to_target = theSum - num;
			if (visited.contains(num_to_target)) {
				log.info("{} + {} = {}", num, num_to_target, theSum);
				return true;
			} else {
				visited.add(num);
			}
		}
		return false;
	}

	public static void main (final String [] args) {
		
		final int[] arr = {5, 7, 1, 4, 2, 8, 3};
		final TargetSum ts = new TargetSum();
		log.info("Result for 10 is: {}", ts.targetSumFinder(arr, 10));
		log.info("Result for 19 is: {}", ts.targetSumFinder(arr, 19));
	}
}
