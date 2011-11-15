/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Nov 15, 2011
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard;

import java.awt.Dimension;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class Image {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private Dimension	origSize	= new Dimension(10, 10);
	private int			pos_x		= 0;
	private int			pos_y		= 0;
	private String		src		= "";


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public Image(String src, int size_x, int size_y, int pos_x, int pos_y) {
		init(src, size_x, size_y, pos_x, pos_y);
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	private void init(String src, int size_x, int size_y, int pos_x, int pos_y) {
		this.origSize = new Dimension(size_x, size_y);
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.src = src;
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public Dimension getOrigSize() {
		return origSize;
	}
	
	
	public void setOrigSize(Dimension origSize) {
		this.origSize = origSize;
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
	
	
	public String getSrc() {
		return src;
	}
	
	
	public void setSrc(String src) {
		this.src = src;
	}
}
