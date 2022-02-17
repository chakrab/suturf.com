package com.suturf.interviewquests.educativeio;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * You are given a linked list where the node has two pointers. The first is the regular next pointer.
 * The second pointer is called arbitrary_pointer and it can point to any node in the linked list. 
 * Your job is to write code to make a deep copy of the given linked list. Here, deep copy means that 
 * any operations on the original list should not affect the copied list.
 * 
 * @author Suvendra
 *
 */
public class LinklistCopy {

	private static final Logger log = LoggerFactory.getLogger(LinklistCopy.class);
	
	public static class Node {
		// Removing the need to define getters and setters, too much work :)
		protected String data;
		protected Node   nextPtr;
		protected Node   arbitraryPtr;
		
		public Node(final String data) {
			this.data = data;
		}
	}
	
	public Node addNode(final Node which, final String data) {
		final Node node = new Node(data);
		node.nextPtr = null;
		node.arbitraryPtr = null;
		if (which != null)
			which.nextPtr = node;
		return node;
	}
	
	public void updateArbitraryPointer(final Node which, final Node arbNode) {
		which.arbitraryPtr = arbNode;
	}
	
	public void printByNextPointer(final Node head) {
		Node currNode = head;
		log.info("Listing by Next Pointer....");
		while (currNode != null) {
			log.info("\t{}", currNode.data);
			currNode = currNode.nextPtr;
		}
	}
	
	public void printByArbitraryPointer(final Node head) {
		Node currNode = head;
		log.info("Listing by Arbitrary Pointer....");
		while (currNode != null) {
			log.info("\t{}", currNode.data);
			currNode = currNode.arbitraryPtr;
		}
	}

	public Node copyLinkedList(final Node head) {
		
		// Now that we have a working linked list
		Node newHead = addNode(null, head.data);
		newHead.nextPtr = head.nextPtr;
		newHead.arbitraryPtr = head.arbitraryPtr;

		Node currNode = newHead;
		Node iterNode = head.nextPtr;
		final Map<Node, Node> arbMap = new HashMap<>(); 
		while (iterNode != null) {
			currNode = addNode(currNode, iterNode.data);
			currNode.arbitraryPtr = iterNode.arbitraryPtr;

			// Store the node
			arbMap.put(iterNode, currNode);
			iterNode = iterNode.nextPtr;
		}

		// Now update the Arbitrary Pointer
		currNode = newHead;
		while (currNode != null) {
			if (currNode.arbitraryPtr != null) {
				currNode.arbitraryPtr = arbMap.get(currNode.arbitraryPtr);
			} else {
				updateArbitraryPointer(currNode, null);
			}
			currNode = currNode.nextPtr;
		}
		return newHead;
	}

	public static void main (final String [] args) {
		
		// Create a linked list
		final LinklistCopy llc = new LinklistCopy();

		final Node root = llc.addNode(null, "HEY");
		final Node node1 = llc.addNode(root, "YOU!");
		final Node node2 = llc.addNode(node1, "OUT");
		final Node node3 = llc.addNode(node2, "THERE");
		final Node node4 = llc.addNode(node3, "IN");
		final Node node5 = llc.addNode(node4, "THE");
		final Node node6 = llc.addNode(node5, "COLD.");
		
		// Assign the arbitrary nodes
		llc.updateArbitraryPointer(root, node3);
		llc.updateArbitraryPointer(node3, node1);
		llc.updateArbitraryPointer(node1, node4);
		llc.updateArbitraryPointer(node4, node5);
		llc.updateArbitraryPointer(node5, node6);
		
		// Now print
		llc.printByNextPointer(root);
		llc.printByArbitraryPointer(root);
		
		// Deep copy
		final Node copyNode = llc.copyLinkedList(root);
		llc.printByNextPointer(copyNode);
		llc.printByArbitraryPointer(copyNode);
	}
}
