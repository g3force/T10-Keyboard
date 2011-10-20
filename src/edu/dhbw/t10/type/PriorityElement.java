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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


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
	char										word;
	PriorityElement						father;
	PriorityElement						suggest;
	int										frequency;
	Map<Character, PriorityElement>	followers;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public PriorityElement(char wo, PriorityElement fa, int fr) {
		word = wo;
		father = fa;
		suggest = this;
		frequency = fr;
		followers = new HashMap<Character, PriorityElement>();
	}
	

	public PriorityElement(char wo, PriorityElement fa, PriorityElement su, int fr) {
		word = wo;
		father = fa;
		suggest = su;
		frequency = fr;
		followers = new HashMap<Character, PriorityElement>();
	}
	

	public PriorityElement(char wo, PriorityElement fa, PriorityElement su, int fr, Map<Character, PriorityElement> fo) {
		word = wo;
		father = fa;
		suggest = su;
		frequency = fr;
		followers = fo;
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * Adds a new Follower to a PriorityElement
	 * Be careful, check first that there isnt this element
	 * 
	 * @param c the letter which was added
	 * @return the new Follower Element
	 */
	public PriorityElement addFollower(char c) {
		PriorityElement newFollower = new PriorityElement(c, this, 0);
		followers.put(c, newFollower);
		return newFollower;
	}
	
	
	/**
	 * returns of this node has a follower with the word c
	 * @param c the word of the follower
	 * @return true, if the follower exists
	 */
	public boolean hasFollower(char c) {
		return followers.containsKey(c);
	}
	
	
	/**
	 * gets the following node with the word c
	 * be careful, exists the follower?
	 * @param c the word of the follower which shall be returned
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
	 */
	public void increase() {
		frequency++;
		ReplaceSuggestsInFathers();
	}
	
	
	/**
	 * local method to replace the suggests in the fathers of this node
	 */
	private void ReplaceSuggestsInFathers() {
		PriorityElement node = this.getFather();
		PriorityElement increasedNode = this;
		// if the father is null, the node is the root element
		while (node.getFather() != null
				&& (node.getSuggest() == increasedNode || node.getSuggest().getFrequency() < frequency)) {
			node.setSuggest(increasedNode);
			node = node.getFather();
		}
	}


	/**
	 * builds the according word to a node, goes up the tree and puts the char to each other
	 * 
	 * @param node starting node
	 * @return the according word to the node
	 */
	public String buildWord() {
		String word = "";
		PriorityElement node = this;
		while (node.getFather() != null) {
			word = node.getWord() + word;
			node = node.getFather();
		}
		return word;
	}
	
	
	/**
	 * puts alle Followers of this node into a list, beginning with itself
	 * 
	 * @return list of all followers
	 */
	public LinkedList<PriorityElement> getListOfFollowers() {
		LinkedList<PriorityElement> ll = new LinkedList<PriorityElement>();
		for (PriorityElement pe : followers.values()) {
			ll.add(pe);
			ll.addAll(pe.getListOfFollowers());
		}
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
		suggest = this;
		for (PriorityElement pe : followers.values()) {
			if (pe.getSuggest().getFrequency() > suggest.getFrequency())
				suggest = pe.getSuggest();
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
	

	public char getWord() {
		return word;
	}
	

	public void setWord(char word) {
		this.word = word;
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
}
