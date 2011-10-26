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

import edu.dhbw.t10.manager.output.OutputManager;
import edu.dhbw.t10.manager.profile.ProfileManager;
import edu.dhbw.t10.type.keyboard.Key;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix
 * 
 */
public class Controller implements ActionListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static Controller	instance;
	
	private String						typedWord	= ""; // FIXME Was ist mit unicode zeichen
	private ProfileManager			profileMan;
	private OutputManager			outputMan;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private Controller() {
		typedWord = "";
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Key k = (Key) e.getSource();
		typedWord = typedWord + k.getText();
		String suggest = profileMan.getActive().getTree().getSuggest(typedWord);
		// outputMan.
		if (e.getSource() instanceof Key) {

		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
