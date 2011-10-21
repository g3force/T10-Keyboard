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
import java.awt.Insets;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;


/**
 * This class is the body of one key on the keyboard. It contains information
 * about size and position as well as for the names and keycodes for the several
 * modes like normal, shift and alt_gr
 * 
 * @author NicolaiO
 * 
 */
public class Key extends JButton {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long		serialVersionUID	= 6949715976373962684L;
	private HashMap<String, Mode>	modes					= new HashMap<String, Mode>();
	private Dimension					size					= new Dimension(10, 10);
	private int							pos_x					= 0;
	private int							pos_y					= 0;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Create a new Key file with given size and position
	 * @param size_x Size of the key button
	 * @param size_y Size of the key button
	 * @param pos_x Position of the key button
	 * @param pos_y Position of the key button
	 */
	public Key(int size_x, int size_y, int pos_x, int pos_y) {
		this.size = new Dimension(size_x, size_y);
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		setLayout(null);
		setMargin(new Insets(1, 1, 1, 1));
		setBounds(getPos_x(), getPos_y(), getSize().width, getSize().height);
	}
	
	
	/**
	 * This constructor is only for compatibility and to avoid nullPointerExceptions...
	 */
	public Key() {
		setLayout(null);
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public void addMode(String mode, String name, String keycode) {
		modes.put(mode, new Mode(name, keycode));
	}
	
	
	public String toString() {
		return "Size: " + size + " Pos: " + pos_x + "," + pos_y + " Modes: " + modes;
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public String getName(String mode) {
		if (modes.containsKey(mode)) {
			return modes.get(mode).getName();
		} else {
			return "";
		}
	}


	public Set<String> getAllModes() {
		return modes.keySet();
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
	
	
	public HashMap<String, Mode> getModes() {
		return modes;
	}
	
	
	public void setModes(HashMap<String, Mode> modes) {
		this.modes = modes;
	}
	
	private class Mode {
		private String	name		= "";
		private String	keycode	= "";
		
		
		public Mode(String _name, String _keycode) {
			name = _name;
			keycode = _keycode;
		}
		
		
		public String toString() {
			return "(" + name + " " + keycode + ")";
		}
		

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}
		
		
		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}
		
		
		@SuppressWarnings("unused")
		public String getKeycode() {
			return keycode;
		}
		
		
		@SuppressWarnings("unused")
		public void setKeycode(String keycode) {
			this.keycode = keycode;
		}
	}
}
