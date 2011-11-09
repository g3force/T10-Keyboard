/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 09.11.2011
 * Author(s): DanielAl
 *
 * *********************************************************
 */
package edu.dhbw.t10.view.menus;

import javax.swing.JLabel;


/**
 * Statusbar, where different things could be displayed for 5 Seconds...
 * TODO DanielAl Thread to be able to run the overwrite method all over the time...
 * 
 * @author DanielAl
 * 
 */
public class StatusBar extends JLabel {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	/**  */
	private static final long	serialVersionUID	= 5692213469714617751L;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public StatusBar(String message) {
		super();
		setText(message);
		// super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public void message(String message) {
		// TODO DanielAl Queuing system so that a message is dispolayed for 5 sec...
		setMessage(message);
	}


	/**
	 * Set the text of the StatusBar.
	 * 
	 * @param message
	 * @author DanielAl
	 */
	private void setMessage(String message) {
		setText(" " + message);
	}
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
