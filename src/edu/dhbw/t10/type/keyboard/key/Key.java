/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 27, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

/**
 * A key represents a combination of a keycode and a name together with some meta information.
 * e.g. 'a' is a key, but the a-Button on the keyboard is a Button, because it also includes an 'A'
 * 
 * A key can have a type and it can be an accept key (for accepting the currently suggested word)
 * 
 * @author NicolaiO
 * 
 */
public class Key {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	public static final int	UNKNOWN	= 0;
	public static final int	CONTROL	= 1;
	public static final int	UNICODE	= 2;
	public static final int	CHAR		= 3;
	
	private int					id			= 0;
	private String				keycode	= "";
	private String				name		= "";
	private int					type		= 0;
	private boolean			accept	= false;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Create new Key.
	 * 
	 * @param id unique identification (mostly for keymap file)
	 * @param name visible name on keyboard
	 * @param keycode code for internal usage (from keymap)
	 * @param type one of UNKNOWN, CONTROL, UNICODE, CHAR
	 * @param accept is this key an accept key? (save word after entering key)
	 * @author NicolaiO
	 */
	public Key(int id, String name, String keycode, int type, boolean accept) {
		this.id = id;
		this.name = name;
		this.keycode = keycode;
		this.type = type;
		this.accept = accept;
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	

	public Key clone() {
		Key nk = new Key(id, name, keycode, type, accept);
		return nk;
	}


	public String toString() {
		return "id:" + id + " n:" + name + " kc:" + keycode + " t:" + type;
	}
	

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
	
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public int getType() {
		return type;
	}
	
	
	public void setType(int type) {
		this.type = type;
	}
	
	
	public boolean isAccept() {
		return accept;
	}
	
	
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
	

}
