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
	public static OutputManager getInstance() {
		if (instance == null) {
			instance = new OutputManager();
		}
		return instance;
	}

	

	

	/**
	 * 
	 * printSuggest deletes the old suggest, prints it out and mark the chars that are added from the suggest word.
	 * 
	 */
	public void printSuggest(String newSuggest, String typed) {
		out.printString(convertToUnicode(newSuggest.substring(typed.length())));
		out.markChar(newSuggest.length() - typed.length());
	}
	

	public String convertToUnicode(String input) {
		// TODO
		return input;
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public Output getOutput() {
		return out;
	}
}
