package com.suturf.interviewquests.educativeio;

/**
 * 
 * Search for a given number in a sorted array, with unique elements, that has been rotated by some arbitrary number. 
 * Return -1 if the number does not exist. Assume that the array does not contain duplicates
 * 
 * Example Array:
 * 176, 188, 199, 200, 210, 222, 1, 10, 20, 47, 59, 63, 75, 88, 99, 107, 120, 133, 155, 162
 * 
 * @author suvendra
 *
 */
public class RotatedArray {

	public int findRotationKey(final int[] arr, final int low, final int high) {
		
		if (low > high) {
			return -1;
		}
		
		final int mid = (low + high)/ 2;
		if (arr[mid] < arr[mid - 1]) {
			return mid - 1;
		}
		if (arr[mid+1] < arr[mid]) {
			return mid;
		}
		
		// Regular Binary Search
		if (arr[low] >= arr[mid]) {
			return findRotationKey(arr, low, mid-1);
		}
		
		return findRotationKey(arr, mid+1, high);
	}
	
	public int[] rearrangeArray(final int [] arr, final int rotationKey) {
		
		
	}
	
	public static void main (final String [] args) {
		final RotatedArray ra = new RotatedArray();
		
		final int[] arr = {176, 188, 199, 200, 210, 222, 1, 10, 20, 47, 59, 63, 75, 88, 99, 107, 120, 133, 155, 162};
		System.out.println("findRotationKey: " + ra.findRotationKey(arr, 0, 20-1));
	}
}
