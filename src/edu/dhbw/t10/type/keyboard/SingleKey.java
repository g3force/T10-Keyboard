/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 27, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard;

/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class SingleKey {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	public static final int	UNKNOWN_KEY	= 0;
	public static final int	CONTROL_KEY	= 1;
	public static final int	CHAR_KEY		= 2;
	public static final int	UNICODE_KEY	= 3;
	public static final int	MUTE_KEY		= 4;
	private int					id				= 0;
	private String				keycode		= "";
	private int					type			= UNKNOWN_KEY;
	private String				name			= "";
	// the name appearing on the button in the screen keyboard
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public SingleKey(int id, String name, String keycode, int type) {
		this.id = id;
		this.name = name;
		this.keycode = keycode;
		this.type = type;
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getKeycode() {
		return keycode;
	}
	
	
	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}
	
	
	public int getType() {
		return type;
	}
	
	
	public void setType(int type) {
		this.type = type;
	}
	
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}


}
