/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Nov 15, 2011
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard;

import java.awt.Dimension;


/**
 * TODO dirk, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author dirk
 * 
 */
public interface LayoutElement {
	public Dimension getOrigSize();
	
	
	public int getPos_x();
	
	
	public int getPos_y();
}
