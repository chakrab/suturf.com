package com.suturf.interviewquests.ds.available;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * List Family contains ArrayList and LinkedList
 * - ArrayList is the generic implementation
 * - LinkedList uses Double Linked link, traversal is faster. Can be also used for Dequeue
 * 
 * @author suvendra
 *
 */
public class ListFamily {
	
	private static final Logger log = LoggerFactory.getLogger(ListFamily.class);
	
	// Build a LIFO stack with LinkedList
	static class LinkedListStack {
		private LinkedList<String> llist = new LinkedList<>();
		
		public void push(final String s) {
			llist.addFirst(s);
		}
		
		// Look at top of stack
		public String peep() {
			return llist.getFirst();
		}
		
		// Look and remove top of stack
		public String pop() {
			return llist.removeFirst();
		}
	}
	
	public static void main (final String [] args) {
		final ListFamily.LinkedListStack stack = new ListFamily.LinkedListStack();
		stack.push("1");
		stack.push("2");
		stack.push("3");
		stack.push("4");
		stack.push("5");
		log.debug(stack.pop());
		log.debug(stack.peep());
		log.debug(stack.pop());
		log.debug(stack.pop());
	}
}
