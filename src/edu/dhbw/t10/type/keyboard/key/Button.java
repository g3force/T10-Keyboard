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

import org.apache.log4j.Logger;


/**
 * This class is the body of one Button on the keyboard. It contains information
 * about the names and keycodes for the several modes like default, shift and alt_gr
 * 
 * @author NicolaiO
 * 
 */
public class Button extends PhysicalButton {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger		logger				= Logger.getLogger(Button.class);
	private static final long			serialVersionUID	= 6949715976373962684L;
	private HashMap<ModeButton, Key>	modes					= new HashMap<ModeButton, Key>();
	private Key								key;
	private ArrayList<ModeButton>		activeModes			= new ArrayList<ModeButton>();
	

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
	 * Add a ModeButton to this Button. This saves the ModeButton in a mode-list and registers as an observer to change
	 * its mode when ModeButton is pressed.
	 * 
	 * @param mode ModeButton that this Button should be bind to
	 * @param accordingKey Key, that should be pressed, when ModeButton is active and Button is pressed
	 * @author NicolaiO
	 */
	public void addMode(ModeButton mode, Key accordingKey) {
		modes.put(mode, accordingKey);
		mode.register(this);
	}
	
	
	/**
	 * activate a mode. If the button is pressed after this, it will react according to mode preferences.
	 * If more than one mode is active afterwards, currently the Button text is set to default!
	 * 
	 * @param mode ModeButton, that should be activated
	 * @author NicolaiO
	 */
	public void addCurrentMode(ModeButton mode) {
		activeModes.add(mode);
		if (activeModes.size() == 1) {
			if (modes.get(mode) != null) {
				setText(modes.get(mode).getName());
			} else {
				logger.warn("addCurrentMode called with invalid mode!");
			}
		} else {
			// If no mode or more than one mode is active, just set ButtonText to default...
			// TODO OPTIONAL NicolaiO support multi-modes
			setText(key.getName());
		}
	}
	
	
	/**
	 * Remove a Mode again, if Mode is not longer active.
	 * 
	 * @param mode ModeButton, that should be deactivated
	 * @author NicolaiO
	 */
	public void rmCurrentMode(ModeButton mode) {
		activeModes.remove(mode);
		if (activeModes.size() == 1) {
			setText(modes.get(activeModes.get(0)).getName());
		} else {
			setText(key.getName());
		}
	}
	
	
	/**
	 * Return all keys, that are currently pressed, including mode keys!
	 * 
	 * @return
	 * @author Dirk
	 */
	public ArrayList<Key> getSingleKey() {
		ArrayList<Key> output = new ArrayList<Key>();
		if (activeModes.size() == 0) {
			output.add(key);
		} else if (activeModes.size() == 1 && modes.containsKey(activeModes.get(0))) {
			output.add(modes.get(activeModes.get(0)));
		} else {
			for (ModeButton modeKey : activeModes)
				output.add(modeKey.getModeKey());
			output.add(modes.get("default"));
		}
		return output;
	}
	
	
	/**
	 * Unset/release all currently active ModeButtons
	 * 
	 * @author NicolaiO
	 */
	public void unsetPressedModes() {
		ArrayList<ModeButton> tactiveModes = new ArrayList<ModeButton>();
		for (ModeButton b : activeModes) {
			if (b.getState() == ModeButton.PRESSED) {
				tactiveModes.add(b);
			}
		}
		for (ModeButton b : tactiveModes) {
			b.release();
		}
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	public HashMap<ModeButton, Key> getModes() {
		return modes;
	}
	
	
	public void setModes(HashMap<ModeButton, Key> modes) {
		this.modes = modes;
	}
	
	
	public ArrayList<ModeButton> getActiveModes() {
		return activeModes;
	}
	
	
	public Key getKey() {
		return key;
	}
	
	
	public void setKey(Key key) {
		this.key = key;
		setText(key.getName());
	}
	
	
}
