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

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import edu.dhbw.t10.helper.StringHelper;
import edu.dhbw.t10.manager.keyboard.KeyboardLayoutLoader;
import edu.dhbw.t10.manager.keyboard.KeyboardLayoutSaver;
import edu.dhbw.t10.manager.keyboard.KeymapLoader;
import edu.dhbw.t10.manager.profile.ImportExportManager;
import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.keyboard.key.MuteButton;
import edu.dhbw.t10.type.tree.PriorityTree;


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
	// private String pathToTree;
	// private String pathToProfile;
	// private String pathToAllowedChars;
	// private String pathToLayoutFile;
	private HashMap<String, String>	paths;
	// private String pathToKeymapFile;
	private transient InputStream		defaultLayoutXML;
	private transient InputStream		defaultKeymapXML;
	private transient PriorityTree	tree;
	private transient KeyboardLayout	kbdLayout;
	
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
		name = pName;
		init(paths);
		save();
	}
	
	
	/**
	 * 
	 * Initializes the necessary information for the profile (such as paths)
	 * 
	 * @author SebastianN
	 */
	private void init(String datapath) {
		File file = new File(datapath + "/profiles");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		File profileDir = new File(datapath + "/profiles/" + name);
		if (!profileDir.isDirectory()) {
			profileDir.mkdir();
		}
		paths = new HashMap<String, String>();
		paths.put("layout", datapath + "/profiles/" + name + "/" + name + ".layout");
		paths.put("profile", datapath + "/profiles/" + name + "/" + name + ".profile");
		paths.put("tree", datapath + "/profiles/" + name + "/" + name + ".tree");
		paths.put("chars", datapath + "/profiles/" + name + "/" + name + ".chars");
		
		logger.debug("Profile " + name + " created");
		load();
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public String toString() {
		return getName();
	}


	/**
	 * Load layout and tree from file
	 * 
	 * @author NicolaiO
	 */
	public void load() {
		loadDefaultPathes();
		loadLayout();
		loadTree();
	}
	
	
	/**
	 * Save profile (layout and tree)
	 * 
	 * @author NicolaiO
	 */
	public void save() {
		saveLayout();
		saveTree();
	}
	
	
	/**
	 * Save layout to file
	 * 
	 * @author NicolaiO
	 */
	private void saveLayout() {
		KeyboardLayoutSaver.save(kbdLayout, paths.get("layout"));
	}
	
	
	private void loadDefaultPathes() {
		if (defaultLayoutXML == null)
			defaultLayoutXML = getClass().getResourceAsStream("/res/default/layout_default.xml");
		if (defaultKeymapXML == null)
			defaultKeymapXML = getClass().getResourceAsStream("/res/default/keymap_default.xml");
		if (defaultLayoutXML == null || defaultKeymapXML == null) {
			logger.error("Could not load default layout file. Program will not run well...");
		}
	}


	/**
	 * load layout from layout file
	 * 
	 * @author NicolaiO
	 */
	private void loadLayout() {
		if (kbdLayout == null) {
			File file = new File(paths.get("layout"));
			if (file.exists()) {
				kbdLayout = KeyboardLayoutLoader.load(file, KeymapLoader.load(defaultKeymapXML));
			} else {
				logger.info("Default Layout loaded");
				kbdLayout = KeyboardLayoutLoader.load(defaultLayoutXML, KeymapLoader.load(defaultKeymapXML));
			}
			for (MuteButton mb : kbdLayout.getMuteButtons()) {
				switch (mb.getType()) {
					case MuteButton.AUTO_COMPLETING:
						mb.setActivated(autoCompleting);
						break;
					case MuteButton.AUTO_PROFILE_CHANGE:
						mb.setActivated(autoProfileChange);
						break;
					case MuteButton.TREE_EXPANDING:
						mb.setActivated(treeExpanding);
						break;
					default:
						break;
				}
			}
		}
	}
	
	
	/**
	 * Loads the (serialized) PriorityTree.
	 * 
	 * @author DirkK
	 */
	private void loadTree() {
		if (tree == null) {
			tree = new PriorityTree(paths.get("chars"));
			try {
				tree.importFromHashMap(ImportExportManager.importFromFile(paths.get("tree"), true));
			} catch (IOException err) {
				logger.warn("Could not fetch the dictionary for the proifle " + name + ", File: " + paths.get("tree"));
			}
			logger.debug("Tree successfully loaded");
		}
	}
	
	
	/**
	 * Saves the PriorityTree as serialized object
	 * 
	 * @author DirkK
	 */
	private void saveTree() {
		if (tree != null) {
			logger.debug("save tree to " + paths.get("tree"));
			try {
				ImportExportManager.exportToFile(tree.exportToHashMap(), paths.get("tree"));
			} catch (IOException err) {
				logger.error("Not able to save the tree for proifle " + name + " to " + paths.get("tree"));
			}
		} else {
			logger.debug("Tree not saved, because not existend");
		}
		logger.debug("save Chars to " + paths.get("chars"));
		tree.saveAllowedChars();
	}
	
	
	/**
	 * Controller requests a Word suggestion with an given Startstring.
	 * 
	 * @param givenChars
	 * @return wordsuggest
	 * @author DirkK
	 */
	public String getWordSuggest(String givenChars) {
		if (isAutoCompleting()) {
			if (getTree() == null) {
				logger.error("PriorityTree of activeProfile==NULL at getWordSuggest");
				return "";
			}
			return getTree().getSuggest(givenChars);
		} else {
			return givenChars;
		}
	}
	
	
	/**
	 * Gives a word which have to be inserted or updated in the data.
	 * 
	 * @param word A complete word to be inserted into tree
	 * @author SebastianN
	 */
	public boolean acceptWord(String word) {
		word = StringHelper.removePunctuation(word);
		if (isTreeExpanding())
			return getTree().insert(word);
		return false;
	}
	
	
	/**
	 * Load the lists of all ddls. (currently only one exists)
	 * Existing items will be removed!
	 * 
	 * @author NicolaiO
	 */
	public void loadDDLs(ArrayList<Profile> profiles) {
		ArrayList<DropDownList> DDLs = getKbdLayout().getDdls();
		for (DropDownList ddl : DDLs) {
			switch (ddl.getType()) {
				case DropDownList.PROFILE:
					// save all action listeners
					ActionListener[] als = ddl.getActionListeners();
					// delete all action listeners, so that they can't be called until we are done
					// e.g. addItem will trigger an ActionEvent!
					for (int i = 0; i < als.length; i++) {
						ddl.removeActionListener(als[i]);
					}
					
					// remove all existing items (normally, there shouldn't be any...
					ddl.removeAllItems();
					
					// add all profiles
					for (Profile p : profiles) {
						ddl.addItem(p.getName());
					}
					
					// set active profile selected
					ddl.setSelectedItem(getName());
					
					logger.debug("loaded " + ddl.getItemCount() + " items in profile-ddl");
					logger.debug("Selected item is: " + ddl.getSelectedItem() + " should be: " + this);
					
					// now, where we are done, add all listeners back
					for (int i = 0; i < als.length; i++) {
						ddl.addActionListener(als[i]);
					}
					
					// do a revalidate to reload the ddl
					ddl.revalidate();
					ddl.repaint();
					
					break;
				default:
					logger.warn("UNKOWN DDL found!");
			}
		}
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Gets a profile's name
	 * 
	 * @return name
	 * @author SebastianN
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * @deprecated
	 * @return
	 * @author dirk
	 */
	public String getPathToAllowedChars() {
		return paths.get("chars");
	}
	
	
	/**
	 * @deprecated
	 * @author dirk
	 */
	public void setPathToAllowedChars(String pathToAllowedChars) {
		paths.remove("chars");
		paths.put("chars", pathToAllowedChars);
	}
	
	
	/**
	 * @deprecated
	 * @return
	 * @author dirk
	 */
	public String getPathToLayoutFile() {
		return paths.get("layout");
	}
	
	
	/**
	 * @deprecated
	 * @author dirk
	 */
	public void setPathToLayoutFile(String pathToLayoutFile) {
		paths.remove("layout");
		paths.put("layout", pathToLayoutFile);
	}
	
	
	/**
	 * Sets a profile's name
	 * 
	 * @param newName - String
	 * @author SebastianN
	 */
	public void setName(String newName) {
		name = newName;
	}
	
	
	/**
	 * @deprecated
	 * @return
	 * @author dirk
	 */
	public String getPathToProfile() {
		return paths.get("profile");
	}
	
	
	/**
	 * @deprecated
	 * @author dirk
	 */
	public void setPathToProfile(String path) {
		paths.remove("profile");
		paths.put("profile", path);
	}
	
	
	/**
	 * @deprecated
	 * @return
	 * @author dirk
	 */
	public String getPathToTree() {
		return paths.get("tree");
	}
	
	
	/**
	 * @deprecated
	 * @author dirk
	 */
	public void setPathToTree(String pathToFile) {
		paths.remove("tree");
		paths.put("tree", pathToFile);
	}
	
	
	/**
	 * @return
	 * @author dirk
	 */
	public PriorityTree getTree() {
		return tree;
	}
	
	
	/**
	 * @author dirk
	 */
	public void setTree(PriorityTree tree) {
		this.tree = tree;
	}
	
	
	public KeyboardLayout getKbdLayout() {
		return kbdLayout;
	}
	
	
	public void setKbdLayout(KeyboardLayout kbdLayout) {
		this.kbdLayout = kbdLayout;
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
	
	
	public HashMap<String, String> getPaths() {
		return paths;
	}
	
	
	public void setPaths(HashMap<String, String> paths) {
		this.paths = paths;
	}


}
