package com.suturf.interviewquests.ds.custom;

import java.util.LinkedList;
import java.util.List;

public class Tree<T> implements Comparable<Tree<T>> {
	
	T data;
	Tree<T> parent;
	List<Tree<T>> children;
	
	public Tree(T data) {
		
		this.data = data;
		this.parent = null;
		this.children = new LinkedList<>();
	}
	
	public Tree<T> addChild(T data) {
		
		final Tree<T> childNode = new Tree<T>(data);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	@Override
	public int compareTo(Tree<T> o) {
		return 0;
	}
}
