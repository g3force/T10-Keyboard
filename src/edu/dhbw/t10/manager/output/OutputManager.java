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

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;
import edu.dhbw.t10.type.keyboard.key.ModeKey;
import edu.dhbw.t10.type.keyboard.key.MuteButton;
import edu.dhbw.t10.type.profile.Profile;


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
	private String						typedWord;
	private String						suggest;
	
	
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
		clearWord();
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
	public void keyIsChar(Key key) {
		printKey(key);
		printSuggest(suggest, typedWord);
	}
	
	
	/**
	 * Handles a typed BackSpace.<br>
	 * If there is a typedWord delete the Mark if exists (delMark), delete the last typed char and print a new suggest.<br>
	 * If there is no typedWord send a BackSpace.
	 * 
	 * @author DanielAl
	 */
	
	public void keyIsBackspace(String oldTypedWord, String oldSuggest) {
		if (oldTypedWord.length() > 0) {
			// If oldSuggest not equal to oldTypedWord, it must be longer and so it is marked. Then this mark has to be
			// deleted first.
			if (!oldTypedWord.equals(oldSuggest))
				delMark(oldSuggest.length() - oldTypedWord.length());
			deleteChar(1);
			printSuggest(suggest, typedWord);
		} else {
			// No OldTypedWord exists so only the left char of the cursor is deleted
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
	public void keyIsControlOrUnicode(Key key) {
		if (typedWord.length() < suggest.length()) {
			delMark(suggest.length() - typedWord.length());
		}
		printKey(key);
	}
	
	
	// --------------------------------------------------------------------------
	// --- buttons pressed actions ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Switchs between the three different Mute modes...<br>
	 * Modes are:<br>
	 * - AUTO_COMPLETING - If activated, this prints a suggested Word behind the typed chars and mark them...
	 * - AUTO_PROFILE_CHANGE - If activated, this changes the profiles based on the surrounded context.
	 * - TREE_EXPANDING - If activated, accepted words are saved in the dictionary...
	 * @param muteB
	 * @author DanielAl
	 */
	public void muteButtonPressed(MuteButton muteB, Profile activeProfile) {
		muteB.push();
		int type = muteB.getType();
		switch (type) {
			case MuteButton.AUTO_COMPLETING:
				if (muteB.isActivated()) {
					clearWord();
				}
				activeProfile.setAutoCompleting(muteB.isActivated());
				break;
			case MuteButton.AUTO_PROFILE_CHANGE:
				activeProfile.setAutoProfileChange(muteB.isActivated());
				break;
			case MuteButton.TREE_EXPANDING:
				if (muteB.isActivated()) {
					clearWord();
				}
				activeProfile.setTreeExpanding(muteB.isActivated());
				break;
		}
		logger.debug("MuteButton pressed");
	}
	
	
	/**
	 * Do the logic for a button event. Switch between different types, specific Keys and a Key Combination...
	 * 
	 * @param button
	 * @author DanielAl
	 */
	public void buttonPressed(Button button, Profile activeProfile) {
		Key key = (Key) button.getPressedKey();
		
		// currently we do not support some buttons for linux...
		if (Output.getOs() == Output.LINUX
				&& (button.getKey().getKeycode().equals("\\WINDOWS\\") || button.getKey().getKeycode()
						.equals("\\CONTEXT_MENU\\"))) {
			Controller.getInstance().showStatusMessage("Button not supported by your OS");
			return;
		}
		
		// get all currently pressed Modekeys
		ArrayList<ModeKey> pressedModeKeys = activeProfile.getKbdLayout().getPressedModeKeys();
		
		if (key.getKeycode().equals("\\CAPS_LOCK\\")) {
			keyIsCapsLock(activeProfile);
		} else {
			// Print the key iff zero or one ModeKeys is pressed
			if (pressedModeKeys.size() - button.getActiveModes().size() < 1) {
				if (key.isAccept()) {
					keyIsAccept(key, typedWord, suggest);
					acceptWord(suggest, activeProfile);
				} else if (key.getType() == Key.CHAR) {
					typedWord = typedWord + key.getName();
					suggest = activeProfile.getWordSuggest(typedWord);
					keyIsChar(key);
				} else if (key.getKeycode().equals("\\BACK_SPACE\\")) {
					if (typedWord.length() > 0) {
						String oldTypedWord = typedWord;
						String oldSuggest = suggest;
						typedWord = typedWord.substring(0, typedWord.length() - 1);
						suggest = activeProfile.getWordSuggest(typedWord);
						keyIsBackspace(oldTypedWord, oldSuggest);
					} else {
						keyIsBackspace(typedWord, suggest);
					}
				} else if (key.getKeycode().equals("\\DELETE\\")) {
					printKey(key);
					suggest = typedWord;
				} else if (key.getType() == Key.CONTROL || key.getType() == Key.UNICODE) {
					keyIsControlOrUnicode(key);
					if (key.getType() == Key.UNICODE
							|| (key.getKeycode().equals("\\SPACE\\") || key.getKeycode().equals("\\ENTER\\"))) {
						acceptWord(typedWord, activeProfile);
					} else if (key.getType() == Key.CONTROL) {
						clearWord();
					}
				}
				logger.debug("Key pressed: " + key.toString());
			} else {
				// print the key combi else (-> pressedModeKeys.size() - button.getActiveModes().size() >= 1
				logger.debug("Keycombi will be executed. Hint: " + pressedModeKeys.size() + "-"
						+ button.getActiveModes().size() + " >= 1");
				logger.trace(pressedModeKeys);
				printCombi(pressedModeKeys, button.getKey());
			}
		}
		
		// unset all ModeButtons, that are in PRESSED state
		activeProfile.getKbdLayout().unsetPressedModes();
	}
	
	
	/**
	 * Sets the typedWord and the suggest to an empty String. So you can begin again with a new word.
	 * 
	 * @author DanielAl
	 */
	public void clearWord() {
		typedWord = "";
		suggest = "";
	}
	
	/**
	 * is called whenever a word shall be accepted
	 * 
	 * @param word
	 * @author DirkK
	 */
	private void acceptWord(String word, Profile activeProfile) {
		boolean success = activeProfile.acceptWord(word);
		if (success) {
			Controller.getInstance().showStatusMessage("Word inserted: " + word);
			logger.trace("Word accepted");
		}
		clearWord();
	}
	
	/**
	 * Run this with a caps_lock key to trigger all shift buttons.
	 * If Shift state is DEFAULT, it will be changed to HOLD, else to DEFAULT
	 * 
	 * @param key
	 * @author NicolaiO
	 */
	private void keyIsCapsLock(Profile activeProfile) {
		logger.trace("CapsLock");
		for (ModeKey mk : activeProfile.getKbdLayout().getModeKeys()) {
			if (mk.getKeycode().equals("\\SHIFT\\")) {
				if (mk.getState() == ModeKey.DEFAULT) {
					mk.setState(ModeKey.HOLD);
				} else {
					mk.setState(ModeKey.DEFAULT);
				}
				break;
			}
		}
		// presenter.pack();
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
