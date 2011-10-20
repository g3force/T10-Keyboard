/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type;

/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class PriorityTree {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private PriorityElement	root;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public PriorityTree() {
		root = new PriorityElement('\u0000', null, null, 0);
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public void insert(String in) {
		PriorityElement node = root;
		char[] inChar = in.toCharArray(); // put every letter of the word alone in an char array
		for (int i = 0; i < inChar.length; i++) {
			if (node.hasFollower(inChar[i])) {
				if (i < inChar.length - 1) {
					node = node.getFollower(inChar[i]);
				} else {
					node = node.getFollower(inChar[i]);
					node.increase();
				}
			} else {
				node = node.addFollower(inChar[i]);
				if (i == inChar.length - 1) {
					node.increase();
				}
			}
		}
	}


	public String getSuggest(String wordPart) {
		PriorityElement node = root;
		char[] wordPartChar = wordPart.toCharArray(); // put every letter of the word alone in a char array
		for (int i = 0; i < wordPartChar.length; i++) {
			if (node.hasFollower(wordPartChar[i])) {
				node = node.getFollower(wordPartChar[i]);
				if (i == wordPartChar.length - 1) {
					return node.getSuggest().buildWord();
				}
			} else {
				return wordPart;
			}
		}
		return "FAILURE";
	}
	

	public void printTree() {
		for (PriorityElement pe : root.getListOfFollowers()) {
			for (int i = 0; i < pe.buildWord().length(); i++) {
				System.out.print("-");
			}
			System.out
.print(pe.getWord() + ", " + pe.getFrequency() + ", Suggest: " + pe.getSuggest().buildWord()
					+ " (Father: " + pe.getFather().buildWord() + ")\n");
		}
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
