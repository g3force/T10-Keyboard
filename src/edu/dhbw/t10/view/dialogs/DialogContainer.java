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
 * Container for ProfileChosser. Only important for event handling.
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
	 * Constructor for DialogContainer...
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
