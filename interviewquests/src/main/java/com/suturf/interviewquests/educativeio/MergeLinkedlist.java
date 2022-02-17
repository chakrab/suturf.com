package com.suturf.interviewquests.educativeio;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Given two sorted linked lists, merge them so that the resulting linked list is also sorted. 
 * 
 * This is a simple problem which employs the age old high-low mainframe technique.
 * 
 * @author Suvendra
 *
 */
public class MergeLinkedlist {

	private static final Logger log = LoggerFactory.getLogger(MergeLinkedlist.class);
	
	public LinkedList<Integer> mergeLinkedlists(final LinkedList<Integer> a, final LinkedList<Integer> b) {
		
		final LinkedList<Integer> rlist = new LinkedList<>();
		final int asize = a.size();
		final int bsize = b.size();
		log.info("A list size: {}, B list size: {}", asize, bsize);
		int acurr = 0, bcurr = 0;
		while (acurr < asize || bcurr < bsize) {
			if (bcurr == bsize) {
				// Just put all As
				int aval = a.get(acurr);
				rlist.add(aval);
				acurr++;
			} else if (acurr == asize) {
				// Just put all Bs
				int bval = b.get(bcurr);
				rlist.add(bval);
				bcurr++;
			} else {
				int aval = a.get(acurr);
				int bval = b.get(bcurr);
	
				if (aval < bval) {
					rlist.add(aval);
					acurr++;
				} else {
					rlist.add(bval);
					bcurr++;
				}
			}
		}
		
		return rlist;
	}
	
	public static void main (final String [] args) {
		
		final int[] a = {1, 3, 6, 9, 12, 17, 26, 33, 45, 46, 52, 67};
		final int[] b = {2, 4, 5, 10, 13, 19, 22, 23, 28, 34, 39, 48, 50};
		
		final LinkedList<Integer> alst = new LinkedList<>();
		final LinkedList<Integer> blst = new LinkedList<>();
		for (int aval : a) alst.add(aval);
		for (int bval : b) blst.add(bval);
		
		final MergeLinkedlist mll = new MergeLinkedlist();
		final LinkedList<Integer> vals = mll.mergeLinkedlists(alst, blst);
		for (int v : vals) log.info(" {}", v);
	}
}
