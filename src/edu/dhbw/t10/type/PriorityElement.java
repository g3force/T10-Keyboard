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
		suggest = null;
		frequency = fr;
		followers = null;
	}
	

	public PriorityElement(char wo, PriorityElement fa, PriorityElement su, int fr) {
		word = wo;
		father = fa;
		suggest = su;
		frequency = fr;
		followers = null;
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
		PriorityElement newFollower = new PriorityElement(c, this, 1);
		newFollower.setSuggest(newFollower);
		followers.put(c, newFollower);
		return newFollower;
	}
	

	public boolean hasFollower(char c) {
		return followers.containsKey(c);
	}
	

	public PriorityElement getFollower(char c) {
		return followers.get(c);
	}
	

	public PriorityElement clone() {
		return new PriorityElement(word, father, suggest, frequency, followers);
	}
	

	public void increase() {
		frequency++;
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
