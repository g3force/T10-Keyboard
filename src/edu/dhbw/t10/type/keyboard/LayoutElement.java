/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Nov 15, 2011
 * Author(s): DirkK
 *
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard;

import java.awt.Dimension;


/**
 * should be implemented by all graphical elements saved in the layout.xml
 * contains the physical functions as size and position
 * 
 * @author DirkK
 * 
 */
public interface LayoutElement {
	public Dimension getOrigSize();
	
	
	public int getPos_x();
	
	
	public int getPos_y();
}
