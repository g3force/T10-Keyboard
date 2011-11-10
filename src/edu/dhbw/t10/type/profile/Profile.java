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

import java.io.File;
import java.io.Serializable;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.keyboard.KeyboardLayoutLoader;
import edu.dhbw.t10.manager.keyboard.KeyboardLayoutSaver;
import edu.dhbw.t10.manager.keyboard.KeymapLoader;
import edu.dhbw.t10.manager.profile.ImportExportManager;
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
	private static final long			serialVersionUID	= 5085464540715301875L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private String							name;
	private String							pathToTree;
	private String							pathToProfile;
	private String							pathToAllowedChars;
	private String							pathToLayoutFile;
	// private String pathToKeymapFile;
	private static final String		defaultLayoutFile	= "data/default/layout_default.xml";
	private static final String		defaultKeymapFile	= "data/default/keymap_default.xml";
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
	
	
	public Profile(String pName) {
		name = pName;
		pathToLayoutFile = "data/layout/" + name + ".layout";
		init();
	}
	
	
	public Profile(String pName, String layoutPath) {
		name = pName;
		pathToLayoutFile = layoutPath;
		init();
	}
	
	
	private void init() {
		File file = new File("data/profiles");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		file = new File("data/trees");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		file = new File("data/layout");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		pathToProfile = "data/profiles/" + name + ".profile";
		pathToTree = "data/trees/" + name + ".tree";
		pathToAllowedChars = "data/trees/" + name + ".chars";
		logger.debug("Profile " + name + " created");
		load();
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Load layout and tree from file
	 * 
	 * @author NicolaiO
	 */
	public void load() {
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
		KeyboardLayoutSaver.save(kbdLayout, pathToLayoutFile);
	}
	
	
	/**
	 * load layout from layout file
	 * 
	 * @author NicolaiO
	 */
	private void loadLayout() {
		File file = new File(pathToLayoutFile);
		if (file.exists()) {
			kbdLayout = KeyboardLayoutLoader.load(pathToLayoutFile, KeymapLoader.load(defaultKeymapFile));
		} else {
			logger.info("Default Layout loaded");
			kbdLayout = KeyboardLayoutLoader.load(defaultLayoutFile, KeymapLoader.load(defaultKeymapFile));
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
	

	/**
	 * 
	 * Loads the (serialized) PriorityTree.
	 * 
	 * @author DirkK
	 */
	private void loadTree() {
		tree = new PriorityTree(pathToAllowedChars);
		tree.importFromHashMap(ImportExportManager.importFromFile(pathToTree, true));
		logger.debug("load: " + pathToTree + " Tree Size: " + tree.exportToHashMap().size());
	}
	
	
	/**
	 * 
	 * Saves the PriorityTree as serialized object
	 * 
	 * @author DirkK
	 */
	private void saveTree() {
		if (tree != null) {
			logger.debug("save tree to " + pathToTree);
			ImportExportManager.exportToFile(tree.exportToHashMap(), pathToTree);
		} else {
			logger.debug("Tree not saved, because not existend");
		}
		logger.debug("save Chars to " + pathToAllowedChars);
		tree.saveAllowedChars();
	}
	
	
	/**
	 * unused
	 * TODO DirkK, delete
	 * @author DirkK
	 */
	// public void saveOrderedTree() {
	// if (tree != null) {
	// ImportExportManager.exportToSortedFile(tree.exportToHashMap(), pathToTree);
	// }
	// }
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
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

	
}
