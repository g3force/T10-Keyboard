/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.profile;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.profile.Serializer;
import edu.dhbw.t10.type.tree.PriorityTree;


/**
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
	
	@SuppressWarnings("unused")
	private static final Logger	logger	= Logger.getLogger(Profile.class);

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public Profile() {
		profileID = -1;
		name = "";
		pathToFile = "";
		tree = null;
		// tree = new PriorityTree();
	}
	
	
	public Profile(int pID, String pName, String pPath, PriorityTree ptree) {
		profileID = pID;
		name = pName;
		pathToFile = pPath;
		tree = ptree;
		// tree = new PriorityTree();
	}
	
	
	public Profile(int pID, String pName, String pPath) {
		profileID = pID;
		name = pName;
		pathToFile = pPath;
		tree = new PriorityTree();
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public void loadTree() {
		tree = Serializer.deserialize(pathToFile);
	}
	
	
	public void saveTree() {
		Serializer.serialize(tree, pathToFile);
	}

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
	
	
	public int getProfileID() {
		return profileID;
	}
	
	
	public void setProfileID(int profileID) {
		this.profileID = profileID;
	}
	
	
	public String getPathToFile() {
		return pathToFile;
	}
	
	
	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}
	
	
	public PriorityTree getTree() {
		return tree;
	}
	
	
	public void setTree(PriorityTree tree) {
		this.tree = tree;
	}


}
