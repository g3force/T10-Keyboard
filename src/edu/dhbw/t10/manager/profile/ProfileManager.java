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

import edu.dhbw.t10.manager.Controller;
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
	 * Reads the config-file with all entrys and assigns
	 * the read values.
	 * 
	 * @author SebastianN
	 */
	private void readConfig() {
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
	private void addEntry(BufferedWriter bw, String entry) {
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
	 * @author SebastianN, NicolaiO
	 */
	public Profile createProfile(String profileName) {
		Profile newProfile = getProfileByName(profileName);
		
		if (newProfile != null) {
			logger.warn("Profile already exists.");
		} else {
			newProfile = new Profile(profileName);
			profiles.add(newProfile);
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
	public void deleteProfile(Profile profile) {
		if (profiles.size() <= 1) {
			logger.debug("Only one or zero profiles left. Can't delete.");
			return;
		}
		profiles.remove(profile);
		deleteFile(profile.getPathToAllowedChars());
		deleteFile(profile.getPathToLayoutFile());
		deleteFile(profile.getPathToProfile());
		deleteFile(profile.getPathToTree());
		File profileDir = new File(profile.getPathToProfile());
		if (profileDir.getParentFile().isDirectory()) {
			deleteFile(profileDir.getParent());
		}
		getActive().loadDDLs(profiles);
	}
	
	
	/**
	 * Delete the given file and log an error, if failed.
	 * 
	 * @param path to file
	 * @author NicolaiO
	 */
	private void deleteFile(String path) {
		File f;
		f = new File(path);
		if (!f.delete())
			logger.error(path + " could not be deleted.");
	}

	
	/**
	 * Marks a profile as 'active'.
	 * 
	 * @param newActive - Handle of the to-be activated profile
	 * @author SebastianN, NicolaiO
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
		activeProfile.loadDDLs(profiles);
		logger.info("Profile now active: " + getActive());
	}
	
	
	/**
	 * Serializing Profile-Arraylist.
	 * Consider to only save current profile, which will automatically serialize the profile
	 * 
	 * @author SebastianN
	 */
	@SuppressWarnings("unused")
	private void serializeProfiles() {
		for (int i = 0; i < profiles.size(); i++) {
			Profile cProfile = profiles.get(i);
			if (cProfile.getPathToProfile() == null || cProfile.getPathToProfile().isEmpty())
				continue;
			logger.info("Serializing Profile " + cProfile.getName() + " to " + cProfile.getPathToProfile());
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
	private void loadSerializedProfiles() {
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
}
