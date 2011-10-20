/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type;

import java.awt.Dimension;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class Key {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private String		name		= "";
	private int			id			= 0;
	private String		keycode	= "";
	private Dimension	size		= new Dimension(0, 0);
	private int			pos_x		= 0;
	private int			pos_y		= 0;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public Key(int id, String name, String keycode, Dimension size, int pos_x, int pos_y) {
		this.name = name;
		this.id = id;
		this.keycode = keycode;
		this.size = size;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
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
	
	
	public Dimension getSize() {
		return size;
	}
	
	
	public void setSize(Dimension size) {
		this.size = size;
	}
	
	
	public int getPos_x() {
		return pos_x;
	}
	
	
	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}
	
	
	public int getPos_y() {
		return pos_y;
	}
	
	
	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}
}
