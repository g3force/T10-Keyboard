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

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;


/**
 * 
 * Profile-Handle. It includes the name, the paths to its PriorityTree-/Profile-file,
 * as well as the PriorityTree itself.
 * 
 * @author SebastianN
 * 
 */
public class Profile implements Serializable {
	/**  */
	private static final long			serialVersionUID	= 5085464540715301877L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private String							name;
	private HashMap<String, String>	paths;
	
	private boolean						autoProfileChange	= true;
	private boolean						autoCompleting		= true;
	private boolean						treeExpanding		= true;
	
	@SuppressWarnings("unused")
	private static final Logger		logger				= Logger.getLogger(Profile.class);
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * 
	 * Constructor of Profile.
	 * 
	 * @param pName - Name of the new profile
	 * @author SebastianN
	 */
	public Profile(String pName, String paths) {
		
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public HashMap<String, String> getPaths() {
		return paths;
	}
	
	
	public void setPaths(HashMap<String, String> paths) {
		this.paths = paths;
	}
	
	
	public boolean isAutoProfileChange() {
		return autoProfileChange;
	}
	
	
	public void setAutoProfileChange(boolean autoProfileChange) {
		this.autoProfileChange = autoProfileChange;
	}
	
	
	public boolean isAutoCompleting() {
		return autoCompleting;
	}
	
	
	public void setAutoCompleting(boolean autoCompleting) {
		this.autoCompleting = autoCompleting;
	}
	
	
	public boolean isTreeExpanding() {
		return treeExpanding;
	}
	
	
	public void setTreeExpanding(boolean treeExpanding) {
		this.treeExpanding = treeExpanding;
	}


}