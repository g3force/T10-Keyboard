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

import edu.dhbw.t10.type.keyboard.key.Key;
import edu.dhbw.t10.type.keyboard.key.ModeKey;


/**
 * The OutputManager provides the interface between the controller and Output. <br>
 * It gives different meta methods for a better handling in the Output. <br>
 * 
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
	 * Instantiate Output. If this fails with an UnknownOSException, the Keyboard is closed.
	 * 
	 * @author DanielAl
	 */
	public OutputManager() {
		logger.debug("initializing...");
		try {
			out = new Output();
		} catch (UnknownOSException err) {
			logger.fatal(err.getMessage());
			// If no Output could be instanciated close the Application
			System.exit(-1);
		}
		logger.debug("initialized");
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
	public boolean printKey(Key c) {
		return out.printKey(c);
	}
	
	
	/**
	 * Deletes 'num' chars via sending so many Back_Spaces...
	 * Implemented directly and not with printCombi...
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
		ArrayList<Key> markCombiHold = new ArrayList<Key>();
		ArrayList<Key> markCombiPress = new ArrayList<Key>();
		markCombiHold.add(new Key(0, "Shift", "\\SHIFT\\", Key.CONTROL, false, "", ""));
		for (int j = 1; j < num + 1; j++) {
			// Add one marked char via one LEFT Key...
			markCombiPress.add(new Key(j, "Left", "\\LEFT\\", Key.CONTROL, false, "", ""));
			logger.trace("Added one mark...");
		}
		boolean mark = out.printCombi(markCombiHold, markCombiPress);
		logger.info(num + " Symboly marked");
		return mark;
	}
	
	
	/**
	 * Unmark all things via pressing the RIGHT Key.
	 * 
	 * @deprecated not working with all application. The unmark of marked chars works differently in different
	 *             applications.
	 * @author DanielAl
	 */
	public void unMark() {
		out.printString("\\RIGHT\\", Key.CONTROL);
		logger.trace("Keys unmarked");
	}
	
	
	/**
	 * Delete all marked things via pressing the DELETE Key
	 * 
	 * @author DanielAl
	 */
	public void delMark(int num) {
		if (num > 0)
			out.printString("\\DELETE\\", Key.CONTROL);
		logger.trace("marked Keys are deleted");
	}
	
	
	/**
	 * Overloaded method printSuggest to call the default function of printSoggest (wich marks the Suggested chars, func
	 * = 0)
	 * @param newSuggest
	 * @param typed
	 * @author DanielAl
	 */
	public void printSuggest(String newSuggest, String typed) {
		printSuggest(newSuggest, typed, 0);
	}
	
	
	/**
	 * Prints a new Suggest for given chars and mark the suggested chars, which aren't yet typed, if func = 0. This is
	 * the default function of printSuggest.<br>
	 * If func !=0 this function will only prints the suggest without marking the suggested chars. This is used,
	 * beacause, the unMark() method, which unmarks the suggest, doesn't work with all applications, and that is the
	 * workaround.
	 * 
	 * @author DanielAl
	 * @param newSuggest
	 * @param typed
	 * @param func
	 */
	public void printSuggest(String newSuggest, String typed, int func) {
		// only used if there are really chars that aren't typed yet...
		if (newSuggest.length() > typed.length()) {
			out.printString(newSuggest.substring(typed.length()), Key.CHAR);
			if (func == 0)
				mark(newSuggest.length() - typed.length());
			logger.debug("Suggest: " + newSuggest + " printed");
		}
	}
	
	
	/**
	 * Print a combination of keys, given by a list of ModeKeys and a finishing key
	 * 
	 * @param mks list of ModeKeys to be pressed
	 * @param key key to be pressed at the end
	 * @author NicolaiO
	 */
	public void printCombi(ArrayList<ModeKey> mks, Key key) {
		ArrayList<Key> pressed = new ArrayList<Key>();
		ArrayList<Key> hold = new ArrayList<Key>();

		for (ModeKey mk : mks) {
			if (mk.getState() != ModeKey.DEFAULT) {
				hold.add((Key) mk);
			}
		}
		pressed.add(key);
		out.printCombi(hold, pressed);
	}
	

	// --------------------------------------------------------------------------
	// --- keyIs Actions --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Accept a suggested word, unmarks it and prints the given key.
	 * 
	 * @param key
	 * @author DanielAl
	 */
	public void keyIsAccept(Key key, String typedWord, String suggest) {
		if (suggest.length() > typedWord.length())
			// outputMan.unMark();
			printSuggest(suggest, typedWord, 1);
		printKey(key);
	}
	
	
	/**
	 * Prints the given key, added it to the typed String and get a new suggest and prints it...
	 * @param key
	 * @author DanielAl
	 */
	public void keyIsChar(Key key, String typedWord, String suggest) {
		printKey(key);
		printSuggest(suggest, typedWord);
	}
	
	
	/**
	 * Handles a typed BackSpace.<br>
	 * If there is a typedWord delete the Mark if exists (delMark), delete the last typed char and print a new suggest.<br>
	 * If there is no typedWord send a BackSpace.
	 * 
	 * FIXME DanielAl Bug mit letztten char löschen...
	 * @author DanielAl
	 */
	public void keyIsBackspace(String typedWord, String suggest) {
		if (typedWord.length() + 1 > 0) {
			// Differencebetween suggest and typedWord plus 1, because typedWord was decreased before...
			delMark(suggest.length() - typedWord.length() + 1);
			deleteChar(1);
			printSuggest(suggest, typedWord);
		} else {
			deleteChar(1);
		}
	}

	
	/**
	 * Prints a Control or Unicode Key, <br>
	 * if no DELETE or BACK_SPACE, these are special Keys and handled with extra methods...
	 * 
	 * @param key
	 * @author DanielAl
	 */
	public void keyIsControlOrUnicode(Key key, String typedWord, String suggest) {
		if (typedWord.length() < suggest.length()) {
			delMark(suggest.length() - typedWord.length());
		}
		printKey(key);
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
