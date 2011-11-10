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
	@SuppressWarnings("unused")
	private static final Logger	logger				= Logger.getLogger(ModeButton.class);
	private static final long		serialVersionUID	= 5356736981172867044L;
	private ModeKey					modeKey;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ModeButton(ModeKey modeKey, int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		this.modeKey = modeKey;
		setText(modeKey.getName());
		modeKey.addModeButton(this);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public void push() {
		modeKey.push();
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public String getModeName() {
		return modeKey.getName();
	}


	public Key getModeKey() {
		return modeKey;
	}
	
	
	public void setModeKey(ModeKey modeKey) {
		this.modeKey = modeKey;
		setText(modeKey.getName());
	}
	
	
	public int getState() {
		return modeKey.getState();
	}
}
