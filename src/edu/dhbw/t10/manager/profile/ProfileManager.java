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
import java.util.ArrayList;

import edu.dhbw.t10.manager.KeyboardLayoutGenerator;
import edu.dhbw.t10.type.KeyboardLayout;
import edu.dhbw.t10.type.Profile;
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
	private static ProfileManager	instance;
	private ArrayList<Profile>		profiles;
	private int							activeProfile;
	private KeyboardLayout			kbdLayout;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	// Singleton
	/**
	 * Constructor as Singleton. This way, we prevent having multiple manager and
	 */
	private ProfileManager() {
		// ....
		profiles = new ArrayList<Profile>();
		activeProfile = -1; // No profile.
		instance = this;
		KeyboardLayoutGenerator lfm = new KeyboardLayoutGenerator();
		kbdLayout = lfm.getKbdLayout();
		// profiles = loadProfiles();
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


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public Profile getActive() {
		return profiles.get(activeProfile);
	}
	
	
	public int create(String profileName) {
		Profile newProfile = new Profile();
		newProfile.setName(profileName);
		newProfile.setID(profiles.size());
		profiles.add(newProfile);
		return newProfile.getID();
	}
	
	
	/**
	 * 
	 * Re-arranges the IDs of the profiles after a certain profile gets deleted.
	 * 
	 */
	private void arrangeProfiles() {
		if (profiles.size() <= 0)
			return;
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
		if (profiles.size() <= 0)
			return;
		for (int i = 0; i < profiles.size(); i++) {
			curProfile = profiles.get(i);
			if (curProfile.getID() == id) {
				profiles.remove(i);
				arrangeProfiles();
				break;
			}
		}
		// If the deleted profile was currently active, we choose the first profile or mark "we need a new profile!"
		if (activeProfile == id) {
			if (profiles.size() > 0)
				activeProfile = 0;
			else
				activeProfile = -1;
		}
	}
	
	
	/**
	 * 
	 * Marks a profile (depending on the ID) as "active". If not found, nothing happens.
	 * 
	 * @param id
	 */
	public void setActive(int id) {
		Profile curProfile = null;
		for (int i = 0; i < profiles.size(); i++) {
			curProfile = profiles.get(i);
			if (curProfile.getID() == id) {
				activeProfile = id;
				break;
			}
		}
	}
	
	
	public ArrayList<Profile> getProfiles() {
		return profiles;
	}
	
	
	public int getActiveProfile() {
		return activeProfile;
	}
	
	
	public KeyboardLayout getKbdLayout() {
		return kbdLayout;
	}
}
