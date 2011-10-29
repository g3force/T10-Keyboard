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

import edu.dhbw.t10.type.keyboard.key.Key;


/**
 * TODO NicolaiO, add comment!
 * 
 * 
 * @author Andres
 * 
 */
public class OutputManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(OutputManager.class);
	
	Output								out		= Output.getInstance();
	private static OutputManager	instance	= null;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private OutputManager() {
		
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public boolean printChar(Key c) {
		return out.printChar(c);
	}
	

	public boolean deleteChar(int length) {
		if (length <= 0)
			return false;
		else {
			for (int i = 0; i < length; i++) {
				out.printString("\\BACK_SPACE\\", Key.CONTROL);
			}
			return true;
		}
	}


	
	

	// // TODO printString bentigt ein Argument für den Type!!!!
	// public boolean printString(String input) {
	// // TODO Key.UNICODE enterfernen und Type vom function header übernehmen!!!
	// return out.printString(input, Key.UNICODE);
	// }
	

	public boolean mark(int i) {
		// FIXME Logik von Output hierherkopieren....
		return out.markChar(i);
	}
	

	public void unMark() {
		out.printString("\\RIGHT\\", Key.CONTROL);
	}

	
	/**
	 * 
	 * printSuggest deletes the old suggest, prints it out and mark the chars that are added from the suggest word.
	 * 
	 */
	public void printSuggest(String newSuggest, String typed) {
		if (newSuggest.length() > typed.length()) { //
			out.printString(convertToUnicode(newSuggest.substring(typed.length())), Key.UNICODE);
		out.markChar(newSuggest.length() - typed.length());
		}
	}
	

	/**
	 * 
	 * TODO andres, add comment!
	 * Quelle: http://www.daniweb.com/software-development/java/threads/147397
	 * modified
	 * 
	 * FIXME evtl. diese Funktion in Output.class und mit extractUnicode kombinieren
	 * 
	 * @param input
	 * @return
	 * @author andres
	 */
	private String convertToUnicode(String input) {
		StringBuffer ostr = new StringBuffer();
		
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			
			if ((ch >= 0x0020) && (ch <= 0x007e)) { // Does the char need to be converted to unicode?
				ostr.append(ch); // No.
			} else { // Yes.
				ostr.append("\\u+"); // own unicode format
				String hex = Integer.toHexString(input.charAt(i) & 0xFFFF); // Get hex value of the char.
				for (int j = 0; j < 4 - hex.length(); j++)
					// Prepend zeros because unicode requires 4 digits
					ostr.append("0");
				ostr.append(hex.toLowerCase()); // standard unicode format.
				ostr.append("\\"); // own unicode format
			}
		}
		return (new String(ostr)); // Return the stringbuffer cast as a string.
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public static OutputManager getInstance() {
		if (instance == null) {
			instance = new OutputManager();
		}
		return instance;
	}
}
