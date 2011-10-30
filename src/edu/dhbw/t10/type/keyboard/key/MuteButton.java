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
 * button for the mute options
 * @author dirk
 * 
 */
public class MuteButton extends PhysicalButton {
	/**  */
	private static final long	serialVersionUID		= -4124533718708150504L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private int						type;
	public static final int		AUTO_PROFILE_CHANGE	= 0;
	public static final int		AUTO_COMPLETING		= 1;
	public static final int		TREE_EXPANDING			= 2;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public MuteButton(int type, int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		this.type = type;
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public int getType() {
		return type;
	}
	
	
	public void setType(int type) {
		this.type = type;
	}
	
}
