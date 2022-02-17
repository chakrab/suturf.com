package com.suturf.interviewquests.educativeio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * You are given an array of positive numbers from 1 to n, such that all numbers from 1 to n are
 * present except one number x. You have to find x. The input array is not sorted.
 * 
 * @author Suvendra
 *
 */
public class MissingNumber {

	private static final Logger log = LoggerFactory.getLogger(MissingNumber.class);
	
	public int missingNumber(final int[] arrNum) {
		
		int cnt = 0, sum = 0, min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int num : arrNum) {
			cnt++;
			sum += num;
			min = num > min?min:num;
			max = num > max?num:max;
		}
		log.info("Count: {}, Min: {}, Max: {}, Sum: {}", cnt, min, max, sum);
		
		// Series Sum: cnt/2 * (min + max)
		final int exp_sum = (int)(((cnt + 1)/2.0) * (min + max));
		log.info("Expected sum: {}", exp_sum);
		
		return (exp_sum - sum);
	}
	
	public static void main(final String [] args) {
		final int[] arr = {3, 7, 1, 2, 8, 4, 5};
		
		final MissingNumber ms = new MissingNumber();
		log.info("Missing number is: {}", ms.missingNumber(arr));
	}
}
