package com.suturf.interviewquests.others;

/**
 * 
 * This is a fence coloring problem. How many ways can I paint n fences so that simultaneous three
 * colors are it the same. Assume two colors.
 * 
 * Blue: 0
 * Green: 1
 * 
 * Transition function
 * F(i, j) = Post number i painted with color j
 * F(i, j) = F(i-1, 1-j) + F(i-2, 1-j)
 * 
 * @author suvendra
 *
 */
public class FenceColor {
	
	public int totalWaysToColor(final int posts) {
		
		final Integer[][] ways = new Integer[posts+1][2]; // only green or blue, so 2
		
		ways[1][0] = 1;
		ways[1][1] = 1;
		ways[2][0] = 2;	// 10, 00
		ways[2][1] = 2; // 01, 11
		
		for (int i=3; i<=posts; i++) {
			for (int j=0; j<=1; j++) {
				ways[i][j] = ways[i-1][1-j] + ways[i-2][1-j];
			}
		}
		
		return ways[posts][0] + ways[posts][1];
	}
	
	public static void main (final String [] args) {
		
		final FenceColor fc = new FenceColor();
		System.out.println(fc.totalWaysToColor(4));
	}
}
