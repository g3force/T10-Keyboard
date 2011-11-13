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

import edu.dhbw.t10.helper.StringHelper;
import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.profile.Profile;


/**
 * The profile-manager handles all profiles, including the path to its profile-file.
 * 
 * @author SebastianN
 */
public class ProfileManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger					= Logger.getLogger(ProfileManager.class);
	private String						configFile				= "t10keyboard.conf";
	private ArrayList<Profile>		profiles					= new ArrayList<Profile>();
	private ArrayList<String>		profilePathes			= new ArrayList<String>();
	private Profile					activeProfile;
	private String						defaultActiveProfile	= "default";
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * Constructor of the ProfileManager. <br/>
	 * Reads the config file, deserializes all profile based on read config file
	 * and marks one profile as active. If no profile was found, a default-profile will be created.
	 * 
	 * @author SebastianN, NicolaiO
	 */
	public ProfileManager() {
		Profile newActiveProfile;
		logger.debug("initializing...");
		readConfig(); // fills activeProfileName and profilePathes with the data from the config file
		logger.debug("configfile: activeProfileName=" + defaultActiveProfile + " profiles=" + profilePathes.size());
		loadSerializedProfiles(); // deserializes all profiles, fills profiles
		// if no profiles were loaded, create a new one
		if (profiles.size() == 0) {
			logger.debug("No profiles loaded. New profile will be created.");
			createProfile(defaultActiveProfile);
		}
		// set active profile by defauleActiveProfile which was either loaded from config file or is set to a default
		// value
		newActiveProfile = getProfileByName(defaultActiveProfile);
		// if the defaultActiveProfile in the config file references a non existent profile, create a new profile with the
		// given name
		if (newActiveProfile == null) {
			newActiveProfile = createProfile(defaultActiveProfile);
		}
		// now, active profile is hopefully set to any profile...
		setActive(newActiveProfile);
		logger.debug("initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------

	/**
	 * Load the lists of all ddls. (currently only one exists)
	 * Existing items will be removed!
	 * 
	 * @author NicolaiO
	 */
	public void loadDDLs() {
		ArrayList<DropDownList> DDLs = getActive().getKbdLayout().getDdls();
		for (DropDownList ddl : DDLs) {
			switch (ddl.getType()) {
				case DropDownList.PROFILE:
					ddl.removeAllItems();
					for (Profile p : profiles) {
						ddl.addItem(p);
					}
					logger.debug("loaded " + ddl.getItemCount() + " items in profile-ddl");
					ddl.setSelectedItem(getActive());
					logger.debug("Selected item is: " + getActive());
					ddl.revalidate();
					break;
				default:
					logger.warn("UNKOWN DDL found!");
			}
		}
	}
	
	
	/**
	 * Adds <b>ONE</b> profile to the dropdown list.
	 * 
	 * @param handle
	 * @author SebastianN
	 */
	@Deprecated
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
	 * Removes a certain profile from the DropdownList
	 * 
	 * @param name of the to-be deleted profile.
	 * @author SebastianN
	 */
	@Deprecated
	public void removeProfileFromDDL(String name) {
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
	 * Reads the config-file with all entrys and assigns
	 * the read values.
	 * 
	 * @author SebastianN
	 */
	public void readConfig() {
		try {
			File confFile = new File(Controller.getInstance().getDatapath() + "/" + configFile);
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
							if (value.isEmpty())
								continue;
							profilePathes.add(value);
						} else if (valName.toLowerCase().equals("activeprofile")) {
							if (value.isEmpty())
								continue;
							defaultActiveProfile = value;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						logger.error("Exception in readConfig().assignValues: " + ex.toString());
					}
				}
				br.close();
			} else {
				logger.debug("Config file could not be found. Doesn't matter, though.");
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
	 * Saves the name of the active profile and the path to all profile-files.
	 * 
	 * @author SebastianN
	 */
	public void saveConfig() {
		try {
			File confFile = new File(Controller.getInstance().getDatapath() + "/" + configFile);
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
			logger.info("Config file saved");
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
	 * Create a new profile
	 * 
	 * @param profileName - String. Name of the profile.
	 * @param pathToNewProfile - String. Path to the new profile.
	 * @return Handle/Pointer to the new profile.
	 * @author SebastianN
	 */
	public Profile createProfile(String profileName) {
		Profile newProfile;
		
		newProfile = getProfileByName(profileName);
		if (newProfile != null) {
			logger.warn("Profile already exists.");
		} else {
			newProfile = new Profile(profileName);
			profiles.add(newProfile);
			if (getActive() == null) {
				logger.error("The famous case, that should never occur, just did exactly this :D");
			} else {
				// loadDDLs();
			}
		}
		serializeProfiles();
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
	public void deleteProfile(Profile profile) {
		// Profile toDelete = getProfileByName(toDel);
		if (profiles.size() <= 1) {
			logger.debug("Only one or zero profiles left. Can't delete.");
			return;
		}
		profiles.remove(profile);
		// for (int i = 0; i < profiles.size(); i++) {
		// curProfile = profiles.get(i);
		// if (curProfile == toDelete) {
		// logger.debug("Delete profile: " + toDelete.getName());
		// profiles.remove(i);
		// loadDDLs();
		// break;
		// }
		// }
	}
	
	
	/**
	 * 
	 * Gets the Position of the wanted profile within the Profile-Arraylist
	 * 
	 * @param name of the profile whose position needs to be verified.
	 * @return Position within array (int). If not found, it returns 0 (so the first position is chosen)
	 * @author SebastianN
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private int getPositionOfProfile(String name) {
		for (int i = 0; i < profiles.size(); i++) {
			if (profiles.get(i).equals(name))
				return i;
		}
		// do not return -1, but 0, because otherwise errors might occur in calling method
		return 0;
	}
	
	
	/**
	 * Marks a profile as 'active'.
	 * 
	 * @param newActive - Handle of the to-be activated profile
	 * @author SebastianN
	 */
	public void setActive(Profile newActive) {
		// do nothing, if profile is already active
		if (newActive == activeProfile) {
			return;
		}
		
		logger.info("Setting profile " + newActive + " active.");

		// save currently active profile
		if (activeProfile != null) {
			activeProfile.save();
		}
		// set and load new active profile
		activeProfile = newActive;
		activeProfile.load();
		logger.info("Profile now active: " + getActive());
		loadDDLs();
	}
	
	
	/**
	 * Controller requests a Word suggestion with an given Startstring.
	 * 
	 * @param givenChars
	 * @return wordsuggest
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
	 * @param word A complete word to be inserted into tree
	 * @author SebastianN
	 */
	public boolean acceptWord(String word) {
		word = StringHelper.removePunctuation(word);
		if (getActive() == null) {
			logger.error("getActive()==NULL at acceptWord");
			return false;
		}
		if (activeProfile.isTreeExpanding())
			return getActive().getTree().insert(word);
		return false;
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
				logger.error("Not able to serialize Profiles, IOException: " + io.toString());
			}
		}
	}
	
	
	/**
	 * Loads serialized profiles from file.
	 * 
	 * @author SebastianN
	 */
	public void loadSerializedProfiles() {
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
	
	
	/**
	 * Check if the given profile name exists
	 * 
	 * @param profile
	 * @return profile exists -> true ...else false
	 * @author FelixP
	 */
	public boolean existProfile(String profile) {
		Profile p = getProfileByName(profile);
		if (p == null)
			return false;
		return true;
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Return all known profiles
	 * 
	 * @return list of all profiles
	 * @author SebastianN
	 */
	public ArrayList<Profile> getProfiles() {
		return profiles;
	}
	
	
	/**
	 * Return currently active profile
	 * 
	 * @return active profile
	 * @author SebastianN
	 */
	public Profile getActive() {
		return activeProfile;
	}
	
	
	/**
	 * Return currently active keyboard layout
	 * 
	 * @return
	 * @author NicolaiO
	 */
	public KeyboardLayout getKbdLayout() {
		return activeProfile.getKbdLayout();
	}
}
