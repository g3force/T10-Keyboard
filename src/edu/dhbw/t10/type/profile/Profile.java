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

import java.io.IOException;

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
	private String			pathToTree;
	private String						pathToProfile;
	private PriorityTree				tree;
	
	@SuppressWarnings("unused")
	private static final Logger	logger	= Logger.getLogger(Profile.class);

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public Profile() {
		profileID = -1;
		name = "";
		pathToTree = "";
		pathToProfile = "";
		tree = null;
		// tree = new PriorityTree();
	}

	
	public Profile(int pID, String pName, String pPath, PriorityTree ptree, String pFile) {
		profileID = pID;
		name = pName;
		pathToTree = pPath;
		tree = ptree;
		pathToProfile = pFile;
		// tree = new PriorityTree();
	}
	
	public Profile(int pID, String pName, String pPath, PriorityTree ptree) {
		profileID = pID;
		name = pName;
		pathToTree = pPath;
		tree = ptree;
		pathToProfile = "";
		// tree = new PriorityTree();
	}
	
	
	public Profile(int pID, String pName, String pPath) {
		profileID = pID;
		name = pName;
		pathToTree = pPath;
		tree = new PriorityTree();
		pathToProfile = "";
	}
	
	
	public Profile(int pID, String pName) {
		profileID = pID;
		name = pName;
		pathToTree = "conf/trees/" + name;
		tree = new PriorityTree();
		pathToProfile = "";
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public void loadTree() {
		try {
			tree = Serializer.deserialize(pathToTree);
		} catch (IOException io) {
			logger.error("IOException: " + io.toString());
		}
	}
	
	
	public void saveTree() {
		try {
			Serializer.serialize(tree, pathToTree);
		} catch (IOException io) {
			logger.error("IOException: " + io.toString());
		}
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
	
	
	public String getPathToProfile() {
		return pathToProfile;
	}
	
	
	public void setPathToProfile(String path) {
		pathToProfile = path;
	}

	
	public int getProfileID() {
		return profileID;
	}
	
	
	public void setProfileID(int profileID) {
		this.profileID = profileID;
	}
	
	
	public String getPathToTree() {
		return pathToTree;
	}
	
	
	public void setPathToTree(String pathToFile) {
		this.pathToTree = pathToFile;
	}
	
	
	public PriorityTree getTree() {
		return tree;
	}
	
	
	public void setTree(PriorityTree tree) {
		this.tree = tree;
	}


}
