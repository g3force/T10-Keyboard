/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 21, 2011
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.t10.type;

import java.awt.Rectangle;
import java.util.ArrayList;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class KeyboardLayout {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private ArrayList<Key>	keys		= new ArrayList<Key>();
	private int					size_x	= 0;
	private int					size_y	= 0;
	private float				scale		= 1;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public KeyboardLayout(int size_x, int size_y, float scale) {
		this.size_x = size_x;
		this.size_y = size_y;
		this.scale = scale;
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public void add(Key key) {
		keys.add(key);
	}
	
	
	public void rescale() {
		for (Key k : keys) {
			Rectangle rect = k.getBounds();
			rect.setBounds((int) (k.getPos_x() * scale), (int) (k.getPos_y() * scale), (int) (k.getSize().width * scale),
					(int) (k.getSize().height * scale));
			k.setBounds(rect);
		}
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public ArrayList<Key> getKeys() {
		return keys;
	}
	
	
	public void setKeys(ArrayList<Key> keys) {
		this.keys = keys;
	}
	
	
	public int getSize_x() {
		return (int) (size_x * scale);
	}
	
	
	public void setSize_x(int size_x) {
		this.size_x = size_x;
	}
	
	
	public int getSize_y() {
		return (int) (size_y * scale);
	}
	
	
	public void setSize_y(int size_y) {
		this.size_y = size_y;
	}
	
	
	public float getScale() {
		return scale;
	}
	
	
	public void setScale(int scale) {
		this.scale = scale;
	}


}
