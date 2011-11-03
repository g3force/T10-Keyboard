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
import edu.dhbw.t10.type.tree.PriorityTree;
import edu.dhbw.t10.view.panels.MainPanel;


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
	private static final long			serialVersionUID	= 5085464540715301874L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private String							name;
	private String							pathToTree;
	private String							pathToProfile;
	private String							pathToAllowedChars;
	private transient PriorityTree	tree;
	private transient KeyboardLayout	kbdLayout;
	
	@SuppressWarnings("unused")
	private static final Logger		logger				= Logger.getLogger(Profile.class);
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	public Profile(String pName) {
		name = pName;
		File file = new File("data/profiles");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		pathToProfile = "data/profiles/" + name + ".profile";
		file = new File("data/trees");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		pathToTree = "data/trees/" + name + ".tree";
		pathToAllowedChars = "data/trees/" + name + ".chars";
		logger.debug("Profile " + pName + " created");
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
		logger.error("load Layout");
		loadLayout();
		logger.error("loaded Layout");
		tree = new PriorityTree(pathToAllowedChars);
		loadTree();
	}
	
	
	/**
	 * Save profile (layout and tree)
	 * 
	 * @author NicolaiO
	 */
	public void save() {
		logger.error("save Layout");
		saveLayout();
		logger.error("saved Layout");
		saveTree();
	}
	
	
	/**
	 * Save layout to file
	 * 
	 * @author NicolaiO
	 */
	private void saveLayout() {
		KeyboardLayoutSaver.save(kbdLayout, "data/conf/keyboard_layout_de_default.out.xml");
	}
	
	
	/**
	 * load layout from layout file
	 * 
	 * @author NicolaiO
	 */
	private void loadLayout() {
		kbdLayout = KeyboardLayoutLoader.load("data/conf/keyboard_layout_de_default.xml",
				KeymapLoader.load("data/conf/keymap.xml"));
		MainPanel.getInstance().setKbdLayout(kbdLayout);
	}

	
	/**
	 * 
	 * Loads the (serialized) PriorityTree.
	 * 
	 * @author DirkK
	 */
	private void loadTree() {
		tree.importFromHashMap(ImportExportManager.importFromFile(pathToTree, true));
		logger.error("load: " + pathToTree + " Tree Size: " + tree.exportToHashMap().size());
	}
	
	
	/**
	 * 
	 * Saves the PriorityTree as serialized object
	 * 
	 * @author DirkK
	 */
	private void saveTree() {
		if (tree != null) {
			ImportExportManager.exportToFile(tree.exportToHashMap(), pathToTree);
		}
		logger.error("save: " + pathToTree);
		tree.saveAllowedChars();
		logger.error("save Chars: " + pathToAllowedChars);
	}
	
	
	/**
	 * unused
	 * TODO dirk, delete
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
	
	
}
