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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

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
	/**
	 * name = profilename
	 * profile = path to profile config file
	 * layout = path to layout
	 * chars = path to the chars file
	 * tree = path to the tree file
	 * autoCompleting = true/false
	 * treeExpanding = true/false
	 * autoProfileChange = true/false
	 */

	private Properties					properties			= new Properties();
	private transient InputStream		defaultLayoutXML;
	private transient InputStream		defaultKeymapXML;
	private transient PriorityTree	tree;
	private transient KeyboardLayout	kbdLayout;
	
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
	public Profile(String pName, String datapath) {
		properties.setProperty("name", pName);
		properties.setProperty("autoCompleting", "true");
		properties.setProperty("treeExpanding", "true");
		properties.setProperty("autoProfileChange", "true");

		String name = properties.getProperty("name");
		File file = new File(datapath + "/profiles");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		File profileDir = new File(datapath + "/profiles/" + name);
		if (!profileDir.isDirectory()) {
			profileDir.mkdir();
		}
		properties.setProperty("layout", datapath + "/profiles/" + name + "/" + name + ".layout");
		properties.setProperty("profile", datapath + "/profiles/" + name + "/" + name + ".profile");
		properties.setProperty("tree", datapath + "/profiles/" + name + "/" + name + ".tree");
		properties.setProperty("chars", datapath + "/profiles/" + name + "/" + name + ".chars");
		
		logger.debug("Profile " + name + " created");
		load();
		save();
	}
	
	
	public Profile(Properties prop) {
		properties = prop;
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
		saveProfile();
	}
	
	
	private void saveProfile() {
		FileOutputStream fis;
		try {
			fis = new FileOutputStream(properties.getProperty("profile"));
			properties.store(fis, "stored by saving the profile");
		} catch (IOException err) {
			logger.info("Could not save the profile");
		}
	}
	
	
	/**
	 * Save layout to file
	 * 
	 * @author NicolaiO
	 */
	private void saveLayout() {
		if (kbdLayout != null) {
			KeyboardLayoutSaver.save(kbdLayout, properties.getProperty("layout"));
		}
	}
	
	
	private void loadDefaultPathes() {
		defaultLayoutXML = getClass().getResourceAsStream("/res/default/layout_default.xml");
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
		File file = new File(properties.getProperty("layout"));
		if (file.exists()) {
			kbdLayout = KeyboardLayoutLoader.load(file, KeymapLoader.load(defaultKeymapXML));
		} else {
			logger.info("Default Layout loaded");
			kbdLayout = KeyboardLayoutLoader.load(defaultLayoutXML, KeymapLoader.load(defaultKeymapXML));
		}
		for (MuteButton mb : kbdLayout.getMuteButtons()) {
			switch (mb.getType()) {
				case MuteButton.AUTO_COMPLETING:
					mb.setActivated(properties.getProperty("autoCompleting").equals("true"));
					break;
				case MuteButton.AUTO_PROFILE_CHANGE:
					mb.setActivated(properties.getProperty("autoProfileChange").equals("true"));
					break;
				case MuteButton.TREE_EXPANDING:
					mb.setActivated(properties.getProperty("treeExpanding").equals("true"));
					break;
				default:
					break;
			}
		}
	}
	
	
	/**
	 * Loads the (serialized) PriorityTree.
	 * 
	 * @author DirkK
	 */
	private void loadTree() {
		tree = new PriorityTree(properties.getProperty("chars"));
		try {
			tree.importFromHashMap(ImportExportManager.importFromFile(properties.getProperty("tree"), true));
		} catch (IOException err) {
			logger.warn("Could not fetch the dictionary for the proifle " + properties.getProperty("name") + ", File: "
					+ properties.getProperty("tree"));
		}
		logger.debug("Tree successfully loaded");
	}
	
	
	/**
	 * Saves the PriorityTree as serialized object
	 * 
	 * @author DirkK
	 */
	private void saveTree() {
		if (tree != null) {
			logger.debug("save tree to " + properties.getProperty("tree"));
			try {
				ImportExportManager.exportToFile(tree.exportToHashMap(), properties.getProperty("tree"));
			} catch (IOException err) {
				logger.error("Not able to save the tree for proifle " + properties.getProperty("name") + " to "
						+ properties.getProperty("tree"));
			}
			logger.debug("save Chars to " + properties.getProperty("chars"));
			tree.saveAllowedChars();
		} else {
			logger.debug("Tree not saved, because not existend");
		}
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
		return properties.getProperty("name");
	}
	
	
	/**
	 * Sets a profile's name
	 * 
	 * @param newName - String
	 * @author SebastianN
	 */
	public void setName(String newName) {
		properties.setProperty("name", newName);
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
		return properties.getProperty("autoProfileChange").equals("true");
	}
	
	
	public void setAutoProfileChange(boolean autoProfileChange) {
		properties.setProperty("autoProfileChange", String.valueOf(autoProfileChange));
	}
	
	
	public boolean isAutoCompleting() {
		return properties.getProperty("autoCompleting").equals("true");
	}
	
	
	public void setAutoCompleting(boolean autoCompleting) {
		properties.setProperty("autoCompleting", String.valueOf(autoCompleting));
	}
	
	
	public boolean isTreeExpanding() {
		return properties.getProperty("treeExpanding").equals("true");
	}
	
	
	public void setTreeExpanding(boolean treeExpanding) {
		properties.setProperty("treeExpanding", String.valueOf(treeExpanding));
	}
	
	
	public Properties getProperties() {
		return properties;
	}
	
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	
	public HashMap<String, String> getPaths() {
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("profile", properties.getProperty("profile"));
		hash.put("layout", properties.getProperty("layout"));
		hash.put("tree", properties.getProperty("tree"));
		hash.put("chars", properties.getProperty("chars"));
		return hash;
	}

}
