/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * This class is the body of one key on the keyboard. It contains information
 * about size and position as well as for the names and keycodes for the several
 * modes like default, shift and alt_gr
 * 
 * @author NicolaiO
 * 
 */
public class ButtonKey extends PhysicalKey {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long		serialVersionUID	= 6949715976373962684L;
	

	private HashMap<ModeKey, SingleKey>	modes					= new HashMap<ModeKey, SingleKey>();
	// assigns to every modi
	private boolean							accept				= false;
	private ArrayList<ModeKey>				activeModes			= new ArrayList<ModeKey>();
	
	
	// contains the activated control key like shift


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ButtonKey(int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param mode
	 * @param name
	 * @param keycode
	 * @param color
	 * @author NicolaiO
	 */
	// TODO INTERFACE for keyboardlayoutloader
	public void addMode(ModeKey mode, SingleKey accordingKey) {
		modes.put(mode, accordingKey);
	}
	
	
	// public String toString() {
	// return "Size: " + origSize + " Pos: " + pos_x + "," + pos_y + " Modes: " + modes;
	// }
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param currentMode
	 * @author NicolaiO
	 */
	public void addCurrentMode(ModeKey mode) {
		activeModes.add(mode);
		setText(modes.get(mode).getName());
		// setBackground(getColorFromString(getColor()));
	}
	
	
	
	

	public ArrayList<SingleKey> getKeycode() {
		ArrayList<SingleKey> output = new ArrayList<SingleKey>();
		if (activeModes.size() == 0) {
			output.add(modes.get("default"));
		} else if (activeModes.size() == 1) {
			output.add(modes.get(activeModes.get(0)));
		} else {
			for (ModeKey modeKey : activeModes)
				output.add(modeKey.getModeKey());
			output.add(modes.get("default"));
		}
		return output;
	}
	
	
	public boolean isAccept() {
		return accept;
	}



	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	
	// public Set<String> getAllModes() {
	// return modes.keySet();
	// }
	
	


	
	
	public HashMap<ModeKey, SingleKey> getModes() {
		return modes;
	}
	
	
	public void setModes(HashMap<ModeKey, SingleKey> modes) {
		this.modes = modes;
	}
	
	
	public ArrayList<ModeKey> getActiveMode() {
		return activeModes;
	}

	
	public int getType() {
		if (activeModes.size() == 0) {
			return modes.get("default").getType();
		} else {
			return modes.get(activeModes.get(0)).getType();
			// TODO returns the fist activeMode, insert priority?
		}
	}
}
