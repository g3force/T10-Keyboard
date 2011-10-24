/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 24, 2011
 * Author(s): felix
 *
 * *********************************************************
 */
package edu.dhbw.t10.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import edu.dhbw.t10.type.Key;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix
 * 
 */
public class EventCollector implements ActionListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private KeyboardListener	keyListener;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Key) {
			this.keyListener.keyboardActionAlterEy(new KeyboardEvent(this, ((Key) e.getSource()).getKeycode()));
		}
	}
	

	public void addListener(EventListener listener) {
		if (listener instanceof KeyboardListener) {
			this.keyListener = (KeyboardListener) listener;
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
