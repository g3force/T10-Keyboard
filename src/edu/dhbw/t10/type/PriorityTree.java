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
public class PriorityTree
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private PriorityElement	root;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public PriorityTree()
	{
		root = new PriorityElement('\u0000', null, null, 0);
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public void insert(String in)
	{
		PriorityElement node = root.clone();
		char[] inChar = in.toCharArray(); //put every letter of the word alone in an char array
		for(int i=0; i<inChar.length; i++)
		{
			if (node.hasFollower(inChar[i]))
			{
				if (i < inChar.length - 1)
				{
					node = node.getFollower(inChar[i]);
				} else
				{
					node.increase();
					ReplaceSuggestsInFathers(node);
				}
			}
			else {
				node = node.addFollower(inChar[i]);
			}
		}
	}
	
	
	private void ReplaceSuggestsInFathers(PriorityElement node)
	{
		int freq = node.getFrequency();
		while(node.getFather().getSuggest().getFrequency()<freq)
		{
			node.getFather().setSuggest(node);
			node = node.getFather();
		}
	}


	public String getSuggest(String wordPart)
	{
		PriorityElement node = root.clone();
		char[] wordPartChar = wordPart.toCharArray(); // put every letter of the word alone in a char array
		for (int i = 0; i < wordPartChar.length; i++)
		{
			if (node.hasFollower(wordPartChar[i]))
			{
				if (i < wordPartChar.length - 1)
				{
					node = node.getFollower(wordPartChar[i]);
				} else
				{
					return node.buildWord();
				}
			}
 else
			{
				return wordPart;
			}
		}
		return "FAILURE";
	}
	
	
	public void printTree()
	{
		// TODO a print method to test insert and getWord
	}
	
	


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
