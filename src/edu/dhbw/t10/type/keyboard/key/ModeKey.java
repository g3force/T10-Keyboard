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

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
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
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param id
	 * @param name
	 * @param keycode
	 * @param type
	 * @param accept
	 * @author NicolaiO
	 */
	public ModeKey(int id, String name, String keycode, int type, boolean accept) {
		super(id, name, keycode, type, accept);
	}
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param key
	 * @author NicolaiO
	 */
	public ModeKey(Key key) {
		super(key.getId(), key.getName(), key.getKeycode(), key.getType(), key.isAccept());
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public void push() {
		if (state == HOLD) {
			release();
		} else if (state == PRESSED) {
			for (ModeButton mb : conModeButtons) {
				state = HOLD;
				mb.getModel().setPressed(true);
				mb.setBorderPainted(false);
			}
		} else if (state == DEFAULT) {
			for (ModeButton mb : conModeButtons) {
				state = PRESSED;
				mb.getModel().setPressed(true);
				for (Button b : observers) {
					b.addCurrentMode(this);
				}
			}
		}
		logger.debug("ModeButton pressed. State is now " + state);
	}
	
	
	public void release() {
		state = DEFAULT;
		for (Button b : observers) {
			b.rmCurrentMode(this);
		}
		for (ModeButton mb : conModeButtons) {
			mb.getModel().setPressed(false);
			mb.setBorderPainted(true);
		}
		logger.debug("ModeButton released");
	}
	
	
	public void register(Button b) {
		observers.add(b);
	}
	
	
	public void unregister(Button b) {
		observers.remove(b);
	}
	
	
	public void addModeButton(ModeButton b) {
		conModeButtons.add(b);
	}
	
	
	public void removeModeButton(ModeButton b) {
		conModeButtons.remove(b);
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	public int getState() {
		return state;
	}
}
