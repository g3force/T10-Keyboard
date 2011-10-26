/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.lang.reflect.Field;
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
	
	public static final int			CONTROL_KEY			= 0;
	public static final int			CHAR_KEY				= 1;
	public static final int			UNICODE_KEY			= 2;
	public static final int			MUTE_KEY				= 3;

	private HashMap<String, Mode>	modes					= new HashMap<String, Mode>();
	private Dimension					origSize				= new Dimension(10, 10);
	private int							pos_x					= 0;
	private int							pos_y					= 0;
	private String						currentMode			= "default";
	private boolean					accept				= false;
	private int							type					= 1;


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
		this.origSize = new Dimension(size_x, size_y);
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		setLayout(null);
		setMargin(new Insets(0, 0, 0, 0));
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
	
	public void addMode(String mode, String name, String keycode, String color) {
		modes.put(mode, new Mode(name, keycode, color));
	}
	
	
	public String toString() {
		return "Size: " + origSize + " Pos: " + pos_x + "," + pos_y + " Modes: " + modes;
	}
	

	public void setCurrentMode(String currentMode) {
		this.currentMode = currentMode;
		setText(getName());
		setBackground(getColorFromString(getColor()));
	}

	
	public Color getColorFromString(String bgColor) {
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField(bgColor);
			color = (Color) field.get(null);
		} catch (Exception e) {
			color = null;
		}
		return color;
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public String getName() {
		if (modes.containsKey(currentMode)) {
			return modes.get(currentMode).getName();
		} else if (modes.containsKey("default")) {
			return modes.get("default").getName();
		} else {
			return "";
		}
	}
	
	
	public String getKeycode() {
		Mode m = modes.get(currentMode);
		if (m != null) {
			return modes.get(currentMode).getKeycode();
		} else if (modes.containsKey("default")) {
			return modes.get("default").getKeycode();
		}
		return "";
	}
	
	
	public String getColor() {
		Mode m = modes.get(currentMode);
		if (m != null) {
			return modes.get(currentMode).getColor();
		} else if (modes.containsKey("default")) {
			return modes.get("default").getColor();
		}
		return "";
	}


	public Set<String> getAllModes() {
		return modes.keySet();
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
	
	
	public String getCurrentMode() {
		return currentMode;
	}
	
	
	public Dimension getOrigSize() {
		return origSize;
	}
	
	
	public void setOrigSize(Dimension origSize) {
		this.origSize = origSize;
	}

	private class Mode {
		private String	name		= "";
		private String	keycode	= "";
		private String	color		= "";
		
		
		public Mode(String _name, String _keycode, String _color) {
			name = _name;
			keycode = _keycode;
			color = _color;
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
		
		
		@SuppressWarnings("unused")
		public String getColor() {
			return color;
		}
		
		
		@SuppressWarnings("unused")
		public void setColor(String color) {
			this.color = color;
		}
	}
	
	
	public boolean isAccept() {
		return accept;
	}
	
	
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
	
	
	public int getType() {
		return type;
	}
	
	
	public void setType(int type) {
		this.type = type;
	}
}
