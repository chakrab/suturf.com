package com.suturf.interviewquests.others;

/**
 * 
 * This is a coin change problem. Suppose you have unlimited supply of coins of
 * denomination 1, 3, 5, 10. How many ways can you give change for value n
 * 
 * Transition Function F(i) = F(i-1) + F(i-3) + F(i-5) + F(i-10)
 * 
 * @author suvendra
 *
 */
public class CoinChange {

	/**
	 * 
	 * NON Unique
	 * 
	 */
	
	
	public int coinChange(final int value) {

		final Integer[] coins = new Integer[value + 1];

		// Base cases
		coins[0] = 1;
		coins[1] = 1;
		coins[2] = 1;
		for (int i = 3; i <= value; i++) {
			if (i >= 1) {
				coins[i] = coins[i - 1];
			}
			if (i >= 3) {
				coins[i] += coins[i - 3];
			}
			if (i >= 5) {
				coins[i] += coins[i - 5];
			}
			if (i >= 10) {
				coins[i] += coins[i - 10];
			}
		}

		return coins[value];
	}

	/**
	 * 
	 * Additional requirement: you can only use EXACTLY t coins
	 * 
	 * F(i, t) = F(i-1, t-1) + F(i-3, t-1) + F(i-5, t-1) + F(i-10, t-1)
	 * 
	 * @param args
	 */
	public int exactCoinChange(final int value, final int t, final int[] avlCoins) {

		final int[][] coins = new int[value + 1][t + 1];
		// Base cases
		coins[0][0] = 1;

		for (int i = 1; i <= value; i++) {
			for (int j = 0; j <= t; j++) {
				if (i > 0 && j == 0) {
					// we cannot give anything without coins
					coins[i][j] = 0;
					continue;
				}

				coins[i][j] = 0;
				for (int k = 0; k < avlCoins.length; k++) {
					if (i >= avlCoins[k]) {
						coins[i][j] += coins[i - avlCoins[k]][j - 1];
					}
				}
			}
		}

		return coins[value][t];

	}

	/**
	 * 
	 * Additional requirement: you can only use EXACTLY t coins
	 * 
	 * F(i, t) = F(i-1, t-1) + F(i-3, t-1) + F(i-5, t-1) + F(i-10, t-1)
	 * 
	 * @param args
	 */
	public int noMoreThanCoinChange(final int value, final int t, final int[] avlCoins) {

		final int[][] coins = new int[value + 1][t + 1];
		// Base cases
		coins[0][0] = 1;

		for (int i = 1; i <= value; i++) {
			for (int j = 0; j <= t; j++) {
				if (i > 0 && j == 0) {
					// we cannot give anything without coins
					coins[i][j] = 0;
					continue;
				}

				// This is the only change. There is still one way to return change
				// if we do not need to return any coins (at least)
				if (i == 0 && j > 0) {
					// we cannot give anything without coins
					coins[i][j] = 1;
					continue;
				}

				coins[i][j] = 0;
				for (int k = 0; k < avlCoins.length; k++) {
					if (i >= avlCoins[k]) {
						coins[i][j] += coins[i - avlCoins[k]][j - 1];
					}
				}
			}
		}

		return coins[value][t];

	}

	/**
	 * 
	 * Return the change using even number of coins
	 * 
	 * Odd: 0 Even: 1
	 * 
	 * Base cases: coins[0][0] = 0 > Cannot return odd coins[0][1] = 1 > Can return
	 * even
	 * 
	 * Transition Fn: For Denomination 1, 3 F[i][0] = F[i-1][1] + F[i-3][1] >
	 * Starting with Odd coins and taking one leaves Even coins F[i][1] = F[i-1][0]
	 * + f[i-3][3]
	 * 
	 * @param args
	 */
	public int onlyEven(final int value, final int[] avlCoins) {

		final int[][] coins = new int[value + 1][2]; // 0 or 1

		coins[0][0] = 0;
		coins[0][1] = 1;

		for (int i = 1; i <= value; i++) {

			coins[i][0] = 0;
			coins[i][1] = 0;
			for (int k = 0; k < avlCoins.length; k++) {
				if (i >= avlCoins[k]) {
					coins[i][0] += coins[i - avlCoins[k]][1];
					coins[i][1] += coins[i - avlCoins[k]][0];
				}
			}
		}
		
		return coins[value][1];
	}

	/**
	 * 
	 * Unique
	 * 
	 */
	
	
	public static void main(final String[] args) {
		final CoinChange cc = new CoinChange();
		System.out.println(cc.coinChange(4));

		// Exact Change
		final int[] coins = { 1, 3, 5, 10 };
		System.out.println(cc.exactCoinChange(7, 3, coins));
		
		// Even coins
		System.out.println(cc.onlyEven(6, coins));
	}
}
