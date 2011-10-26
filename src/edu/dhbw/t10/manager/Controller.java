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

import java.awt.event.ActionListener;

import edu.dhbw.t10.manager.output.OutputManager;
import edu.dhbw.t10.manager.profile.ProfileManager;


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
		profileMan = ProfileManager.getInstance();
		outputMan = OutputManager.getInstance();
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
	
	
	// TODO DANIEL SCHNELLLLL

	@Override
	// public void actionPerformed(ActionEvent e) {
	// Key key = (Key) e.getSource();
	// if (key.getType() == Key.CHAR_KEY) {
	// typedWord = typedWord + key.getText();
	// String suggest = profileMan.getActive().getTree().getSuggest(typedWord);
	// if (isBackSpace(suggest)) {
	// if (typedWord.length() > 0) {
	// typedWord = typedWord.substring(0, typedWord.length() - 2);
	// outputMan.printSuggest(suggest, typedWord);
	// } else {
	// outputMan.deleteChar(1);
	// }
	// } else if (isAcceptSpace(suggest)) {
	// outputMan.printString("\\SPACE\\");
	// typedWord = "";
	// suggest = "";
	// } else if (isDeclineSpace(suggest)) {
	// int diff = suggest.length() - typedWord.length();
	// outputMan.deleteChar(diff);
	// outputMan.printString("\\SPACE\\");
	// typedWord = "";
	// suggest = "";
	// } else if (isControl(suggest)) {
	// outputMan.printString(suggest);
	// } else {
	// outputMan.printString(suggest);
	// printSuggest(suggest, typedWord);
	// }
	// }
	// // outputMan.
	// if (e.getSource() instanceof Key) {
	//
	// }
	// }
	
	
	/**
	 * Checks if a Keyinput is a Control Character
	 */
	private boolean isControl(String input) {
		if (input.charAt(0) == '\\' && input.charAt(input.length() - 1) == '\\' && !input.substring(0).startsWith("\\U+")) {
			return true;
		} else
			return false;
	}
	
	
	/**
	 * Checks if a Keyinput is a Back Space
	 */
	private boolean isBackSpace(String input) {
		if (input == "\\BACK_SPACE\\") {
			return true;
		} else
			return false;
	}
	
	
	/**
	 * Checks if a Keyinput is a Accept Space
	 */
	private boolean isAcceptSpace(String input) {
		if (input == "\\SPACE\\") {
			return true;
		} else
			return false;
	}
	
	
	/**
	 * Checks if a Keyinput is a Decline Space
	 */
	private boolean isDeclineSpace(String input) {
		if (input == "\\SPACE\\") {
			return true;
		} else
			return false;
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
