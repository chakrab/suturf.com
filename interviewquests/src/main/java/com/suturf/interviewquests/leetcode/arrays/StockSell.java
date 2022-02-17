package com.suturf.interviewquests.leetcode.arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * You are given an array prices where prices[i] is the price of a given stock on the ith day. You want 
 * to maximize your profit by choosing a single day to buy one stock and choosing a different day in the 
 * future to sell that stock. Return the maximum profit you can achieve from this transaction. If you 
 * cannot achieve any profit, return 0.
 *
 * @author suvendra
 *
 */
public class StockSell {
	
	private static final Logger log = LoggerFactory.getLogger(StockSell.class);

	public int maximizeProfit(final int[] prices) {

		int len = prices.length;
        if (len <= 1) return 0;
        int max = 0;
        int low = prices[0];
        for (int i=1; i<len; i++) {
            max = prices[i] - low > max ? prices[i] - low : max;
            low = prices[i] < low ? prices[i] : low;
        }

        return max;
	}

	public static void main (final String [] args) {
		
		final int[] values1 = {7, 1, 5, 3, 6, 4};
		final StockSell ss = new StockSell();
		log.info("{}", ss.maximizeProfit(values1));
		
		final int[] values2 = {7, 6, 4, 3, 1};
		log.info("{}", ss.maximizeProfit(values2));
	}
}
