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
import edu.dhbw.t10.type.keyboard.key.Button;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix, Andres
 * 
 */
public class Controller implements ActionListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static Controller	instance;
	
	private String					typedWord;	// FIXME Was ist mit unicode zeichen
	private String					suggest;

	private ProfileManager		profileMan;
	private OutputManager		outputMan;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private Controller() {
		typedWord = "";
		suggest = "";
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
	public void actionPerformed(ActionEvent e) {
		Button key = (Button) e.getSource();

		// if (key.getType() == Key.CHAR_KEY) {
		// outputMan.getOutput().printChar(key);
		// typedWord = typedWord + key.getText();
		// suggest = profileMan.getActive().getTree().getSuggest(typedWord);
		// outputMan.printSuggest(suggest, typedWord);
		//
		//
		// } else if (isBackSpace(key.getText())) {
		// if (typedWord.length() > 0) {
		// typedWord = typedWord.substring(0, typedWord.length() - 2);
		// outputMan.printSuggest(key.getText(), typedWord);
		// } else {
		// outputMan.getOutput().printChar(key);
		// }
		// } else if (key.isAccept()) {
		// // TODO Daniel demarkiere Wort, schreibe SPACE und lösche Buffer; akzeptiere Wort
		// } else if (key.getText() == "\\SPACE\\" && !key.isAccept()) {
		// // TODO Daniel schreibe Leerzeiechen und lösche Puffer (WOrt wird wegen Markierung gelöscht)
		// } else if (key.getType() == Key.CONTROL_KEY) {
		// // TODO Daniel sende Control_Key
		// } else if (key.getType() == Key.CHAR_KEY || key.getType() == Key.UNICODE_KEY) {
		// outputMan.getOutput().printString(key.getText());
		// outputMan.printSuggest(key.getText(), typedWord);
		// } else if (key.getType() == Key.MUTE_KEY) {
		// // TODO Daniel Do something for mute
		// }
	}
	

	/**
	 * Checks if a Keyinput is a Control Character
	 */
	private boolean isControl(String input) {
		// if (input.charAt(0) == '\\' && input.charAt(input.length() - 1) == '\\' &&
		// !input.substring(0).startsWith("\\U+")) {
		if (true) {
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
