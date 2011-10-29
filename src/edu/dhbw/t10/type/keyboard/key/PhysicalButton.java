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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.lang.reflect.Field;

import javax.swing.JButton;


/**
 * this ist he PhysicalButton visible on the screen
 * extends JButton and consists position and such stuff
 * is never created itself, only extended
 * 
 * @author dirk
 * 
 */
public abstract class PhysicalButton extends JButton {
	/**  */
	private static final long	serialVersionUID	= -5515131562102664028L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private int						pos_x					= 0;
	private int						pos_y					= 0;
	private Dimension				origSize				= new Dimension(10, 10);

	
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
	public PhysicalButton(int size_x, int size_y, int pos_x, int pos_y) {
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
	public PhysicalButton() {
		setLayout(null);
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
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
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
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
	
	
	public Dimension getOrigSize() {
		return origSize;
	}
	
	
	public void setOrigSize(Dimension origSize) {
		this.origSize = origSize;
	}
}
