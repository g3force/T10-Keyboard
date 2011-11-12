/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 28, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;

import org.apache.log4j.Logger;


/**
 * this ist he PhysicalButton visible on the screen
 * extends JButton and consists position and such stuff
 * is never created itself, only extended
 * 
 * @author DirkK, NicolaiO
 * 
 */
public abstract class PhysicalButton extends JButton {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long		serialVersionUID	= -5515131562102664028L;
	@SuppressWarnings("unused")
	private static final Logger	logger				= Logger.getLogger(PhysicalButton.class);
	private int							pos_x					= 0;
	private int							pos_y					= 0;
	private Dimension					origSize				= new Dimension(10, 10);
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Create a new Key file with given size and position
	 * 
	 * @param size_x Size of the key button
	 * @param size_y Size of the key button
	 * @param pos_x Position of the key button
	 * @param pos_y Position of the key button
	 * @author NicolaiO
	 */
	public PhysicalButton(int size_x, int size_y, int pos_x, int pos_y) {
		this.origSize = new Dimension(size_x, size_y);
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		setBounds(getPos_x(), getPos_y(), getSize().width, getSize().height);
		setLayout(null);
		setMargin(new Insets(0, 0, 0, 0));
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
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
