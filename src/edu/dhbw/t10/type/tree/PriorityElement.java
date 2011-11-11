/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 19, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.tree;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * elements in the PriorityTree
 * @author DirkK
 * 
 */
public class PriorityElement implements Serializable {
	/**  */
	private static final long						serialVersionUID	= -6948672774660317104L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private char										letter;
	private PriorityElement							father;
	private PriorityElement							suggest;
	private int											frequency;
	private long										lastUse;
	private Map<Character, PriorityElement>	followers;
	
	@SuppressWarnings("unused")
	private static final Logger					logger				= Logger.getLogger(PriorityElement.class);
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * TODO DirkK, add comment! (params!!)
	 * 
	 * @param wo
	 * @param fa
	 * @param fr
	 * @author DirkK
	 */
	public PriorityElement(char wo, PriorityElement fa, int fr) {
		initializer(wo, fa, this, fr, new HashMap<Character, PriorityElement>());
		// logger.trace("PriorityElement created (1)");
	}
	
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @param wo
	 * @param fa
	 * @param su
	 * @param fr
	 * @author DirkK
	 */
	public PriorityElement(char wo, PriorityElement fa, PriorityElement su, int fr) {
		// constructor only for root element, suggest shall not be this, but null
		initializer(wo, fa, su, fr, new HashMap<Character, PriorityElement>());
		// logger.trace("PriorityElement created (2)");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @param wo
	 * @param fa
	 * @param su
	 * @param fr
	 * @param fo
	 * @author DirkK
	 */
	private void initializer(char wo, PriorityElement fa, PriorityElement su, int fr, Map<Character, PriorityElement> fo) {
		letter = wo;
		father = fa;
		suggest = su;
		frequency = fr;
		followers = fo;
		updateLastUse();
	}
	
	
	/**
	 * updates the timestamp in this node
	 * should be called, whenever the node is changed
	 * 
	 * @author DirkK
	 */
	private void updateLastUse() {
		Calendar timestamp = new GregorianCalendar();
		lastUse = timestamp.getTimeInMillis();
	}
	
	
	/**
	 * Adds a new Follower to a PriorityElement
	 * Be careful, check first that there isnt this element
	 * 
	 * @param c the letter which was added
	 * @return the new Follower Element
	 * @author DirkK
	 */
	public PriorityElement addFollower(char c) {
		// logger.trace("Adding follower...");
		PriorityElement newFollower = new PriorityElement(c, this, 0);
		followers.put(c, newFollower);
		// logger.trace("Follower added");
		return newFollower;
	}
	
	
	/**
	 * returns of this node has a follower with the word c
	 * @param c the letter of the follower
	 * @return true, if the follower exists
	 * @author DirkK
	 */
	public boolean hasFollower(char c) {
		return followers.containsKey(c);
	}
	
	
	/**
	 * gets the following node with the word c
	 * be careful, exists the follower?
	 * @param c the letter of the follower which shall be returned
	 * @return the follower with the word c
	 * @author DirkK
	 */
	public PriorityElement getFollower(char c) {
		if (hasFollower(c))
			return followers.get(c);
		else
			return null;
	}
	
	
	/**
	 * increases the frequency of a node, adjusts the suggests in the fathers
	 * function is called, whenever an Priority element is created or frequency higher
	 * 
	 * @author DirkK
	 */
	public void increase() {
		updateLastUse();
		frequency++;
		replaceSuggestsInFathers();
	}
	
	
	/**
	 * local method to replace the suggests in the fathers of this node after an increase
	 * 
	 * @author DirkK
	 */
	private void replaceSuggestsInFathers() {
		// logger.debug("Replacing suggests (in Fathers)...");
		PriorityElement node = this.getFather();
		PriorityElement increasedNode = this;
		// if the father is null, the node is the root element
		// if the word in the suggest is the same, as the word in this node -> nothing to do, but shall not abort while
		// loop
		// the frequency in this node is lower then the frequency in the increased node -> replace suggest
		while (node.getFather() != null
				&& (node.getSuggest().buildWord().equals(increasedNode.buildWord()) || node.getSuggest().getFrequency() < frequency)) {
			node.setSuggest(increasedNode);
			node = node.getFather();
		}
		// logger.debug("Suggests (in Fathers) replaced");
	}
	
	
	/**
	 * builds the according word to a node, goes up the tree and puts the char to each other
	 * 
	 * @param node starting node
	 * @return the according word to the node
	 * @author DirkK
	 */
	public String buildWord() {
		// logger.trace("Building Word...");
		String word = "";
		PriorityElement node = this;
		while (node.getFather() != null) {
			word = node.getLetter() + word;
			node = node.getFather();
		}
		// logger.trace("Word built");
		return word;
	}
	
	
	/**
	 * puts alle Followers of this node into a list, beginning with itself
	 * 
	 * @return list of all followers
	 * @author DirkK
	 */
	public LinkedList<PriorityElement> getListOfFollowers() {
		// logger.debug("Fetching followers...");
		LinkedList<PriorityElement> ll = new LinkedList<PriorityElement>();
		for (PriorityElement pe : followers.values()) {
			ll.add(pe);
			ll.addAll(pe.getListOfFollowers());
		}
		// logger.debug("Followers fetched");
		return ll;
	}
	
	
	/**
	 * 
	 * creates a HashMap of the Followers of this element
	 * this element itself is not included
	 * 
	 * @return
	 * @author DirkK
	 */
	public HashMap<String, Integer> getHashMapOfFollowers() {
		// logger.trace("Fetching followers...");
		HashMap<String, Integer> ll = new HashMap<String, Integer>();
		// int counter = 0;
		for (PriorityElement pe : followers.values()) {
			if (pe.getFrequency() > 0) {
				ll.put(pe.buildWord(), pe.getFrequency());
				// counter++;
			}
			ll.putAll(pe.getHashMapOfFollowers());
		}
		// logger.trace("Followers fetched (" + counter + " Elements fetched)");
		return ll;
	}
	
	
	/**
	 * deletes the Follower of this node according to the word given
	 * takes the needed char of word by its own
	 * 
	 * @param word the word of the node which shall be deleted
	 * @author DirkK
	 */
	public void deleteFollower(String word) {
		int indexOfCharToDelete = buildWord().length();
		char charToDelete = word.charAt(indexOfCharToDelete);
		followers.remove(charToDelete);
	}
	
	
	/**
	 * resets the suggest of this item and searches again for the right one
	 * 
	 * @author DirkK
	 */
	public void resetSuggest() {
		// logger.debug("Replacing suggests (in Node)...");
		suggest = this;
		for (PriorityElement pe : followers.values()) {
			if (pe.getSuggest().getFrequency() > suggest.getFrequency())
				suggest = pe.getSuggest();
		}
		// logger.debug("Suggests (in Node) replaced");
	}
	
	
	/**
	 * prints the PriorityElement
	 * structure (non-root): [word], [frequency], [lastUse], Suggest: [word_suggest] ([freq_suggest]) (Father:
	 * [word_father])
	 * 
	 * @author DirkK
	 */
	public void print() {
		if (father == null) {
			System.out.print("[root], " + frequency + ", " + lastUse + ", Suggest: " + suggest + " (Father: " + father
					+ ")\n");
		} else {
			System.out.print(buildWord() + ", " + frequency + ", " + lastUse // this item
					+ ", Suggest: " + suggest.buildWord() + "(" + suggest.getFrequency() + ")" // according
																														// suggest
					+ " (Father: " + father.buildWord() + ")\n"); // according father
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public Map<Character, PriorityElement> getFollowers() {
		return followers;
	}
	
	
	public void setFollowers(Map<Character, PriorityElement> followers) {
		this.followers = followers;
	}
	
	
	public char getLetter() {
		return letter;
	}
	
	
	public void setLetter(char word) {
		this.letter = word;
	}
	
	
	public PriorityElement getFather() {
		return father;
	}
	
	
	public void setFather(PriorityElement father) {
		this.father = father;
	}
	
	
	public PriorityElement getSuggest() {
		return suggest;
	}
	
	
	public void setSuggest(PriorityElement suggest) {
		this.suggest = suggest;
	}
	
	
	public int getFrequency() {
		return frequency;
	}
	
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
	public long getLastUse() {
		return lastUse;
	}
	
	
	public void setLastUse(long lastUse) {
		this.lastUse = lastUse;
	}
}
