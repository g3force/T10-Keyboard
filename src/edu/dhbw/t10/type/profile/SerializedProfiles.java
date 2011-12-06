
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

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.tree.PriorityTree;


/**
 *
 * Profile-Handle. It includes the name, the paths to its PriorityTree-/Profile-file,
 * as well as the PriorityTree itself.
 *
 * @author SebastianN
 *
 */
public class SerializedProfiles implements Serializable {
        /**  */
        private static final long                       serialVersionUID        = 5085464540715301877L;
        // --------------------------------------------------------------------------
        // --- variables and constants ----------------------------------------------
        // --------------------------------------------------------------------------
        private String                                                  name;
        // private String pathToTree;
        // private String pathToProfile;
        // private String pathToAllowedChars;
        // private String pathToLayoutFile;
        private HashMap<String, String> paths;
        // private String pathToKeymapFile;
        private transient InputStream           defaultLayoutXML;
        private transient InputStream           defaultKeymapXML;
        private transient PriorityTree  tree;
        private transient KeyboardLayout        kbdLayout;
       
        private boolean                                         autoProfileChange       = true;
        private boolean                                         autoCompleting          = true;
        private boolean                                         treeExpanding           = true;
       
        @SuppressWarnings("unused")
        private static final Logger             logger                          = Logger.getLogger(Profile.class);
       
       
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
	public SerializedProfiles(String pName, String paths) {

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