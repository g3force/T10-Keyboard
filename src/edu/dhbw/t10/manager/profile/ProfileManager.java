/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.profile;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.keyboard.KeyboardLayoutLoader;
import edu.dhbw.t10.manager.keyboard.KeymapLoader;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.profile.Profile;
import edu.dhbw.t10.view.Presenter;
import edu.dhbw.t10.view.panels.MainPanel;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class ProfileManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(ProfileManager.class);
	private static ProfileManager	instance;
	private ArrayList<Profile>		profiles;
	private ArrayList<String>		profilePath;
	private Profile					activeProfile;
	private KeyboardLayout			kbdLayout;
	private boolean					autoProfileChange	= true;
	private boolean					autoCompleting		= true;
	private boolean					treeExpanding		= true;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	// Singleton
	/**
	 * Constructor as Singleton. This way, we prevent having multiple manager and
	 */
	private ProfileManager() {
		// ....
		profilePath = new ArrayList<String>();
		getSerializedProfiles();
		if (profiles.size() == 0) {
			Profile prof = new Profile(0, "default");
			prof.saveTree();
			profiles.add(new Profile());
		}
		activeProfile = profiles.get(0); // TODO save active profile
		// ---------------------DUMMY CODE------------------------------
		Profile prof = new Profile(1, "Pflichteheft");
		setActive(prof);
		prof.setPathToTree("conf/trees/" + prof.getName());
		prof.saveTree();
		profiles.add(prof);

		// -------------------ENDE DUMMY CODE---------------------------
		serializeProfiles();
		instance = this;
		kbdLayout = KeyboardLayoutLoader
				.load("conf/keyboard_layout_de_default.xml", KeymapLoader.load("conf/keymap.xml"));
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return Return of an instance of the Singleton "ProfileManager", thus preventing
	 *         multiple ProfileManager.
	 */
	public static ProfileManager getInstance() {
		if (instance == null) {
			instance = new ProfileManager();
		}
		return instance;
	}
	

	public void resizeWindow(Dimension size) {
		if (kbdLayout != null) {
			float xscale = (float) size.width / (float) kbdLayout.getOrigSize_x();
			float yscale = (float) size.height / (float) kbdLayout.getOrigSize_y();
			float fontScale = xscale + yscale / 2;
			kbdLayout.setScale_x(xscale);
			kbdLayout.setScale_y(yscale);
			kbdLayout.setScale_font(fontScale);
			kbdLayout.rescale();
			MainPanel.getInstance().setPreferredSize(new Dimension(kbdLayout.getSize_x(), kbdLayout.getSize_y()));
			Presenter.getInstance().pack();
		}
	}
	
	
	public void readConfig() {
		try {
			File confFile = new File("./config");
			FileReader fr = new FileReader(confFile);
			BufferedReader br = new BufferedReader(fr);
			
			String entry = "";
			while ((entry = br.readLine()) != null) {
				// Commentary-Indicator: //
				if (entry.indexOf("//") >= 0)
					entry = entry.substring(0, entry.indexOf("//"));
				
				if (entry.isEmpty())
					continue;
				
				// Indicators deleted.
				int posOfEql = entry.indexOf("=");
				String valName = entry.substring(0, posOfEql);
				String value = entry.substring(posOfEql + 1, entry.length());
				if (valName.equals("ProfilePath")) {
					profilePath.add(value);
				} else if (valName.equals("XMLPath")) {
					// XMLPath.add(value);
					logger.debug("XMLPath: " + value);
				}
			}
			br.close();
		} catch (IOException io) {
			logger.debug("IOException in readConfig()");
			io.printStackTrace();
		} catch (Exception ex) {
			logger.debug("Exception in readConfig(): " + ex.toString());
			ex.printStackTrace();
		}
	}
	
	private String createComment(String comment) {
		comment = "//" + comment;
		return comment;
	}
	
	
	public void saveConfig() {
		try {
			File confFile = new File("./config");
			FileWriter fw = new FileWriter(confFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(createComment("Configfile for T10"));
			
			for (int i = 0; i < profilePath.size(); i++) {
				bw.write("ProfilePath=" + profilePath.get(i) + "\n");
			}
			bw.close();
		} catch (IOException io) {
			logger.debug("IOException in readConfig()");
			io.printStackTrace();
		} catch (Exception ex) {
			logger.debug("Exception in readConfig(): " + ex.toString());
			ex.printStackTrace();
		}
	}

	
	/**
	 * 
	 * Create a new profile
	 * 
	 * @param profileName - String. Name of the profile.
	 * @param pathToNewProfile - String. Path to the new profile.
	 * @return Handle/Pointer to the new profile.
	 * @author DerBaschti
	 */

	public Profile create(String profileName, String pathToNewProfile) {
		Profile newProfile = new Profile();
		newProfile.setName(profileName);
		newProfile.setID(profiles.size());
		profilePath.add(pathToNewProfile);
		profiles.add(newProfile);
		if (activeProfile == null) {
			logger.info("Active profile was set to newProfile");
			activeProfile = newProfile;
		}
		return newProfile;
	}
	

	/**
	 * 
	 * Re-arranges the IDs of the profiles after a certain profile gets deleted.
	 * 
	 */
	private void arrangeProfiles() {
		if (profiles.size() <= 0) {
			logger.debug("profiles.size()==0 at arrange");
			return;
		}
		Profile curProfile = null;
		for (int i = 0; i < profiles.size(); i++) {
			curProfile = profiles.get(i);
			curProfile.setID(i);
		}
	}
	

	/**
	 * 
	 * Deletes a profile depending on the ID.<br/>
	 * If the ID we deleted was currently active,
	 * we either mark the first profile as active or mark that we need a new profile.
	 * 
	 * @param id - int. ID of the profile you want to delete.
	 */
	public void delete(int id) {
		Profile curProfile = null;
		if (profiles.size() <= 0) {
			logger.debug("profiles.size()==0 at delete");
			return;
		}
		for (int i = 0; i < profiles.size(); i++) {
			curProfile = profiles.get(i);
			if (curProfile.getID() == id) {
				profiles.remove(i);
				arrangeProfiles();
				break;
			}
		}
		// If the deleted profile was currently active, we choose the first profile or mark "we need a new profile!"
		if (activeProfile.getID() == id) {
			if (profiles.size() > 0) {
				logger.debug("activeProfile=profiles(0)");
				activeProfile = profiles.get(0);
			} else {
				logger.debug("activeProfile=NULL");
				activeProfile = null;
			}
		}
	}
	

	/**
	 * 
	 * Marks a profile (depending on the ID) as "active". If not found, nothing happens.
	 * 
	 * @param id
	 */
	public void setActiveByID(int id) {
		Profile curProfile = null;
		for (int i = 0; i < profiles.size(); i++) {
			curProfile = profiles.get(i);
			if (curProfile.getID() == id) {
				setActive(curProfile);
				break;
			}
		}
	}
	
	
	public void setActive(Profile newActive) {
		activeProfile.saveTree();
		activeProfile = newActive;
		activeProfile.loadTree();
	}


	/**
	 * 
	 * TODO ??? implementieren... siehe Kontrollfluss Diagramm
	 * OutputManager requests a Word suggestion with an given Startstring.
	 * @param givenChars
	 * @return
	 */
	public String getWordSuggest(String givenChars) {
		if (autoCompleting) {
			if (getActive() == null) {
				logger.error("getActive()==NULL at getWordSuggest");
				return "";
			} else if (getActive().getTree() == null) {
				logger.error("PriorityTree of activeProfile==NULL at getWordSuggest");
				return "";
			}
			return getActive().getTree().getSuggest(givenChars);
		} else {
			return givenChars;
		}

	}
	
	
	/**
	 * 
	 * TODO ??? implementieren...
	 * Gives a word which have to be inserted or updated in the data.
	 * 
	 * @param word
	 */
	public void acceptWord(String word) {
		if (getActive() == null) {
			logger.error("getActive()==NULL at acceptWord");
			return;
		}
		if (treeExpanding)
			getActive().getTree().insert(word);
	}

	
	/**
	 * 
	 * Serializing Profile-Arraylist
	 * 
	 */
	public void serializeProfiles() {
		for (int i = 0; i < profiles.size(); i++) {
			Profile cProfile = profiles.get(i);
			if (cProfile.getPathToProfile() == null || cProfile.getPathToProfile().isEmpty())
				continue;
			Serializer.serialize(cProfile, cProfile.getPathToProfile());
		}
	}
	
	
	public void getSerializedProfiles() {
		for (int i = 0; i < profilePath.size(); i++)
			profiles.add((Profile) Serializer.deserialize(profilePath.get(i)));
		if (profiles == null) {
			profiles = new ArrayList<Profile>();
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public ArrayList<Profile> getProfiles() {
		return profiles;
	}

	public Profile getActive() {
		return activeProfile;
	}

	public KeyboardLayout getKbdLayout() {
		return kbdLayout;
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
