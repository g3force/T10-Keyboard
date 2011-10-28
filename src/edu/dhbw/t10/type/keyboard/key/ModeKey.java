/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 28, 2011
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

/**
 * TODO dirk, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author dirk
 * 
 */
public class ModeKey extends PhysicalKey {
	/**  */
	private static final long	serialVersionUID	= 5356736981172867044L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	SingleKey modeKey;
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ModeKey(SingleKey modeKey, int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		this.modeKey = modeKey;
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public SingleKey getModeKey() {
		return modeKey;
	}
	public void setModeKey(SingleKey modeKey) {
		this.modeKey = modeKey;
	}
}
	
