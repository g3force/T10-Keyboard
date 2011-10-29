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
public class Button extends PhysicalButton {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long			serialVersionUID	= 6949715976373962684L;

	
	private HashMap<ModeButton, Key>	modes					= new HashMap<ModeButton, Key>();
	private Key								key;
	// assigns to every modi
	private ArrayList<ModeButton>		activeModes			= new ArrayList<ModeButton>();
	
	
	// contains the activated control key like shift


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public Button(int size_x, int size_y, int pos_x, int pos_y) {
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
	public void addMode(ModeButton mode, Key accordingKey) {
		modes.put(mode, accordingKey);
	}
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param currentMode
	 * @author NicolaiO
	 */
	public void addCurrentMode(ModeButton mode) {
		activeModes.add(mode);
		setText(modes.get(mode).getName());
		// setBackground(getColorFromString(getColor()));
	}
	
	
	public ArrayList<Key> getSingleKey() {
		ArrayList<Key> output = new ArrayList<Key>();
		if (activeModes.size() == 0) {
			output.add(modes.get("default"));
		} else if (activeModes.size() == 1 && modes.containsKey(activeModes.get(0))) {
			output.add(modes.get(activeModes.get(0)));
		} else {
			for (ModeButton modeKey : activeModes)
				output.add(modeKey.getModeKey());
			output.add(modes.get("default"));
		}
		return output;
	}
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// public Set<String> getAllModes() {
	// return modes.keySet();
	// }
	
	
	public HashMap<ModeButton, Key> getModes() {
		return modes;
	}
	
	
	public void setModes(HashMap<ModeButton, Key> modes) {
		this.modes = modes;
	}
	
	
	public ArrayList<ModeButton> getActiveMode() {
		return activeModes;
	}
	
	
	public Key getKey() {
		return key;
	}
	
	
	public void setKey(Key key) {
		this.key = key;
	}


}
