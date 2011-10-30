/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): dirk
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


/**
 * data container for the dictionary, the data it self is stored in the PriorityElements, PriorityTree has functions
 * like insert, delete,...
 * @author dirk
 * 
 */
public class PriorityTree implements Serializable {
	/**  */
	private static final long		serialVersionUID	= 662040913098286336L;
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private PriorityElement			root;
	private transient LinkedList<int[]>	allowedChars;
	
	private static final Logger	logger	= Logger.getLogger(PriorityTree.class);
	private transient String				pathToAllowedChars;

	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public PriorityTree(String chars) {
		pathToAllowedChars = chars;
		allowedChars = new LinkedList<int[]>();
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
		insert(word, 1, false);
	}
	
	
	/**
	 * inserts a word to the tree
	 * if the word already exist, frequency is increased by one and suggests are adujsted
	 * @param word the word that should be inserted
	 * @param frequency the start frequency of the inserting word
	 */
	private void insert(String word, int frequency, boolean setFreq) {
		if (inputValid(word)) {
			logger.debug("Insertig Word...");
			PriorityElement node = root;
			char[] inChar = word.toCharArray(); // put every letter of the word alone in an char array
			for (int i = 0; i < inChar.length; i++) 
			{
				if (node.hasFollower(inChar[i])) 
				{
					if (i < inChar.length - 1) {
						node = node.getFollower(inChar[i]);
					} else {
						node = node.getFollower(inChar[i]);
						if (setFreq)
							node.setFrequency(frequency - 1);
						node.increase();
					}
					logger.debug("Inserting Node... (Node Increased)");
				} else {
					node = node.addFollower(inChar[i]);
					if (i == inChar.length - 1) {
						if (setFreq)
							node.setFrequency(frequency - 1);
						node.increase();
					}
					logger.debug("Inserting Node... (New Node Added)");
				}
				logger.info("Word Inserted");
			}
		} else {
			logger.info("Word Ignored - not valid");
			System.out.println("not valid");
		}
	}
	
	
	/**
	 * takes a String with the beginning of the word, goes to the according node and returns the stored suggest word
	 * 
	 * @param wordPart beginning of a word
	 * @return suggested Word
	 */
	public String getSuggest(String wordPart) {
		logger.debug("Creating suggest for " + wordPart + "...");
		PriorityElement suggest = getElement(wordPart);
		if (suggest == null) {
			logger.info("Suggest created (same as wordPart)");
			return wordPart;
		} else {
			String out = suggest.getSuggest().buildWord();
			logger.info("Suggest created (suggest word: " + out + ")");
			return out;
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
		printTree(true, "");
	}
	
	
	/**
	 * print subtree starting at a PrirorityElement specified through the according word
	 * @param rootElement
	 */
	public void printTree(String rootElement) {
		printTree(false, rootElement);
	}
	
	
	/**
	 * helper method for printTree, does the work
	 * and yes, i know that takeRoot is useless, but I do not know the word of root
	 * @param takeRoot decides whether the root element should be took or the given attribute
	 * @param rootElement the element which shall be took, if takeRoot is false
	 */
	private void printTree(boolean takeRoot, String rootElement) {
		PriorityElement start = root;
		if (!takeRoot) {
			start = getElement(rootElement);
			if (start == null)
				start = root;
		}
		logger.debug("Printing output...");
		start.print();
		for (PriorityElement pe : start.getListOfFollowers()) {
			pe.print();
		}
		System.out.println("Complete amount of Elements: " + start.getListOfFollowers().size());
		logger.debug("Output printed");
	}
	
	
	/**
	 * inserts a list of words to a tree
	 * @param input HashMap referencing a word (String) to its frequency (int)
	 */
	public void importFromHashMap(HashMap<String, Integer> input) {
		for (Entry<String, Integer> entry : input.entrySet()) {
			insert(entry.getKey(), entry.getValue(), true);
		}
	}
	
	
	/**
	 * exports the dictionary tree as a HashMap
	 * @return the dictionary tree as a HashMap
	 */
	public HashMap<String, Integer> exportToHashMap() {
		HashMap<String, Integer> exportMap = new HashMap<String, Integer>();
		for (PriorityElement pe : root.getListOfFollowers()) {
			if (pe.getFrequency() != 0)
				exportMap.put(pe.buildWord(), pe.getFrequency());
		}
		
		return exportMap;
	}
	
	
	/**
	 * any PriorityElement with has got a bottomBorder or less frequency is deleted
	 * @param bottomBorder border to decide if a PriorityElement has to be deleted
	 * @param olderThan not implemented yet
	 * @param flag 0 -> only bottomBorder
	 *           1 -> only olderThan
	 *           2 -> bottomBorder OR olderThan
	 *           3 -> bottomBorder AND olderThan
	 * @return the amount of deleted items
	 */
	public int autoCleaning(int bottomBorder, long olderThan, int flag) {
		LinkedList<PriorityElement> toDelete = new LinkedList<PriorityElement>();
		for (PriorityElement pe : root.getListOfFollowers()) {
			if ((flag == 0 && (pe.getFrequency() <= bottomBorder)) || (flag == 1 && (pe.getLastUse() <= olderThan))
					|| (flag == 2 && (pe.getFrequency() <= bottomBorder || pe.getLastUse() <= olderThan))
					|| (flag == 3 && (pe.getFrequency() <= bottomBorder && pe.getLastUse() <= olderThan))) {
				if (pe.getFollowers().size() == 0) {
					toDelete.add(pe);
				} else {
					pe.setFrequency(0);
				}
			}
		}
		int length = toDelete.size();
		while (!toDelete.isEmpty()) {
			PriorityElement pe = toDelete.pop();
			delete(pe.buildWord());
		}
		logger.info("Cleaned (removed elements: " + length + ")");
		return length;
	}
	
	

	/**
	 * prints out the dictionary, beginning with the word with the highest frequency
	 * bad in performance
	 */
	public LinkedList<PriorityElement> getFreqSortedList() {
		LinkedList<PriorityElement> ll = new LinkedList<PriorityElement>();
		for (PriorityElement pe : root.getListOfFollowers()) {
			boolean sorted = false;
			if (ll.isEmpty())
				ll.add(pe);
			for (int i = 0; i < ll.size() && !sorted; i++) {
				if (ll.get(i).getFrequency() < pe.getFrequency()) {
					ll.add(i, pe);
					sorted = true;
				}
			}
		}
		return ll;
	}
	
	
	/**
	 * 
	 * only for english, ÄÖÜäüö not supported
	 * @param in string
	 * @return true if, all chars are in the alphabet
	 */
	
	private boolean inputValid(String in) {
		for(char letter: in.toCharArray()) {
			int counter = 0;
			for (int[] range : allowedChars) {
				if ((int) letter >= range[0] && (int) letter <= range[1])
					counter++;
			}
			if (counter == 0)
				return false;
		}
		return true;

	}
	
	
	private void saveAllowedChars() {
		try {
			File confFile = new File("pathToAllowedChars");
			FileWriter fw = new FileWriter(confFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (int i = 0; i < allowedChars.size(); i++) {
				bw.write(allowedChars.get(i)[0] + "-" + allowedChars.get(i)[1] + "\n");
			}
			bw.close();
		} catch (IOException io) {
			logger.debug("IOException in readConfig()");
			io.printStackTrace();
		} catch (Exception ex) {
			logger.debug("Exception in readConfig(): " + ex.toString());
			ex.printStackTrace();
		}
	}
	
	
	private void loadAllowedChars() {
		try {
			File confFile = new File(pathToAllowedChars);
			if (confFile.exists()) {
				FileReader fr = new FileReader(confFile);
				BufferedReader br = new BufferedReader(fr);
				String entry = "";
				while ((entry = br.readLine()) != null) {
					String[] entries = entry.split("-");
					int[] newi = { Integer.parseInt(entries[0]), Integer.parseInt(entries[1]) };
					allowedChars.add(newi);
				}
			} else {
				logger.error("No allowed chars");
				int[] newi = { 0, 255 };
				allowedChars.add(newi);
			}
		} catch (IOException io) {
			logger.debug("IOException in loadAllowedChars()");
			io.printStackTrace();
		} catch (Exception ex) {
			logger.debug("Exception in loadAllowedChars(): " + ex.toString());
			ex.printStackTrace();
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
