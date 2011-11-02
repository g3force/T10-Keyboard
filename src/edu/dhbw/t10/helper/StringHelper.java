/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Nov 1, 2011
 * Author(s): DirkK
 *
 * *********************************************************
 */
package edu.dhbw.t10.helper;

/**
 * TODO DirkK, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author DirkK
 * 
 */
public class StringHelper {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public static String removePunctuation(String input) {
		input = input.replace("\"", "");
		input = input.replace("'", "");
		input = input.replace(".", "");
		input = input.replace(",", "");
		input = input.replace(";", "");
		input = input.replace(":", "");
		input = input.replace("(", "");
		input = input.replace(")", "");
		input = input.replace("[", "");
		input = input.replace("]", "");
		input = input.replace("/", "");
		input = input.replace("\\", "");
		input = input.replace("?", "");
		input = input.replace("!", "");
		return input;
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
