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
	private static ProfileManager	instance;
	private ArrayList<Profile>		profiles;
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
	

	public Profile create(String profileName) {
		Profile newProfile = new Profile();
		newProfile.setName(profileName);
		newProfile.setID(profiles.size());
		profiles.add(newProfile);
		if (activeProfile == null)
			activeProfile = newProfile;
		return newProfile;
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
		if (activeProfile.getID() == id) {
			if (profiles.size() > 0)
				activeProfile = profiles.get(0);
			else
				activeProfile = null;
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
				activeProfile = curProfile;
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
		if (treeExpanding)
			getActive().getTree().insert(word);
	}

	
	/**
	 * 
	 * Serializing Profile-Arraylist
	 * 
	 * @author hpadmin
	 */
	public void serializeProfiles() {
		Serializer.serialize(profiles, "./conf/profiles");
	}
	
	
	public void getSerializedProfiles() {
		profiles = Serializer.deserialize("./conf/profiles");
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
