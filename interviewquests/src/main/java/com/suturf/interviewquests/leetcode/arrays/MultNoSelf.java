package com.suturf.interviewquests.leetcode.arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Given an integer array nums, return an array answer such that answer[i] is equal to the product 
 * of all the elements of nums except nums[i]. The product of any prefix or suffix of nums is 
 * guaranteed to fit in a 32-bit integer. You must write an algorithm that runs in O(n) time and 
 * without using the division operation.
 * 
 * @author suvendra
 *
 */
public class MultNoSelf {

	private static final Logger log = LoggerFactory.getLogger(MultNoSelf.class);

	public int[] multArray(final int[] numbers) {
		
		// Multiply towards the right first, then come back left
		final int lenNum = numbers.length;
		final int[] retArr = new int[lenNum];
		int prod = 1;
		
		// All left
		for (int i=0; i < lenNum; i++) {
			retArr[i] = prod;
			prod *= numbers[i];
		}
		
		prod = 1;
		// All right
		for (int i=lenNum - 1; i >= 0; i--) {
			retArr[i] *= prod;
			prod *= numbers[i];
		}
		
		return retArr;
	}
	
	public static void main(final String [] args) {
		final int[] nums1 = {-1,1,0,-3,3};
		final int[] nums2 = {1,2,3,4};
		
		final MultNoSelf ns = new MultNoSelf();
		int[] arr = ns.multArray(nums1);
		log.info("--------------- A --------------");
		for (int arrEach : arr) {
			log.info("--> {}", arrEach);
		}
		
		arr = ns.multArray(nums2);
		log.info("--------------- B --------------");
		for (int arrEach : arr) {
			log.info("--> {}", arrEach);
		}
	}
}
