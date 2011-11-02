/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 28, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

import java.util.ArrayList;

import org.apache.log4j.Logger;


/**
 * button for the different modes like shift, strg pressed
 * 
 * @author DirkK
 * 
 */
public class ModeButton extends PhysicalButton {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger				= Logger.getLogger(ModeButton.class);
	public static final int			DEFAULT				= 0;
	public static final int			PRESSED				= 1;
	public static final int			HOLD					= 2;
	private static final long		serialVersionUID	= 5356736981172867044L;
	private Key							modeKey;
	private ArrayList<Button>		observers			= new ArrayList<Button>();
	private int							state					= DEFAULT;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ModeButton(Key modeKey, int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		this.modeKey = modeKey;
		setText(modeKey.getName());
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public void push() {
		if (state == HOLD) {
			release();
		} else if (state == PRESSED) {
			state = HOLD;
		} else if (state == DEFAULT) {
			state = PRESSED;
			for (Button b : observers) {
				b.addCurrentMode(this);
			}
		}
		logger.debug("ModeButton pressed. State is now " + state);
	}
	
	
	public void release() {
		state = DEFAULT;
		for (Button b : observers) {
			b.rmCurrentMode(this);
		}
		logger.debug("ModeButton released");
	}


	public void register(Button b) {
		observers.add(b);

	}
	
	
	public void unregister(Button b) {
		observers.remove(b);
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public Key getModeKey() {
		return modeKey;
	}
	
	
	public void setModeKey(Key modeKey) {
		this.modeKey = modeKey;
		setText(modeKey.getName());
	}
	
	
	public int getState() {
		return state;
	}
	
	
	public void setState(int state) {
		this.state = state;
	}
}
