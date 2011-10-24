/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 20.10.2011
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.output;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * TODO andres, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author andres
 * 
 */
public class Keys {
	interface KeyCommand {
		void sendKeys(Robot keyBot);
	}

	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Output.class);

	private Map<String, Integer>	keyList;
	private List<Integer>			shiftList;
	private List<Integer>			altgrList;
	private List<Integer>			crtlList;
	
	
	// FIXME
	// private Map<String, KeyCommand> keyList;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	Keys() {
		/*
		 * FIXME Idee: Command für Shift, Crtl und AltGr Funktionen in die HashMap speichern. nicht nur Konstante. Java
		 * reflection verwenden "Field f = KeyEvent.class.getfield("VK_" + Char)"
		 * evtl. HashMap aus Datei einlesen? --> Layout-Datei einlesen und spezifische HashMap generieren?
		 * Ländereinstellungen beachten
		 * FIXME Umlaute, Fragezeichen, Pipe, ß fehlen --> Unicode Tastenkombination mit Alt + Numpad Tastenkombi
		 */
		/*
		 * keyList = new HashMap<String, KeyCommand>();
		 * keyList.put("0", new KeyCommand() {
		 * public void sendKeys(Robot keyBot)
		 * }
		 * });
		 */

		keyList = new HashMap<String, Integer>();

		// Alphanumeric Keycodes
		keyList.put("0", KeyEvent.VK_0);
		keyList.put("1", KeyEvent.VK_1);
		keyList.put("2", KeyEvent.VK_2);
		keyList.put("3", KeyEvent.VK_3);
		keyList.put("4", KeyEvent.VK_4);
		keyList.put("5", KeyEvent.VK_5);
		keyList.put("6", KeyEvent.VK_6);
		keyList.put("7", KeyEvent.VK_7);
		keyList.put("8", KeyEvent.VK_8);
		keyList.put("9", KeyEvent.VK_9);
		keyList.put("A", KeyEvent.VK_A);
		keyList.put("B", KeyEvent.VK_B);
		keyList.put("C", KeyEvent.VK_C);
		keyList.put("D", KeyEvent.VK_D);
		keyList.put("E", KeyEvent.VK_E);
		keyList.put("F", KeyEvent.VK_F);
		keyList.put("G", KeyEvent.VK_G);
		keyList.put("H", KeyEvent.VK_H);
		keyList.put("I", KeyEvent.VK_I);
		keyList.put("J", KeyEvent.VK_J);
		keyList.put("K", KeyEvent.VK_K);
		keyList.put("L", KeyEvent.VK_L);
		keyList.put("M", KeyEvent.VK_M);
		keyList.put("N", KeyEvent.VK_N);
		keyList.put("O", KeyEvent.VK_O);
		keyList.put("P", KeyEvent.VK_P);
		keyList.put("Q", KeyEvent.VK_Q);
		keyList.put("R", KeyEvent.VK_R);
		keyList.put("S", KeyEvent.VK_S);
		keyList.put("T", KeyEvent.VK_T);
		keyList.put("U", KeyEvent.VK_U);
		keyList.put("V", KeyEvent.VK_V);
		keyList.put("W", KeyEvent.VK_W);
		keyList.put("X", KeyEvent.VK_X);
		keyList.put("Y", KeyEvent.VK_Y);
		keyList.put("Z", KeyEvent.VK_Z);
		
		// Special Characters
		keyList.put(" ", KeyEvent.VK_SPACE);
		keyList.put(",", KeyEvent.VK_COMMA);
		keyList.put("-", KeyEvent.VK_MINUS);
		keyList.put(".", KeyEvent.VK_PERIOD);
		keyList.put("/", KeyEvent.VK_SLASH);
		keyList.put(";", KeyEvent.VK_SEMICOLON);
		keyList.put("=", KeyEvent.VK_EQUALS);
		keyList.put("&", KeyEvent.VK_AMPERSAND);
		keyList.put("*", KeyEvent.VK_ASTERISK);
		keyList.put("\"", KeyEvent.VK_QUOTEDBL);
		keyList.put("<", KeyEvent.VK_LESS);
		keyList.put(">", KeyEvent.VK_GREATER);
		keyList.put("{", KeyEvent.VK_BRACELEFT);
		keyList.put("}", KeyEvent.VK_BRACERIGHT);
		keyList.put("@", KeyEvent.VK_AT);
		keyList.put(":", KeyEvent.VK_COLON);
		keyList.put("^", KeyEvent.VK_CIRCUMFLEX);
		keyList.put("$", KeyEvent.VK_DOLLAR);
		keyList.put("€", KeyEvent.VK_EURO_SIGN);
		keyList.put("!", KeyEvent.VK_EXCLAMATION_MARK);
		keyList.put("INVERTED_EXCLAMATION_MARK", KeyEvent.VK_INVERTED_EXCLAMATION_MARK);
		keyList.put("(", KeyEvent.VK_LEFT_PARENTHESIS);
		keyList.put("#", KeyEvent.VK_NUMBER_SIGN);
		keyList.put("+", KeyEvent.VK_PLUS);
		keyList.put(")", KeyEvent.VK_RIGHT_PARENTHESIS);
		keyList.put("_", KeyEvent.VK_UNDERSCORE);
		keyList.put("[", KeyEvent.VK_OPEN_BRACKET);
		keyList.put("\\", KeyEvent.VK_BACK_SLASH);
		keyList.put("]", KeyEvent.VK_CLOSE_BRACKET);
		keyList.put("BACK_QUOTE", KeyEvent.VK_BACK_QUOTE);
		keyList.put("QUOTE", KeyEvent.VK_QUOTE);
		
		// Control Symbols - Left Side of Keyboard
		keyList.put("ESCAPE", KeyEvent.VK_ESCAPE);
		keyList.put("TAB", KeyEvent.VK_TAB);
		keyList.put("CLEAR", KeyEvent.VK_CLEAR);
		keyList.put("SHIFT", KeyEvent.VK_SHIFT);
		keyList.put("CAPS_LOCK", KeyEvent.VK_CAPS_LOCK);
		keyList.put("CONTROL", KeyEvent.VK_CONTROL);
		keyList.put("ALT", KeyEvent.VK_ALT);
		keyList.put("ALT_GRAPH", KeyEvent.VK_ALT_GRAPH);
		keyList.put("WINDOWS", KeyEvent.VK_WINDOWS);
		keyList.put("CONTEXT_MENU", KeyEvent.VK_CONTEXT_MENU);
		
		// Control Symbols - Top of Keyboard
		keyList.put("PRINTSCREEN", KeyEvent.VK_PRINTSCREEN);
		keyList.put("SCROLL_LOCK", KeyEvent.VK_SCROLL_LOCK);
		keyList.put("PAUSE", KeyEvent.VK_PAUSE);
		
		// Control Symbols - beside the right Side of Keyboard
		keyList.put("INSERT", KeyEvent.VK_INSERT);
		keyList.put("DELETE", KeyEvent.VK_DELETE);
		keyList.put("HOME", KeyEvent.VK_HOME);
		keyList.put("END", KeyEvent.VK_END);
		keyList.put("PAGE_UP", KeyEvent.VK_PAGE_UP);
		keyList.put("PAGE_DOWN", KeyEvent.VK_PAGE_DOWN);
		
		// Control Symbols - Right Side of Keyboard
		keyList.put("ENTER", KeyEvent.VK_ENTER);
		keyList.put("BACK_SPACE", KeyEvent.VK_BACK_SPACE);
		
		// Control Symbols - Move Keys
		keyList.put("LEFT", KeyEvent.VK_LEFT);
		keyList.put("UP", KeyEvent.VK_UP);
		keyList.put("RIGHT", KeyEvent.VK_RIGHT);
		keyList.put("DOWN", KeyEvent.VK_DOWN);
		
		// Control Symbols - Special Controls
		keyList.put("CUT", KeyEvent.VK_CUT);
		keyList.put("COPY", KeyEvent.VK_COPY);
		keyList.put("PASTE", KeyEvent.VK_PASTE);
		keyList.put("UNDO", KeyEvent.VK_UNDO);
		
		// Control Symbols and CHaracters from Numpad
		keyList.put("NUM_LOCK", KeyEvent.VK_NUM_LOCK);
		keyList.put("NUMPAD0", KeyEvent.VK_NUMPAD0);
		keyList.put("NUMPAD1", KeyEvent.VK_NUMPAD1);
		keyList.put("NUMPAD2", KeyEvent.VK_NUMPAD2);
		keyList.put("NUMPAD3", KeyEvent.VK_NUMPAD3);
		keyList.put("NUMPAD4", KeyEvent.VK_NUMPAD4);
		keyList.put("NUMPAD5", KeyEvent.VK_NUMPAD5);
		keyList.put("NUMPAD6", KeyEvent.VK_NUMPAD6);
		keyList.put("NUMPAD7", KeyEvent.VK_NUMPAD7);
		keyList.put("NUMPAD8", KeyEvent.VK_NUMPAD8);
		keyList.put("NUMPAD9", KeyEvent.VK_NUMPAD9);
		keyList.put("MULTIPLY", KeyEvent.VK_MULTIPLY);
		keyList.put("ADD", KeyEvent.VK_ADD);
		keyList.put("SEPARATER", KeyEvent.VK_SEPARATER);
		keyList.put("SEPARATOR", KeyEvent.VK_SEPARATOR);
		keyList.put("SUBTRACT", KeyEvent.VK_SUBTRACT);
		keyList.put("DECIMAL", KeyEvent.VK_DECIMAL);
		keyList.put("DIVIDE", KeyEvent.VK_DIVIDE);
		keyList.put("KP_UP", KeyEvent.VK_KP_UP);
		keyList.put("KP_DOWN", KeyEvent.VK_KP_DOWN);
		keyList.put("KP_LEFT", KeyEvent.VK_KP_LEFT);
		keyList.put("KP_RIGHT", KeyEvent.VK_KP_RIGHT);
		
		// Function Keys
		keyList.put("F1", KeyEvent.VK_F1);
		keyList.put("F2", KeyEvent.VK_F2);
		keyList.put("F3", KeyEvent.VK_F3);
		keyList.put("F4", KeyEvent.VK_F4);
		keyList.put("F5", KeyEvent.VK_F5);
		keyList.put("F6", KeyEvent.VK_F6);
		keyList.put("F7", KeyEvent.VK_F7);
		keyList.put("F8", KeyEvent.VK_F8);
		keyList.put("F9", KeyEvent.VK_F9);
		keyList.put("F10", KeyEvent.VK_F10);
		keyList.put("F11", KeyEvent.VK_F11);
		keyList.put("F12", KeyEvent.VK_F12);
		keyList.put("F13", KeyEvent.VK_F13);
		keyList.put("F14", KeyEvent.VK_F14);
		keyList.put("F15", KeyEvent.VK_F15);
		keyList.put("F16", KeyEvent.VK_F16);
		keyList.put("F17", KeyEvent.VK_F17);
		keyList.put("F18", KeyEvent.VK_F18);
		keyList.put("F19", KeyEvent.VK_F19);
		keyList.put("F20", KeyEvent.VK_F20);
		keyList.put("F21", KeyEvent.VK_F21);
		keyList.put("F22", KeyEvent.VK_F22);
		keyList.put("F23", KeyEvent.VK_F23);
		keyList.put("F24", KeyEvent.VK_F24);
		
		// Other
		keyList.put("CANCEL", KeyEvent.VK_CANCEL);
		keyList.put("HELP", KeyEvent.VK_HELP);
		keyList.put("META", KeyEvent.VK_META);
		keyList.put("DEAD_GRAVE", KeyEvent.VK_DEAD_GRAVE);
		keyList.put("DEAD_ACUTE", KeyEvent.VK_DEAD_ACUTE);
		keyList.put("DEAD_CIRCUMFLEX", KeyEvent.VK_DEAD_CIRCUMFLEX);
		keyList.put("DEAD_TILDE", KeyEvent.VK_DEAD_TILDE);
		keyList.put("DEAD_MACRON", KeyEvent.VK_DEAD_MACRON);
		keyList.put("DEAD_BREVE", KeyEvent.VK_DEAD_BREVE);
		keyList.put("DEAD_ABOVEDOT", KeyEvent.VK_DEAD_ABOVEDOT);
		keyList.put("DEAD_DIAERESIS", KeyEvent.VK_DEAD_DIAERESIS);
		keyList.put("DEAD_ABOVERING", KeyEvent.VK_DEAD_ABOVERING);
		keyList.put("DEAD_DOUBLEACUTE", KeyEvent.VK_DEAD_DOUBLEACUTE);
		keyList.put("DEAD_CARON", KeyEvent.VK_DEAD_CARON);
		keyList.put("DEAD_CEDILLA", KeyEvent.VK_DEAD_CEDILLA);
		keyList.put("DEAD_OGONEK", KeyEvent.VK_DEAD_OGONEK);
		keyList.put("DEAD_IOTA", KeyEvent.VK_DEAD_IOTA);
		keyList.put("DEAD_VOICED_SOUND", KeyEvent.VK_DEAD_VOICED_SOUND);
		keyList.put("DEAD_SEMIVOICED_SOUND", KeyEvent.VK_DEAD_SEMIVOICED_SOUND);
		keyList.put("FINAL", KeyEvent.VK_FINAL);
		keyList.put("CONVERT", KeyEvent.VK_CONVERT);
		keyList.put("NONCONVERT", KeyEvent.VK_NONCONVERT);
		keyList.put("ACCEPT", KeyEvent.VK_ACCEPT);
		keyList.put("MODECHANGE", KeyEvent.VK_MODECHANGE);
		keyList.put("KANA", KeyEvent.VK_KANA);
		keyList.put("KANJI", KeyEvent.VK_KANJI);
		keyList.put("ALPHANUMERIC", KeyEvent.VK_ALPHANUMERIC);
		keyList.put("KATAKANA", KeyEvent.VK_KATAKANA);
		keyList.put("HIRAGANA", KeyEvent.VK_HIRAGANA);
		keyList.put("FULL_WIDTH", KeyEvent.VK_FULL_WIDTH);
		keyList.put("HALF_WIDTH", KeyEvent.VK_HALF_WIDTH);
		keyList.put("ROMAN_CHARACTERS", KeyEvent.VK_ROMAN_CHARACTERS);
		keyList.put("PREVIOUS_CANDIDATE", KeyEvent.VK_PREVIOUS_CANDIDATE);
		keyList.put("CODE_INPUT", KeyEvent.VK_CODE_INPUT);
		keyList.put("JAPANESE_KATAKANA", KeyEvent.VK_JAPANESE_KATAKANA);
		keyList.put("JAPANESE_HIRAGANA", KeyEvent.VK_JAPANESE_HIRAGANA);
		keyList.put("JAPANESE_ROMAN", KeyEvent.VK_JAPANESE_ROMAN);
		keyList.put("KANA_LOCK", KeyEvent.VK_KANA_LOCK);
		keyList.put("INPUT_METHOD_ON_OFF", KeyEvent.VK_INPUT_METHOD_ON_OFF);
		keyList.put("AGAIN", KeyEvent.VK_AGAIN);
		keyList.put("STOP", KeyEvent.VK_STOP);
		keyList.put("COMPOSE", KeyEvent.VK_COMPOSE);
		keyList.put("BEGIN", KeyEvent.VK_BEGIN);
		
		// Shift List
		shiftList = new ArrayList<Integer>();
		shiftList.add(KeyEvent.VK_SLASH);
		shiftList.add(KeyEvent.VK_SEMICOLON);
		shiftList.add(KeyEvent.VK_EQUALS);
		shiftList.add(KeyEvent.VK_AMPERSAND);
		shiftList.add(KeyEvent.VK_ASTERISK);
		shiftList.add(KeyEvent.VK_QUOTEDBL);
		shiftList.add(KeyEvent.VK_GREATER);
		shiftList.add(KeyEvent.VK_COLON);
		shiftList.add(KeyEvent.VK_DOLLAR);
		shiftList.add(KeyEvent.VK_EXCLAMATION_MARK);
		shiftList.add(KeyEvent.VK_LEFT_PARENTHESIS);
		shiftList.add(KeyEvent.VK_RIGHT_PARENTHESIS);
		shiftList.add(KeyEvent.VK_QUOTE);
		shiftList.add(KeyEvent.VK_UNDERSCORE);

		altgrList = new ArrayList<Integer>();
		altgrList.add(KeyEvent.VK_BRACELEFT);
		altgrList.add(KeyEvent.VK_BRACERIGHT);
		altgrList.add(KeyEvent.VK_AT);
		altgrList.add(KeyEvent.VK_EURO_SIGN);
		altgrList.add(KeyEvent.VK_OPEN_BRACKET);
		altgrList.add(KeyEvent.VK_BACK_SLASH);
		altgrList.add(KeyEvent.VK_CLOSE_BRACKET);
		
		crtlList = new ArrayList<Integer>();
		crtlList.add(KeyEvent.VK_CUT);
		crtlList.add(KeyEvent.VK_COPY);
		crtlList.add(KeyEvent.VK_PASTE);
		crtlList.add(KeyEvent.VK_UNDO);
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * @return int keyCode
	 */
	protected int getKey(String Sequence) {
		if (keyList.get(Sequence) != null)
			return keyList.get(Sequence);
		else {
			logger.error("Character " + Sequence.toString() + " could not be found. ");
			return keyList.get("#");
		}
	}
	

	/**
	 * @return int keyCode
	 */
	protected int getKey(Character Sequence) {
		if (keyList.get(Sequence.toString()) != null)
			return keyList.get(Sequence.toString());
		else {
			logger.error("Character " + Sequence.toString() + " could not be found. ");
			return keyList.get("#");
		}
	}
	

	/**
	 * FIXME
	 * @return boolean
	 */
	protected boolean getShift(int key) {
		return shiftList.contains(key);
	}
	

	/**
	 * FIXME
	 * @return boolean
	 */
	protected boolean getCrtl(int key) {
		return crtlList.contains(key);
	}
	

	/**
	 * FIXME
	 * @return boolean
	 */
	protected boolean getAltGr(int key) {
		return altgrList.contains(key);
	}

}
