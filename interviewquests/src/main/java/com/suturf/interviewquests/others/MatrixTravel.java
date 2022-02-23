package com.suturf.interviewquests.others;

/**
 * 
 * In this problem we will find the number of ways to travel from one path to a different one
 * in a m x n matrix
 * 
 * +--+--+--+--+--+--+
 * | S|  |  |  |  |  |
 * +--+--+--+--+--+--+
 * |  |  |  |  |  |  |
 * +--+--+--+--+--+--+
 * |  |  |  |  |  | E|
 * +--+--+--+--+--+--+
 * 
 * F(i,j) = F(i-1, j) + F(i, j-1)
 * 
 * @author suvendra
 *
 */
public class MatrixTravel {

	public int regularTravel(final int m, final int n) {
		
		final Integer[][] matrix = new Integer[m][n];
		matrix[0][0] = 1;
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				if (i > 0 && j > 0) {
					matrix[i][j] = matrix[i-1][j] + matrix[i][j-1];
				} else if (i > 0) {
					matrix[i][j] = matrix[i-1][j];
				} else if (j > 0) {
					matrix[i][j] = matrix[i][j-1];
				}
			}
		}
		
		return matrix[m-1][n-1];
	}
	
	public int blockedTravel(final int m, final int n, final Integer[][] blocks) {
		
		final Integer[][] matrix = new Integer[m][n];
		matrix[0][0] = 1;
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				
				if (blocks[i][j] == 1) {
					matrix[i][j] = 0;
					continue;
				}

				if (i > 0 && j > 0) {
					matrix[i][j] = matrix[i-1][j] + matrix[i][j-1];
				} else if (i > 0) {
					matrix[i][j] = matrix[i-1][j];
				} else if (j > 0) {
					matrix[i][j] = matrix[i][j-1];
				}
			}
		}
		
		return matrix[m-1][n-1];
	}
	
	public static void main (final String [] args) {
		final MatrixTravel mt = new MatrixTravel();
		
		// Regular Travel
		System.out.println(mt.regularTravel(3, 4));
		
		// Blocked Travel
		final Integer[][] blocks = new Integer[3][4];
		for (int i=0; i<3; i++) {
			for (int j=0; j<4; j++) {
				blocks[i][j] = 0;
			}
		}
		blocks[1][2] = 1;
		blocks[1][3] = 1;
		System.out.println(mt.blockedTravel(3, 4, blocks));
	}
}
