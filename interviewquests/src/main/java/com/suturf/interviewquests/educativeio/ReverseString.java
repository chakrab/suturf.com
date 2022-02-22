package com.suturf.interviewquests.educativeio;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReverseString {

	private static final Logger log = LoggerFactory.getLogger(ReverseString.class);
	
	public String reverseString(final String str) {
		
		final char[] arrStr = str.toCharArray();
		final int charLen = arrStr.length;
		for (int i=0; i<charLen/2; i++) {
			final char t = arrStr[i];
			arrStr[i] = arrStr[charLen-1-i];
			arrStr[charLen-1-i] = t;
		}
		
		return new String(arrStr);
	}
	
	public String reverseWord(final String str) {
		
		final String revrsed = reverseString(str);
		
		final char[] arrStr = revrsed.toCharArray();
		final int charLen = arrStr.length;
		
		// Now we go through the words and put them
		// We will use a stack
		final StringBuilder strb = new StringBuilder();
		final Stack<Character> stack = new Stack<>();
		for (int i=0; i<charLen; i++) {
			if (arrStr[i] == ' ') {
				// Dump the word
				while (!stack.isEmpty())
					strb.append(stack.pop());
				strb.append(' ');
			} else {
				stack.push(arrStr[i]);
			}
		}
		
		// Last Word
		while (!stack.isEmpty())
			strb.append(stack.pop());
		
		return strb.toString();
	}

	public static void main (final String[] args) {
		
		final String str1 = "A long lost desert";
		final String str2 = "Made of bass wood";
		
		final ReverseString rs = new ReverseString();
		log.info("String '{}' reversed: {}", str1, rs.reverseString(str1));
		log.info("String '{}' reversed: {}", str2, rs.reverseString(str2));
		
		log.info("String word '{}' reversed: {}", str1, rs.reverseWord(str1));
		log.info("String word '{}' reversed: {}", str2, rs.reverseWord(str2));
	}
}
