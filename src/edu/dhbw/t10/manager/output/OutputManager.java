/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.t10.manager.output;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.profile.ProfileManager;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author Andres
 * 
 */
public class OutputManager
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Output.class);
	
	Output								out		= new Output();
	ProfileManager						proMan	= ProfileManager.getInstance();
	String								typed		= "";										// FIXME Was ist mit unicode zeichen
	String								suggest	= "";										// FIXME Was ist mit Unicode zeichen
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	@SuppressWarnings("unused")
	/**
	 * Process the Input data for 5 cases. 
	 */
	public void processInput(String input) {
		if (isBackSpace(input)) {
			if (typed.length() > 0) {
				typed = typed.substring(0, typed.length() - 2);
				suggest();
			} else {
				out.deleteChar(1);
			}
		} else if (isAcceptSpace(input)) {
			out.printString("\\SPACE\\");
			proMan.acceptWord(suggest);
			typed = "";
			suggest = "";
		} else if (isDeclineSpace(input)) {
			int diff = suggest.length() - typed.length();
			out.deleteChar(diff);
			out.printString("\\SPACE\\");
			typed = "";
			suggest = "";
		} else if (isControl(input)) {
			out.printString(input);
		} else {
			out.printString(input);
			typed = typed + input;
			suggest();
		}
	}
	

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
	

	/**
	 * 
	 * Suggest deletes the old suggest, requests a new one and prints it out.
	 * 
	 */
	private void suggest() {
		int diff = suggest.length() - typed.length();
		out.deleteChar(diff);
		suggest = proMan.getWordSuggest(typed);
		out.printString(suggest.substring(typed.length()));
	}
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
