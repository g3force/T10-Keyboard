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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.profile.Profile;


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
	private static final Logger	logger					= Logger.getLogger(ProfileManager.class);
	private static final String	configFile				= "data/config";
	private ArrayList<Profile>		profiles					= new ArrayList<Profile>();
	private ArrayList<String>		profilePathes			= new ArrayList<String>();
	private Profile					activeProfile;
	private String						defaultActiveProfile	= "default";

	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ProfileManager() {
		logger.debug("initializing...");
		readConfig(); // fills activeProfileName and profilePathes with the data from the config file
		logger.debug("configfile: activeProfileName=" + defaultActiveProfile + " profiles=" + profilePathes.size());
		getSerializedProfiles(); // deserializes all profiles, fills profiles
		// if no profiles were loaded, create a new one
		if (profiles.size() == 0) {
			logger.debug("No profiles loaded. New profile will be created.");
			createProfile(defaultActiveProfile);
		}
		// set active profile by defauleActiveProfile which was either loaded from config file or is set to a default
		// value
		activeProfile = getProfileByName(defaultActiveProfile);
		// if the defaultActiveProfile in the config file references a non existent profile, create a new profile with the
		// given name
		if (activeProfile == null) {
			activeProfile = createProfile(defaultActiveProfile);
		}
		// now, active profile is hopefully set to any profile...
		activeProfile.load();
		logger.debug("initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * Adds <b>ALL</b> profiles to the Profile-DropdownList
	 * 
	 * @author SebastianN
	 */
	public void addAllProfilesToDDL() {
		ArrayList<DropDownList> DDLs = getActive().getKbdLayout().getDdls();
		for (int i = 0; i < DDLs.size(); i++) {
			if (DDLs.get(i).getType() == DropDownList.PROFILE) {
				for (int j = 0; j < profiles.size(); j++) {
					DDLs.get(i).addItem(profiles.get(j).getName());
					DDLs.get(i).revalidate();
				}
			}
		}
	}
	

	/**
	 * 
	 * Adds <b>ONE</b> profile to the dropdown list.
	 * 
	 * @param handle
	 * @author SebastianN
	 */
	
	public void addProfileToDDL(Profile handle) {
		ArrayList<DropDownList> DDLs = getActive().getKbdLayout().getDdls();
		for (int i = 0; i < DDLs.size(); i++) {
			if (DDLs.get(i).getType() == DropDownList.PROFILE) {
				DDLs.get(i).addItem(handle.getName());
				DDLs.get(i).revalidate();
			}
		}
	}
	

	/**
	 * 
	 * Removes a certain profile from the DropdownList
	 * 
	 * @param name of the to-be deleted profile.
	 * @author SebastianN
	 */
	private void removeProfileFromDDL(String name) {
		ArrayList<DropDownList> DDLs = getActive().getKbdLayout().getDdls();
		for (int i = 0; i < DDLs.size(); i++) {
			if (DDLs.get(i).getType() == DropDownList.PROFILE) {
				DDLs.get(i).removeAllItems();
				DDLs.get(i).revalidate();
				System.out.println(":D");
				for (int j = 0; j < profiles.size(); j++) {
					logger.debug("Profile re-added: " + profiles.get(j).getName());
					DDLs.get(i).addItem(profiles.get(j).getName());
					DDLs.get(i).revalidate();
					DDLs.get(i).repaint();
				}
			}
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
			File confFile = new File(configFile);
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
						if (valName.toLowerCase().equals("profilepath")) {
							profilePathes.add(value);
						} else if (valName.toLowerCase().equals("activeprofile")) {
							defaultActiveProfile = value;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						logger.error("Exception in readConfig().assignValues: " + ex.toString());
					}
				}
				br.close();
			} else {
				logger.error("Config file could not be found.");
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
	 * Add an entry to the config file.
	 * 
	 * @param bw - Handle/Reference to a BufferedWriter
	 * @param entry - String containing what you want to write.
	 * @author SebastianN
	 */
	public void addEntry(BufferedWriter bw, String entry) {
		try {
			bw.write(entry + "\n");
		} catch (IOException io) {
			io.printStackTrace();
		}
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
			
			addEntry(bw, createComment("Configfile for T10"));
			

			if (activeProfile != null)
				addEntry(bw, "ActiveProfile=" + activeProfile.getName());
			
			for (int i = 0; i < profiles.size(); i++) {
				if (profiles.get(i).getPathToProfile().isEmpty()) {
					logger.error("Profile " + profiles.get(i).getName() + " has no path to profile");
					continue;
				}
				addEntry(bw, "ProfilePath=" + profiles.get(i).getPathToProfile());
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
	
	public Profile createProfile(String profileName) {
		if (getProfileByName(profileName) != null) {
			logger.error("Profile already exists.");
			return null;
		}
		Profile newProfile = new Profile(profileName);
		profiles.add(newProfile);
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
		logger.error("Profile \"" + name + "\" not found.");
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
	public void deleteProfile(String toDel) {
		Profile toDelete = getProfileByName(toDel);
		if (profiles.size() <= 0) {
			logger.debug("profiles.size()==0 at delete");
			return;
		} else if (toDelete == null) {
			logger.debug("toDelete==null at delete");
			return;
		} else if (toDelete == activeProfile) {
			logger.debug("toDelete = activeProfile");
			return;
		}
		Profile curProfile = null;
		for (int i = 0; i < profiles.size(); i++) {
			curProfile = profiles.get(i);
			if (curProfile == toDelete) {
				logger.debug("Delete profile: " + toDelete.getName());
				profiles.remove(i);
				removeProfileFromDDL(toDelete.getName());
				break;
			}
		}
	}
	
	
	/**
	 * 
	 * Gets the Position of the wanted profile within the Profile-Arraylist
	 * 
	 * @param name of the profile whose position needs to be verified.
	 * @return Position within array (int). If not found, it returns -1
	 * @author SebastianN
	 */
	private int getPositionOfProfile(String name) {
		for (int i = 0; i < profiles.size(); i++) {
			if (profiles.get(i).equals(name))
				return i;
		}
		return -1;
	}
	

	/**
	 * Marks a profile as 'active'.
	 * 
	 * @param newActive - Handle of the to-be activated profile
	 * @author SebastianN
	 */
	public void setActive(Profile newActive) {
		if (newActive == activeProfile) {
			return;
		}
		if (activeProfile != null) {
			activeProfile.save();
		}
		ArrayList<DropDownList> DDLs = getActive().getKbdLayout().getDdls();
		for (int i = 0; i < DDLs.size(); i++) {
			if (DDLs.get(i).getType() == DropDownList.PROFILE) {
				DDLs.get(i).setSelectedItem(this.getPositionOfProfile(newActive.getName()));
			}
		}
		activeProfile = newActive;
		activeProfile.load();
	}
	
	
	/**
	 * Controller requests a Word suggestion with an given Startstring.
	 * 
	 * @param givenChars
	 * @return
	 * @author DirkK
	 */
	public String getWordSuggest(String givenChars) {
		if (activeProfile.isAutoCompleting()) {
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
	 * Gives a word which have to be inserted or updated in the data.
	 * 
	 * @param word
	 * @author SebastianN
	 */
	public void acceptWord(String word) {
		if (getActive() == null) {
			logger.error("getActive()==NULL at acceptWord");
			return;
		}
		if (activeProfile.isTreeExpanding())
			getActive().getTree().insert(word);
	}
	
	
	/**
	 * Serializing Profile-Arraylist
	 * 
	 * @author SebastianN
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
		int counter = 0;
		if (profiles == null) {
			profiles = new ArrayList<Profile>();
		}
		for (int i = 0; i < profilePathes.size(); i++) {
			try {
				Profile dProf = (Profile) Serializer.deserialize(profilePathes.get(i));
				profiles.add(dProf);
				counter++;
			} catch (IOException io) {
				logger.error("Not able to deserialize Profile from file " + profilePathes.get(i));
			}
		}
		logger.info("Deserialized " + counter + " profiles.");
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
	

	
	
}
