package com.suturf.interviewquests.educativeio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Given the root node of a directed graph, clone this graph by creating its deep copy so that the cloned graph has the 
 * same vertices and edges as the original graph.
 * 
 * Node {
 * 		public int val;
 * 		public List<Node> neighbors;
 * }
 * 
 * @author suvendra
 *
 */
public class CloneGraph {

	public static class Node {
		public Integer val;
		public List<Node> neighbors = new ArrayList<>();
	}

	public Node cloneGraph(final Node root) {
		
		final Map<Integer, Node> mapVisited = new HashMap<>();
		return cloneGraphNode(root, mapVisited);
		
	}
	
	private Node cloneGraphNode(final Node node, Map<Integer, Node> visited) {
		if (visited.containsKey(node.val)) {
			return visited.get(node.val);
		}
		
		// Make a copy
		final Node newNode = new Node();
		newNode.val = node.val;
		visited.put(node.val, newNode);
		for (final Node neighbor : node.neighbors) {
			newNode.neighbors.add(cloneGraphNode(neighbor, visited));
		}

		return newNode;
	}
}
