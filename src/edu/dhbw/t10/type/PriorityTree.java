/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): dirk
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


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
	
	private static final Logger	logger	= Logger.getLogger(PriorityTree.class);


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
		insert(word, 0);
	}
	
	
	/**
	 * inserts a word to the tree
	 * if the word already exist, frequency is increased by one and suggests are adujsted
	 * @param word the word that should be inserted
	 * @param frequency the start frequency of the inserting word
	 */
	private void insert(String word, int frequency) {
		logger.debug("Insertig Word...");
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
				logger.debug("Inserting Node... (Node Increased)");
			} else {
				node = node.addFollower(inChar[i]);
				if (i == inChar.length - 1) {
					node.increase();
				}
				logger.debug("Inserting Node... (New Node Added)");
			}
		}
		logger.info("Word Inserted");
	}
	

	/**
	 * takes a String with the beginning of the word, goes to the according node and returns the stored suggest word
	 * 
	 * @param wordPart beginning of a word
	 * @return suggested Word
	 */
	public String getSuggest(String wordPart) {
		logger.debug("Creating suggest...");
		PriorityElement suggest = getElement(wordPart);
		if (suggest == null) {
			logger.info("Suggest created (same as wordPart)");
			return wordPart;
		} else {
			logger.info("Suggest created (suggest from dicctionary)");
			return suggest.getSuggest().buildWord();
		}
		
	}
	
	
	/**
	 * delete a given word in the tree
	 * if the according word has got followers, the frequency is set to 0
	 * no followers -> it is deleted; all fathers are also deleted if they have no followers
	 * suggests are adjusted in both cases
	 * 
	 * @param word the word to be deleted
	 */
	public void delete(String word) {
		logger.debug("Deleting Node...");
		PriorityElement deleteEl = getElement(word);
		if (deleteEl != null) {
			logger.debug("Deleting Node... (Node exist)");
			deleteEl.setFrequency(0);
			PriorityElement node = deleteEl;
			while (node.getFollowers().isEmpty()) {
				logger.debug("Deleting Node... (Node deleted)");
				node = node.getFather();
				node.deleteFollower(word);
			}
			while (node.getFather() != null && node.getSuggest().buildWord().equals(word)) {
				logger.debug("Deleting Node... (Suggest changed)");
				node.resetSuggest();
				node = node.getFather();
			}
		}
		logger.info("Node deleted");
	}
	
	
	/**
	 * gets the according PriorityElement to a given word
	 * 
	 * @param word according word
	 * @return the according PriorityElement
	 */
	private PriorityElement getElement(String word) {
		PriorityElement node = root;
		char[] elChar = word.toCharArray(); // put every letter of the word alone in a char array
		for (int i = 0; i < elChar.length; i++) {
			if (node.hasFollower(elChar[i])) {
				node = node.getFollower(elChar[i]);
				if (i == elChar.length - 1) {
					logger.debug("Node found");
					return node;
				}
			}
		}
		logger.error("Node not found (getElement)");
		return null;
	}
	

	/**
	 * prints the tree
	 */
	public void printTree() {
		logger.debug("Printing output...");
		System.out.print(", " + root.getFrequency() + ", Suggest: " + root.getSuggest() + " (Father: "
				+ root.getFather() + ")\n");
		for (PriorityElement pe : root.getListOfFollowers()) {
			System.out.print(pe.buildWord() + ", " + pe.getFrequency() + ", Suggest: " + pe.getSuggest().buildWord()
					+ " (Father: " + pe.getFather().buildWord() + ")\n");
		}
		logger.debug("Output printed");
	}
	
	
	public void importFromHashMap(HashMap<String, Integer> input) {
		for (Entry<String,Integer> entry:input.entrySet()) {
			insert(entry.getKey(),entry.getValue());
		}
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
