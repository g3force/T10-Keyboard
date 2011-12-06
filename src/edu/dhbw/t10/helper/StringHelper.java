/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Nov 1, 2011
 * Author(s): DirkK, DanielAl
 * 
 * *********************************************************
 */
package edu.dhbw.t10.helper;

import java.util.ArrayList;


/**
 * Provides Helper methods for String Handling...
 * 
 * @author DirkK, DanielAl
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
	/**
	 * Removes all punctuation Symbols of a given String.<br>
	 * 
	 * @author DirkK
	 */
	public static String removePunctuation(String input) {
		input = input.replace("\t", "");
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
	
	
	/**
	 * 
	 * Find a Unicode in a given String and returns a List with the indices.
	 * 
	 * @param sequence
	 * @return
	 * @author DanielAl
	 */
	public static ArrayList<Integer> extractUnicode(String sequence) {
		ArrayList<Integer> unicodeStart = new ArrayList<Integer>();
		int help = 0;
		while (help < sequence.length()) {
			if (sequence.substring(help).startsWith("\\U+")) {
				help = sequence.indexOf("\\U+", help);
				unicodeStart.add(help);
				help += 8;
			} else
				help++;
		}
		return unicodeStart;
	}
	
	
	/**
	 * Converts a given String to a String, where non-ASCII chars a replaced by a Unicode Sequence.
	 * 
	 * Quelle: http://www.daniweb.com/software-development/java/threads/147397
	 * modified
	 * 
	 * @param input
	 * @return
	 * @author DanielAl
	 */
	public static String convertToUnicode(String input) {
		StringBuffer ostr = new StringBuffer();
		
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			
			if ((ch >= 0x0020) && (ch <= 0x007e)) { // Does the char need to be converted to unicode?
				ostr.append(ch); // No.
			} else { // Yes.
				ostr.append("\\U+"); // own unicode format
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
	
	
	public static String addEnding(String path, String ending) {
		String existingEnding = "";
		if (path.lastIndexOf(".") > 0) {// any ending
			existingEnding = path.substring(path.lastIndexOf("."), path.length());
			if (!ending.equals(existingEnding))// wrong ending
				path += ending;
		} else { // no ending
			path += ending;
		}
		return path;
	}
	
	
	/**
	 * String has to be of the format: (<number>(-<number>)*:)*
	 * @param input
	 * @return
	 * @author dirk
	 */
	public static String transformNumbersToChars(String input) {
		String output = "";
		for (String range : input.split(":")) {
			for (String number : range.split("-")) {
				try {
					int nr = Integer.parseInt(number);
					char c = (char) nr;
					output += String.valueOf(c) + "-";
				} catch (NumberFormatException e) {
					System.out.println("Exception: " + number);
				}
			}
			if (output.length() > 0 && output.charAt(output.length() - 1) == '-')
				output = output.substring(0, output.length() - 1) + ":";
		}
		if (output.length() > 0 && output.charAt(output.length() - 1) == ':')
			output = output.substring(0, output.length() - 1);
		return output;
	}
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
