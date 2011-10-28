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
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;


/**
 * This class is the body of one key on the keyboard. It contains information
 * about size and position as well as for the names and keycodes for the several
 * modes like default, shift and alt_gr
 * 
 * @author NicolaiO
 * 
 */
public class ButtonKey extends JButton {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long		serialVersionUID	= 6949715976373962684L;
	

	private HashMap<SingleKey, SingleKey>	modes					= new HashMap<SingleKey, SingleKey>();
	// assigns to every modi
	private Dimension					origSize				= new Dimension(10, 10);
	private int							pos_x					= 0;
	private int							pos_y					= 0;
	private boolean							accept				= false;
	private ArrayList<SingleKey>				activeModes			= new ArrayList<SingleKey>();
	private String								color					= "";
	
	
	// contains the activated control key like shift


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
	public ButtonKey(int size_x, int size_y, int pos_x, int pos_y) {
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
	public ButtonKey() {
		setLayout(null);
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param mode
	 * @param name
	 * @param keycode
	 * @param color
	 * @author NicolaiO
	 */
	// TODO INTERFACE for keyboardlayoutloader
	public void addMode(SingleKey mode, SingleKey accordingKey) {
		modes.put(mode, accordingKey);
	}
	
	
	public String toString() {
		return "Size: " + origSize + " Pos: " + pos_x + "," + pos_y + " Modes: " + modes;
	}
	
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @param currentMode
	 * @author NicolaiO
	 */
	public void addCurrentMode(String mode) {
		// activeModes.add(mode);
		setText(modes.get(mode).getName());
		// setBackground(getColorFromString(getColor()));
	}
	
	
	/**
	 * @param bgColor
	 * @return
	 * @author NicolaiO
	 */
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
	
	
	public ArrayList<SingleKey> getKeycode() {
		ArrayList<SingleKey> output = new ArrayList<SingleKey>();
		if (activeModes.size() == 0) {
			output.add(modes.get("default"));
		} else if (activeModes.size() == 1) {
			output.add(modes.get(activeModes.get(0)));
		} else {
			output.addAll(activeModes);
			output.add(modes.get("default"));
		}
		return output;
	}
	
	
	public boolean isAccept() {
		return accept;
	}



	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * TODO NicolaiO, add comment!
	 * 
	 * @return
	 * @author NicolaiO
	 */
	public String getColor() {
		String m = modes.get(currentMode);
		if (m != null) {
			return modes.get(currentMode).getColor();
		} else if (modes.containsKey("default")) {
			return modes.get("default").getColor();
		}
		return "";
	}
	
	
	// public Set<String> getAllModes() {
	// return modes.keySet();
	// }
	
	

	
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
	
	
	public HashMap<String, SingleKey> getModes() {
		return modes;
	}
	
	
	public void setModes(HashMap<String, SingleKey> modes) {
		this.modes = modes;
	}
	
	
	public ArrayList<String> getActiveMode() {
		return activeModes;
	}
	
	
	public Dimension getOrigSize() {
		return origSize;
	}
	
	
	public void setOrigSize(Dimension origSize) {
		this.origSize = origSize;
	}
	
	
	public int getType() {
		if (activeModes.size() == 0) {
			return modes.get("default").getType();
		} else {
			return modes.get(activeModes.get(0)).getType();
			// TODO returns the fist activeMode, insert priority?
		}
	}

	// private class Mode {
	// private String name = "";
	// private String keycode = "";
	// private String color = "";
	//
	//
	// public Mode(String _name, String _keycode, String _color) {
	// name = _name;
	// keycode = _keycode;
	// color = _color;
	// }
	//
	//
	// public String toString() {
	// return "(" + name + " " + keycode + ")";
	// }
	//
	//
	// @SuppressWarnings("unused")
	// public String getName() {
	// return name;
	// }
	//
	//
	// @SuppressWarnings("unused")
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	//
	// @SuppressWarnings("unused")
	// public String getKeycode() {
	// return keycode;
	// }
	//
	//
	// @SuppressWarnings("unused")
	// public void setKeycode(String keycode) {
	// this.keycode = keycode;
	// }
	//
	//
	// @SuppressWarnings("unused")
	// public String getColor() {
	// return color;
	// }
	//
	//
	// @SuppressWarnings("unused")
	// public void setColor(String color) {
	// this.color = color;
	// }
	// }
}
