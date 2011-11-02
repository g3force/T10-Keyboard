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
import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.keyboard.KeyboardLayoutLoader;
import edu.dhbw.t10.manager.keyboard.KeyboardLayoutSaver;
import edu.dhbw.t10.manager.keyboard.KeymapLoader;
import edu.dhbw.t10.manager.profile.ImportExportManager;
import edu.dhbw.t10.manager.profile.Serializer;
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
		File file = new File("profiles");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		pathToProfile = "data/profiles/" + name + ".profile";
		file = new File("trees");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		pathToTree = "data/trees/" + name + ".tree";
		pathToAllowedChars = "data/trees/" + name + ".chars";

		tree = new PriorityTree(pathToAllowedChars);
		saveTree();

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
		try {
			tree = Serializer.deserialize(pathToTree);
			tree.importFromHashMap(ImportExportManager.importFromFile(pathToTree, true));
		} catch (IOException io) {
			tree = new PriorityTree(pathToAllowedChars);
			logger.warn("No Tree found for Profile" + name + ", new Tree created");
		}
	}
	
	
	/**
	 * 
	 * Saves the PriorityTree as serialized object
	 * 
	 * @author DirkK
	 */
	private void saveTree() {
		try {
			Serializer.serialize(tree, pathToTree);
		} catch (IOException io) {
			logger.error("Tree not saved, IOException.");
		}
		if (tree != null) {
			ImportExportManager.exportToFile(tree.exportToHashMap(), pathToTree);
		}
	}
	
	
	/**
	 * 
	 * TODO DirkK, add comment!
	 * 
	 * @author DirkK
	 */
	public void saveOrderedTree() {
		if (tree != null) {
			ImportExportManager.exportToSortedFile(tree.exportToHashMap(), pathToTree);
		}
	}
	

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
