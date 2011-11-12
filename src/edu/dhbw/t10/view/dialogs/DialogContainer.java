/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 10.11.2011
 * Author(s): FelixP
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view.dialogs;

import javax.swing.JFrame;

import edu.dhbw.t10.view.menus.EMenuItem;


/**
 * TODO FelixP, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author FelixP
 * 
 */
public class DialogContainer extends JFrame {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long	serialVersionUID	= 8371311241882585808L;
	private ProfileChooser		pc;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * TODO FelixP, add comment!
	 * 
	 * @param menuType
	 * @author FelixP
	 */
	public DialogContainer(EMenuItem menuType) {
		pc = new ProfileChooser(menuType, this);
		
		getContentPane().add(pc, "Center");
		pack();
		
		
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
