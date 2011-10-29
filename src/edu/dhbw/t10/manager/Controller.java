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
import edu.dhbw.t10.type.keyboard.key.Key;


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
	
	private String					typedWord;
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
	


	@Override
	public void actionPerformed(ActionEvent e) {
		Key key = (Key) e.getSource();

		if (key.isAccept()) {
			if (suggest.length() > typedWord.length())
				outputMan.unMark();
			outputMan.printChar(key);
			profileMan.acceptWord(suggest);
			typedWord = "";
			suggest = "";
		} else if (key.getType() == Key.CHAR_KEY) {
			outputMan.printChar(key);
			typedWord = typedWord + key.getText();
			suggest = profileMan.getWordSuggest(typedWord);
			outputMan.printSuggest(suggest, typedWord);
		} else if (key.getType() == Key.UNICODE_KEY) {
			outputMan.printChar(key);
		} else if (key.getText() == "\\BACK_SPACE\\") {
			if (typedWord.length() > 0) {
				typedWord = typedWord.substring(0, typedWord.length() - 2);
				outputMan.deleteChar(2); // Zwei, weil einmal muss die aktuelle Markierung gelöscht werden und
																	// dann ein Zeichen.
				suggest = profileMan.getWordSuggest(typedWord);
				outputMan.printSuggest(suggest, typedWord);
			} else {
				outputMan.deleteChar(1);
			}
		} else if ((key.getText() == "\\SPACE\\" || key.getText() == "\\ENTER\\")) {
			outputMan.printChar(key);
			typedWord = "";
			suggest = "";
		} else if (key.getType() == Key.CONTROL_KEY) {
			outputMan.printChar(key);
			if (key.getText() == "\\DELETE\\")
				suggest = typedWord;
		} else if (key.getType() == Key.MUTE_KEY) {
			// TODO Do something for mute
		}
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
