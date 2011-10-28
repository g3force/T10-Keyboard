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
public class MuteKey extends PhysicalKey {
	/**  */
	private static final long	serialVersionUID	= -4124533718708150504L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	SingleKey	muteKey;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public MuteKey(SingleKey muteKey, int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		this.muteKey = muteKey;
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------

	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public SingleKey getMuteKey() {
		return muteKey;
	}
	
	
	public void setMuteKey(SingleKey muteKey) {
		this.muteKey = muteKey;
	}

}
