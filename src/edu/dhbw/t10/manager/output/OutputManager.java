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

import edu.dhbw.t10.helper.StringHelper;
import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;


/**
 * TODO DanielAl, add comment!
 * 
 * 
 * @author DanielAl
 * 
 */
public class OutputManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(OutputManager.class);
	
	Output								out;

	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public OutputManager() {
		try {
			out = new Output();
		} catch (UnknownOSException err) {
			logger.fatal(err.getMessage());
			// If no Output could be instanciated close the Application
			System.exit(-1);
		}
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
			out.printString(StringHelper.convertToUnicode(newSuggest.substring(typed.length())), 0);
			out.markChar(newSuggest.length() - typed.length());
		}
	}
	

	public void printCombi(Button b) {
		
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
