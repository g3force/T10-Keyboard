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
public class Profile {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private int		profileID;
	private String	name;
	private String			pathToFile;
	private PriorityTree	tree;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public Profile() {
		profileID = -1;
		name = "";
		pathToFile = "";
		// tree = new PriorityTree();
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * 
	 * Gets the profile-ID
	 * 
	 * @return profileID
	 */
	public int getID() {
		return profileID;
	}
	
	
	/**
	 * 
	 * Sets the profile-ID
	 * 
	 * @param id - int
	 */
	
	public void setID(int id) {
		profileID = id;
	}
	
	
	/**
	 * 
	 * Gets a profile's name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * 
	 * Sets a profile's name
	 * 
	 * @param newName - String
	 */
	
	public void setName(String newName) {
		name = newName;
	}
}
