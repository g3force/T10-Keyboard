/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Nov 10, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;


/**
 * ModeKey represent a mode such as shift, that can be referenced to {@link ModeButton}.
 * It saves to current state, all observers (the Buttons, which have a shift mode) and all connected ModeButtons.
 * 
 * @author NicolaiO
 * 
 */
public class ModeKey extends Key {
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final Logger	logger			= Logger.getLogger(ModeKey.class);
	public static final int			DEFAULT			= 0;
	public static final int			PRESSED			= 1;
	public static final int			HOLD				= 2;
	private int							state				= DEFAULT;
	private ArrayList<Button>		observers		= new ArrayList<Button>();
	private HashSet<ModeButton>	conModeButtons	= new HashSet<ModeButton>();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Create a new ModeKey with a Key as basement.
	 * 
	 * @param key The key, that this ModeKey references to
	 * @author NicolaiO
	 */
	public ModeKey(Key key) {
		super(key.getId(), key.getName(), key.getKeycode(), key.getType(), key.isAccept(), key.getIconSrc());
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Push the occording Mode (like shift) and trigger all connected ModeButtons and normal Buttons
	 * Toggle the state ( DEFAULT => PRESSED => HOLD => DEFAULT )
	 * 
	 * @author NicolaiO
	 */
	public void push() {
		switch (state) {
			case DEFAULT:
				setState(PRESSED);
				break;
			case PRESSED:
				setState(HOLD);
				break;
			case HOLD:
				setState(DEFAULT);
				break;
		}
		logger.debug("ModeButton pressed. State is now " + state);
	}
	
	
	/**
	 * Set Mode state back to DEFAULT and also trigger all Buttons and ModeButtons
	 * 
	 * @author NicolaiO
	 */
	public void release() {
		setState(DEFAULT);
		logger.debug("ModeButton released");
	}
	
	
	public void setState(int state) {
		switch (state) {
			case DEFAULT:
				this.state = state;
				// notify all ModeButtons
				for (ModeButton mb : conModeButtons) {
					mb.getModel().setPressed(false);
					mb.setBorderPainted(true);
					mb.setFont(new Font(mb.getFont().getName(), Font.PLAIN, mb.getFont().getSize()));
					mb.setIcon(mb.getModeKey().getIcon());
					// mb.setText("");
				}
				// notify all normal Buttons
				for (Button b : observers) {
					b.rmCurrentMode(this);
				}
				break;
			case PRESSED:
				this.state = state;
				for (ModeButton mb : conModeButtons) {
					mb.getModel().setPressed(true);
					mb.setBorderPainted(true);
					mb.setFont(new Font(mb.getFont().getName(), Font.PLAIN, mb.getFont().getSize()));
					mb.setIcon(mb.getModeKey().getIcon());
					// mb.setText("");
				}
				// notify all normal Buttons
				for (Button b : observers) {
					b.addCurrentMode(this);
				}
				break;
			case HOLD:
				this.state = state;
				// notify all ModeButtons
				for (ModeButton mb : conModeButtons) {
					mb.getModel().setPressed(true);
					mb.setBorderPainted(true);
					mb.setFont(new Font(mb.getFont().getName(), Font.BOLD, mb.getFont().getSize()));
					mb.setIcon(mb.getModeKey().getIcon());
					// mb.setText(mb.getModeKey().getName());
				}
				// notify all normal Buttons
				for (Button b : observers) {
					b.addCurrentMode(this);
				}
				break;
		}
		logger.debug("State set to " + state);
	}

	
	/**
	 * Register a Button as an observer, so that the Button can be informed of a status change
	 * 
	 * @param b Button to be registered
	 * @author NicolaiO
	 */
	public void register(Button b) {
		observers.add(b);
	}
	
	
	/**
	 * Unregister the Button (for more info, have a look at {@link register(Button b)}
	 * 
	 * @param b Button to unregister
	 * @author NicolaiO
	 */
	public void unregister(Button b) {
		observers.remove(b);
	}
	
	
	/**
	 * Add a ModeButton, that references this ModeKey
	 * 
	 * @param b ModeButton
	 * @author NicolaiO
	 */
	public void addModeButton(ModeButton b) {
		conModeButtons.add(b);
	}
	
	
	/**
	 * Remove a ModeButton, that references this ModeKey
	 * 
	 * @param b ModeButton
	 * @author NicolaiO
	 */
	public void removeModeButton(ModeButton b) {
		conModeButtons.remove(b);
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Return the current State of this ModeKey
	 * 
	 * @return current state
	 * @author NicolaiO
	 */
	public int getState() {
		return state;
	}
}
