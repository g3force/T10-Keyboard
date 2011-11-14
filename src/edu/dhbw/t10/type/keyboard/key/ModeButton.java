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

import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;


/**
 * button for the different modes like shift, strg pressed
 * 
 * @author DirkK, NicolaiO
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
	
	/**
	 * Create a new ModeButton with given ModeKey and bounds
	 * 
	 * @param modeKey
	 * @param size_x
	 * @param size_y
	 * @param pos_x
	 * @param pos_y
	 * @author NicolaiO
	 */
	public ModeButton(ModeKey modeKey, int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		setModeKey(modeKey);
		modeKey.addModeButton(this);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Push the button. The event will be transmitted to the occording ModeKey and thus to all other ModeButtons with
	 * same ModeKey (e.g. to all Shift Buttons)
	 * 
	 * @author NicolaiO
	 */
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
		if (!modeKey.getIcon().equals("")) {
			URL iconUrl;
			iconUrl = getClass().getResource(modeKey.getIcon());
			setIcon(new ImageIcon(iconUrl));
		} else
			setText(modeKey.getName());
	}
	
	
	public int getState() {
		return modeKey.getState();
	}
}
