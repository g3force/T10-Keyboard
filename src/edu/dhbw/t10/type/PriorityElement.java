/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 19, 2011
 * Author(s): dirk
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * TODO dirk, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author dirk
 * 
 */
public class PriorityElement {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private char										letter;
	private PriorityElement							father;
	private PriorityElement							suggest;
	private int											frequency;
	private long										lastUse;
	private Map<Character, PriorityElement>	followers;
	
	private static final Logger		logger	= Logger.getLogger(PriorityElement.class);

	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public PriorityElement(char wo, PriorityElement fa, int fr) {
		initializer(wo, fa, this, fr, new HashMap<Character, PriorityElement>());
		logger.debug("PriorityElement created (1)");
	}
	

	public PriorityElement(char wo, PriorityElement fa, PriorityElement su, int fr) {
		initializer(wo, fa, su, fr, new HashMap<Character, PriorityElement>());
		logger.debug("PriorityElement created (2)");
	}
	

	public PriorityElement(char wo, PriorityElement fa, PriorityElement su, int fr, Map<Character, PriorityElement> fo) {
		initializer(wo, fa, su, fr, fo);
		logger.debug("PriorityElement created (3)");
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	private void initializer(char wo, PriorityElement fa, PriorityElement su, int fr, Map<Character, PriorityElement> fo) {
		letter = wo;
		father = fa;
		suggest = su;
		frequency = fr;
		followers = fo;
		Calendar timestamp = new GregorianCalendar();
		lastUse = timestamp.getTimeInMillis();
	}
	
	
	private void updateLastUse() {
		Calendar timestamp = new GregorianCalendar();
		lastUse = timestamp.getTimeInMillis();
	}
	/**
	 * 
	 * Adds a new Follower to a PriorityElement
	 * Be careful, check first that there isnt this element
	 * 
	 * @param c the letter which was added
	 * @return the new Follower Element
	 */
	public PriorityElement addFollower(char c) {
		logger.debug("Adding follower...");
		PriorityElement newFollower = new PriorityElement(c, this, 0);
		followers.put(c, newFollower);
		logger.debug("Follower added");
		return newFollower;
	}
	
	
	/**
	 * returns of this node has a follower with the word c
	 * @param c the letter of the follower
	 * @return true, if the follower exists
	 */
	public boolean hasFollower(char c) {
		return followers.containsKey(c);
	}
	
	
	/**
	 * gets the following node with the word c
	 * be careful, exists the follower?
	 * @param c the letter of the follower which shall be returned
	 * @return the follower with the word c
	 */
	public PriorityElement getFollower(char c) {
		if (hasFollower(c))
			return followers.get(c);
		else
			return null;
	}
	
	
	/**
	 * increases the frequency of a node, adjusts the suggests in the fathers
	 * function is called, whenever an Priority element is created or frequency higherd
	 */
	public void increase() {
		updateLastUse();
		frequency++;
		ReplaceSuggestsInFathers();
	}
	
	
	/**
	 * local method to replace the suggests in the fathers of this node
	 */
	private void ReplaceSuggestsInFathers() {
		logger.debug("Replacing suggests (in Fathers)...");
		PriorityElement node = this.getFather();
		PriorityElement increasedNode = this;
		// if the father is null, the node is the root element
		while (node.getFather() != null
				&& (node.getSuggest().buildWord().equals(increasedNode.buildWord()) || node.getSuggest().getFrequency() < frequency)) {
			node.setSuggest(increasedNode);
			node = node.getFather();
		}
		logger.debug("Suggests (in Fathers) replaced");
	}


	/**
	 * builds the according word to a node, goes up the tree and puts the char to each other
	 * 
	 * @param node starting node
	 * @return the according word to the node
	 */
	public String buildWord() {
		logger.debug("Building Word...");
		String word = "";
		PriorityElement node = this;
		while (node.getFather() != null) {
			word = node.getLetter() + word;
			node = node.getFather();
		}
		logger.debug("Word built");
		return word;
	}
	
	
	/**
	 * puts alle Followers of this node into a list, beginning with itself
	 * 
	 * @return list of all followers
	 */
	public LinkedList<PriorityElement> getListOfFollowers() {
		logger.debug("Fetching followers...");
		LinkedList<PriorityElement> ll = new LinkedList<PriorityElement>();
		for (PriorityElement pe : followers.values()) {
			ll.add(pe);
			ll.addAll(pe.getListOfFollowers());
		}
		logger.debug("Followers fetched");
		return ll;
	}
	
	
	/**
	 * deletes the Follower of this node according to the word given
	 * takes the needed char of word by its own
	 * 
	 * @param word the word of the node which shall be deleted
	 */
	public void deleteFollower(String word) {
		int indexOfCharToDelete = buildWord().length();
		char charToDelete = word.charAt(indexOfCharToDelete);
		followers.remove(charToDelete);
	}
	
	
	/**
	 * resets the suggest of this item and searches again for the right one
	 */
	public void resetSuggest() {
		logger.debug("Replacing suggests (in Node)...");
		suggest = this;
		for (PriorityElement pe : followers.values()) {
			if (pe.getSuggest().getFrequency() > suggest.getFrequency())
				suggest = pe.getSuggest();
		}
		logger.debug("Suggests (in Node) replaced");
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
