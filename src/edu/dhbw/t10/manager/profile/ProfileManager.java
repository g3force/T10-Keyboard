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

import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.profile.Profile;
import edu.dhbw.t10.view.Presenter;
import edu.dhbw.t10.view.panels.MainPanel;


/**
 * The profile-manager handles all profiles, including the path to its profile-file.
 * 
 * @author SebastianN
 * 
 */
public class ProfileManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger				= Logger.getLogger(ProfileManager.class);
	private static ProfileManager	instance				= new ProfileManager();
	private ArrayList<Profile>		profiles;
	private ArrayList<String>		profilePathes;
	private Profile					activeProfile;
	private String						activeProfileName;														// Just for initializing
	private boolean					autoProfileChange	= true;
	private boolean					autoCompleting		= true;
	private boolean					treeExpanding		= true;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	// Singleton
	/**
	 * Constructor as Singleton. This way, we prevent having multiple manager
	 */
	private ProfileManager() {
		logger.debug("initializing...");
		profilePathes = new ArrayList<String>();
		readConfig(); // fills activeProfileName and profilePathes with the data from the config file
		logger.debug("initializing... configfile: activeProfileName=" + activeProfileName + " profiles="
				+ profilePathes.size());
		getSerializedProfiles(); // deserializes all profiles, fills profiles
		logger.debug("initializing... deserializing the profiles");
		if (profiles.size() == 0) {
			logger.debug("dummy profile will be created");
			activeProfileName = "default";
			profiles.add(new Profile("default"));
		}
		activeProfile = getProfileByName(activeProfileName); // TODO save active profile
		logger.debug("initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return Return of an instance of the Singleton "ProfileManager", thus preventing
	 *         multiple ProfileManager.
	 */
	public static ProfileManager getInstance() {
		return instance;
	}
	
	
	public void resizeWindow(Dimension size) {
		KeyboardLayout kbdLayout = activeProfile.getKbdLayout();
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
			logger.debug("Window rescaled");
		}
	}
	
	
	/**
	 * 
	 * Reads the config-file with all entrys and assigns
	 * the read values.
	 * 
	 * @author SebastianN
	 */
	public void readConfig() {
		try {
			File confFile = new File("data/config.db");
			if (confFile.exists()) {
				FileReader fr = new FileReader(confFile);
				BufferedReader br = new BufferedReader(fr);
				
				String entry = "";
				while ((entry = br.readLine()) != null) {
					// Commentary-Indicator: //
					if (entry.indexOf("//") >= 0)
						entry = entry.substring(0, entry.indexOf("//"));
					
					if (entry.isEmpty()) // In case an entry was just a comment.
						continue;
					
					// Comment-Indicators deleted.
					// Regular Format:
					// ActiveProfile=NAMEOFPROFILE
					// ProfilePath=config.cfg
					// ProfilePath=C:\lol.cfg
					int posOfEql = entry.indexOf("=");
					
					// Split and afterwards assign values.
					try {
						String valName = entry.substring(0, posOfEql);
						String value = entry.substring(posOfEql + 1, entry.length());
						if (valName.equals("ProfilePath")) {
							profilePathes.add(value);
						} else if (valName.equals("XMLPath")) {
							// XMLPath.add(value);
							logger.debug("XMLPath: " + value);
						} else if (valName.equals("ActiveProfile")) {
							activeProfileName = value;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						logger.error("Exception in readConfig().assignValues: " + ex.toString());
					}
				}
				br.close();
			}
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
	 * Creates a comment for config-files.
	 * 
	 * @param comment - String. Comment you want to add.
	 * @return Changed comment as String.
	 * @author SebastianN
	 */
	private String createComment(String comment) {
		comment = "//" + comment;
		return comment;
	}
	
	
	/**
	 * 
	 * Saves the name of the active profile and the path to all profile-files.
	 * 
	 * @author SebastianN
	 */
	public void saveConfig() {
		try {
			File confFile = new File("data/config");
			FileWriter fw = new FileWriter(confFile);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(createComment("Configfile for T10\n"));
			
			if (activeProfile != null)
				bw.write("ActiveProfile=" + activeProfile.getName() + "\n");
			
			for (int i = 0; i < profiles.size(); i++) {
				bw.write("ProfilePath=" + profiles.get(i).getPathToProfile() + "\n");
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
	 * @author SebastianN
	 */
	
	public Profile create(String profileName, String pathToNewProfile) {
		Profile newProfile = new Profile(profileName);
		profilePathes.add(pathToNewProfile);
		profiles.add(newProfile);
		if (activeProfile == null) {
			logger.info("Active profile was set to newProfile");
			activeProfile = newProfile;
		}
		return newProfile;
	}
	
	
	/**
	 * 
	 * Get a Profile based on its name
	 * 
	 * @param name - String. Name of the profile.
	 * @return If found, handle/reference to said profile. Otherwise NULL
	 * @author SebastianN
	 */
	
	public Profile getProfileByName(String name) {
		if (!profiles.isEmpty()) {
			for (int i = 0; i < profiles.size(); i++) {
				if (profiles.get(i).getName().equals(name))
					return profiles.get(i);
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * Deletes a profile depending on the ID.<br/>
	 * If the ID we deleted was currently active,
	 * we either mark the first profile as active or mark that we need a new profile.
	 * 
	 * @param id - int. ID of the profile you want to delete.
	 */
	public void delete(Profile toDelete) {
		Profile curProfile = null;
		if (profiles.size() <= 0) {
			logger.debug("profiles.size()==0 at delete");
			return;
		} else if (toDelete == null) {
			logger.debug("toDelete==null at delete");
			return;
		}
		for (int i = 0; i < profiles.size(); i++) {
			curProfile = profiles.get(i);
			if (curProfile == toDelete) {
				profiles.remove(i);
				break;
			}
		}
		// If the deleted profile was currently active, we choose the first profile or mark "we need a new profile!"
		if (activeProfile == toDelete) {
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
	 * Marks a profile as 'active'.
	 * 
	 * @param newActive - Handle of the to-be activated profile
	 * @author SebastianN
	 */
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
			try {
				Serializer.serialize(cProfile, cProfile.getPathToProfile());
			} catch (IOException io) {
				logger.error("Not able to serialize Profiles, IOException: ");
				io.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * If available, getSerializedProfiles gets serialized profiles from file.
	 * In case all files couldn't be read, a new profile-list is allocated.
	 * 
	 * @author SebastianN
	 */
	public void getSerializedProfiles() {
		for (int i = 0; i < profilePathes.size(); i++) {
			try {
				profiles.add((Profile) Serializer.deserialize(profilePathes.get(i)));
			} catch (IOException io) {
				logger.error("Not able to deserialize Profile from file" + profilePathes.get(i));
			}
		}
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
		return activeProfile.getKbdLayout();
	}
	
	
	public boolean isAutoProfileChange() {
		return autoProfileChange;
	}
	
	
	public void setAutoProfileChange(boolean autoProfileChange) {
		this.autoProfileChange = autoProfileChange;
	}
	
	
	public void toggleAutoProfileChange() {
		if (autoProfileChange)
			autoProfileChange = false;
		else
			autoProfileChange = true;
	}
	
	
	public boolean isAutoCompleting() {
		return autoCompleting;
	}
	
	
	public void setAutoCompleting(boolean autoCompleting) {
		this.autoCompleting = autoCompleting;
	}
	
	
	public void toggleAutoCompleting() {
		if (autoCompleting)
			autoCompleting = false;
		else
			autoCompleting = true;
	}
	
	
	public boolean isTreeExpanding() {
		return treeExpanding;
	}
	
	
	public void setTreeExpanding(boolean treeExpanding) {
		this.treeExpanding = treeExpanding;
	}
	
	
	public void toggleTreeExpanding() {
		if (treeExpanding)
			treeExpanding = false;
		else
			treeExpanding = true;
	}
	
	
}
