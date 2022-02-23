package com.suturf.interviewquests.others;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * You are climbing a stair case that has n steps. You can take 1 or 2 steps at a time.
 * In how many distinct ways can you climb to the top?
 * 
 * f(n) = f(n-1) + f(n-2)
 * 
 * @author suvendra
 *
 */
public class ClimbingStairs {
	
	// Used for Memoization
	final Map<Integer, Integer> map = new HashMap<>();

	public int usingRecursion(final int cnt) {
		if (cnt == 0 || cnt == 1) {
			return 1;
		}
		
		return (usingRecursion(cnt - 1) + usingRecursion(cnt - 2));
	}
	
	public int usingMeomoization(final int cnt) {
		if (cnt == 0 || cnt == 1) {
			return 1;
		}
		
		int minus1;
		if (map.containsKey(cnt - 1)) {
			minus1 = map.get(cnt - 1);
		} else {
			minus1 = usingRecursion(cnt - 1);
			map.put(cnt - 1, minus1);
		}
		
		int minus2;
		if (map.containsKey(cnt - 2)) {
			minus2 = map.get(cnt - 2);
		} else {
			minus2 = usingRecursion(cnt - 2);
			map.put(cnt - 2, minus2);
		}
		
		return (minus1 + minus2);
	}
	
	public int usingTopDown(final int cnt) {
		final Integer[] table = new Integer[cnt+1];
		table[0] = 1;
		table[1] = 1;
		for (int i=2; i<=cnt; i++) {
			table[i] = table[i-1] + table[i-2];
		}
		
		return table[cnt];
	}
	
	public int reduceSpaceComplexity(final int cnt) {
		int a, b, c = 0;
		a = 1;
		b = 1;
		for (int i=2; i<=cnt; i++) {
			c = a + b;
			a = b;
			b = c;
		}
		
		return c;
	}
	
	/**
	 * Now assume that you can go k steps at a time
	 */
	public int kSteps(final int cntSteps, final int maxSteps) {
		
		final Integer[] table = new Integer[cntSteps+1];
		table[0] = 1;
		for (int i=1; i<=cntSteps; i++) {
			//table[i] = table[i-1] + table[i-2] ... + table[i-k]
			int cnt = 0;
			for (int j=1; j<=maxSteps; j++) {
				if (i - j < 0) {
					continue;
				}

				cnt += table[i-j];
			}
			table[i] = cnt;
		}
		
		return table[cntSteps];
	}
	
	/**
	 * Now assume a paid staircase. You have a staircase with steps stairs and you can climb
	 * either 1 or 2 steps at a time. Minimize cost
	 *  
	 * @param args
	 */
	public int leastCostProblem(final int steps, final int[] cost, final Set<Integer> path) {
		
		final Integer[] minCost = new Integer[steps+1];
		minCost[0] = 0;
		minCost[1] = cost[1];
		for (int i=2; i<= steps; i++) {
			int prevCost = 0;
			if (minCost[i-1] > minCost[i-2]) {
				path.add(i-2);
				prevCost = minCost[i-2];
			} else {
				path.add(i-1);
				prevCost = minCost[i-1];
			}
			minCost[i] = prevCost + cost[i];
		}
		
		path.add(steps);
		return minCost[steps];
	}
	
	public static void main (final String [] args) {
		final ClimbingStairs cs = new ClimbingStairs();
		
		// Using Recursion
		System.out.println(cs.usingRecursion(3));
		System.out.println(cs.usingRecursion(5));
		System.out.println(cs.usingRecursion(20));
		
		// Using Memoization
		System.out.println(cs.usingMeomoization(3));
		System.out.println(cs.usingMeomoization(5));
		System.out.println(cs.usingMeomoization(20));
		
		// Top Down
		System.out.println(cs.usingTopDown(3));
		System.out.println(cs.usingTopDown(5));
		System.out.println(cs.usingTopDown(20));
		
		// Reduce Space Complexity
		System.out.println(cs.reduceSpaceComplexity(3));
		System.out.println(cs.reduceSpaceComplexity(5));
		System.out.println(cs.reduceSpaceComplexity(20));
		
		// Multiple Stair problem
		System.out.println(cs.kSteps(7, 2));
		
		// Minimum cost problem
		final Set list1 = new HashSet<>();
		int[] cost1 = {0, 3, 2, 4};
		System.out.println(cs.leastCostProblem(3, cost1, list1));
		
		final Set list2 = new HashSet<>();
		int[] cost2 = {0, 3, 2, 4, 6, 1, 1, 5, 3};
		System.out.println(cs.leastCostProblem(8, cost2, list2));
		list2.forEach(x -> System.out.println("Step -> " + x));
	}
}
