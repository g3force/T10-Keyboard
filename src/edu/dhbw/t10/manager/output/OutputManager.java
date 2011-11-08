/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): DanielAl
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.output;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;


/**
 * The OutputManager provides the interface between the controller and Output. <br>
 * It gives different meta methods for a better handling in the Output. <br>
 * @author DanielAl
 */
public class OutputManager {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(OutputManager.class);
	
	// Output instance
	Output								out;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Constructor for this class with no parameters<br>
	 * Instanciate Output. If this fails with an UnknownOSException, the Keyboard is closed.
	 * 
	 * @author DanielAl
	 * 
	 */
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
	
	/**
	 * Calls Output.printChar(c)
	 * @param Key c
	 * @return boolean
	 * 
	 * @author DanielAl
	 */
	public boolean printChar(Key c) {
		return out.printChar(c);
	}
	
	/**
	 * Deletes 'num' chars via sending so many Back_Spaces...
	 * 
	 * @param int num
	 * @return boolean
	 * @author DanielAl
	 */
	public boolean deleteChar(int num) {
		if (num <= 0)
			return false;
		else {
			for (int i = 0; i < num; i++) {
				out.printString("\\BACK_SPACE\\", Key.CONTROL);
			}
			return true;
		}
	}
	
	/**
	 * Marks 'num' chars backwards via holding SHIFT and press the LEFT Key.
	 * 
	 * @author DanielAl
	 * @return boolean
	 * @param int num
	 */
	public boolean mark(int num) {
		// Use a ArrayList to be able to use the printCombi
		ArrayList<Key> markCombi = new ArrayList<Key>();
		markCombi.add(new Key(0, "Shift", "\\SHIFT\\", Key.CONTROL));
		for (int j = 0; j < num; j++) {
			// Add one marked char via one LEFT Key...
			markCombi.add(new Key(j, "Left", "\\LEFT\\", Key.CONTROL));
		}
		boolean mark = out.printCombi(markCombi);
		logger.info(num + " Symboly marked");
		return mark;
	}

	/**
	 * Unmark all things via pressing the RIGHT Key
	 * 
	 * @author DanielAl
	 */
	public void unMark() {
		out.printString("\\RIGHT\\", Key.CONTROL);
	}
	

	/**
	 * Prints a new Suggest for given chars and mark the suggested chars, which aren't yet typed.<br>
	 * 
	 * @author DanielAl
	 * @param String newSuggest, String typed
	 */
	public void printSuggest(String newSuggest, String typed) {
		// only used if there are really chars that aren't typed yet...
		if (newSuggest.length() > typed.length()) {
			out.printString(newSuggest.substring(typed.length()), 0);
			mark(newSuggest.length() - typed.length());
		}
	}
	

	/**
	 * Prints a Key Combination...
	 * 
	 * @param Button b
	 * @author DanielAl
	 */
	public void printCombi(Button b) {
		out.printCombi(b.getSingleKey());
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
