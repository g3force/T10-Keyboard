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
 * @author dirk
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
	
	/**
	 * inserts a word to the tree
	 * if the word already exist, frequency is increased by one and suggests are adujsted
	 * @param word the word that should be inserted
	 */
	public void insert(String word) {
		PriorityElement node = root;
		char[] inChar = word.toCharArray(); // put every letter of the word alone in an char array
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
	

	/**
	 * takes a String with the beginning of the word, goes to the according node and returns the stored suggest word
	 * @param wordPart beginning of a word
	 * @return suggested Word
	 */
	public String getSuggest(String wordPart) {
		PriorityElement suggest = getElement(wordPart);
		if (suggest == null) {
			return wordPart;
		} else {
			return suggest.getSuggest().buildWord();
		}
		
	}
	
	
	public void delete(String word) {
		PriorityElement deleteEl = getElement(word);
		if (deleteEl != null) {
			System.out.println("delete started");
			PriorityElement node = deleteEl;
			while (node.getFollowers().isEmpty()) {
				System.out.println("followers empty");
				node = node.getFather();
				node.deleteFollower(word);
			}
			while (node.getFather() != null && node.getSuggest().buildWord().equals(word)) {
				System.out.println("suggest changed");
				node.resetSuggest();
				node = node.getFather();
			}
		}
	}
	
	
	private PriorityElement getElement(String el) {
		PriorityElement node = root;
		char[] elChar = el.toCharArray(); // put every letter of the word alone in a char array
		for (int i = 0; i < elChar.length; i++) {
			if (node.hasFollower(elChar[i])) {
				node = node.getFollower(elChar[i]);
				if (i == elChar.length - 1) {
					return node;
				}
			}
		}
		return null;
	}
	

	/**
	 * prints the tree
	 */
	public void printTree() {
		System.out.print(" , " + root.getFrequency() + ", Suggest: " + root.getSuggest() + " (Father: "
				+ root.getFather() + ")\n");
		for (PriorityElement pe : root.getListOfFollowers()) {
			for (int i = 0; i < pe.buildWord().length(); i++) {
				System.out.print("-");
			}
			System.out.print(pe.getWord() + ", " + pe.getFrequency() + ", Suggest: " + pe.getSuggest().buildWord()
					+ " (Father: " + pe.getFather().buildWord() + ")\n");
		}
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
