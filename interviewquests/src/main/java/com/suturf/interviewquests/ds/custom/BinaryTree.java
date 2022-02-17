package com.suturf.interviewquests.ds.custom;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryTree<T> implements Comparable<BinaryTree<T>> {
	
	private static final Logger log = LoggerFactory.getLogger(BinaryTree.class);

	T data;
	BinaryTree<T> leftNode;
	BinaryTree<T> rightNode;
	
	public BinaryTree(T data) {
		
		this.data = data;
		this.leftNode = null;
		this.rightNode = null;
	}
	
	public void insert(final BinaryTree<T> value) {

		if (value.compareTo(this) < 1) {
			if (this.leftNode != null) {
				this.leftNode.insert(value);
			} else {
				this.leftNode = value;
			}
		} else {
			if (this.rightNode != null) {
				this.rightNode.insert(value);
			} else {
				this.rightNode = value;
			}
		}
	}
	
	public void inOrder() {
		if (this.leftNode != null) this.leftNode.inOrder();
		log.info(" > " + this.data);
		if (this.rightNode != null) this.rightNode.inOrder();
	}
	
	public void preOrder() {
		log.info(" > " + this.data);
		if (this.leftNode != null) this.leftNode.inOrder();
		if (this.rightNode != null) this.rightNode.inOrder();
	}
	
	public void postOrder() {
		if (this.leftNode != null) this.leftNode.inOrder();
		if (this.rightNode != null) this.rightNode.inOrder();
		log.info(" > " + this.data);
	}
	
	public int maxTreeHeight(int hgt) {
		final int lhgt = this.leftNode == null?hgt:this.leftNode.maxTreeHeight(hgt+1);
		final int rhgt = this.rightNode == null?hgt:this.rightNode.maxTreeHeight(hgt+1);
		
		return lhgt>rhgt?lhgt:rhgt;
	}

	/**
	 * For O(n), you will need to use a queue
	 * 1. Add root to Queue
	 * 2. Dequeue root and queue left then right
	 * 3. Dequeue first one and queue the left and then right
	 * 4. Keep on doing till everything finished
	 */
	public void levelOrder() {
		
		final Queue<BinaryTree<T>> que = new LinkedList<>();
		que.add(this);
		
		log.info("Printing in LEVEL order");
		while (!que.isEmpty()) {
			final BinaryTree<T> node = que.remove();
			log.info("\t{}", node.data.toString());
			if (node.leftNode != null) que.add(node.leftNode);
			if (node.rightNode != null) que.add(node.rightNode);
		}
	}
	
	public void visualizeTree(final StringBuilder sb, final String seps) {
		
		sb.append(seps);
		sb.append("|---");
		sb.append(this.data);
		sb.append("\n");
		
		if (this.leftNode != null) this.leftNode.visualizeTree(sb, seps + "|    ");
		if (this.rightNode != null) this.rightNode.visualizeTree(sb, seps + "|    ");
	}

	@Override
	public int compareTo(BinaryTree<T> binaryTree) {

		if (binaryTree.data instanceof Integer) {
			int d1 = Integer.parseInt(binaryTree.data.toString());
			int d2 = Integer.parseInt(this.data.toString());
			return (d1 > d2 ? -1 : 1); 
		}
		
		return 1;
	}
	
	public static void main(final String[] args) {
		System.out.println("Provide numbers:");
		final Scanner scan = new Scanner(System.in);
		String s = scan.nextLine();
		if ("".equals(s)) {
			s = "12, 45, 11, 67, 42, 8, 1, 89, 34, 22, 65, 80, 52";
		}
		scan.close();

		final String [] parts = s.split(",");
		final BinaryTree<Integer> root = new BinaryTree<>(Integer.parseInt(parts[0].trim()));
		for (int i=1; i<parts.length; i++) {
			final BinaryTree<Integer> bt = new BinaryTree<>(Integer.parseInt(parts[i].trim()));
			root.insert(bt);
		}
		
		log.info("In Order -->");
		root.inOrder();
		
		log.info("Pre Order -->");
		root.preOrder();
		
		log.info("Post Order -->");
		root.postOrder();
		
		final StringBuilder sb = new StringBuilder();
		root.visualizeTree(sb, "");
		System.out.println("\n");
		System.out.println(sb);
		
		final int treeHeight = root.maxTreeHeight(1);
		log.info("Max Tree Height: {}", treeHeight);
		root.levelOrder();
	}
}
