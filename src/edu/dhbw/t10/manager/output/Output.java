/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): Andres
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.output;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.dhbw.t10.type.keyboard.Key;


/**
 * 
 * This class provides the functionallity of printing Strings via sending Key Strokes to the system.
 * Letters, big letters and numbers are converted directly to their own java.awt.event.KeyEvent constant, which is sent.
 * All other symbols are written via their Unicode.
 * FIXME only Linux Support at this moment
 * 
 * Control symbols are sent via their java.awt.event.KeyEvent constant
 * 
 * TODO Get several Control symbols and combine them to a key combination
 * TODO Get last active window and write there
 * @author Andres
 * 
 */
public class Output {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Output.class);
	private static int				os			= 1;
	
	private static Output			instance;

	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private Output() {
		
	}
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public static Output getInstance() {
		if (instance == null) {
			instance = new Output();
		}
		return instance;
	}


	public boolean printChar(Key c) {
		return printString(c.getText(), c.getType());
	}


	public boolean printString(String charSequence) {
		return printString(charSequence, 0);
	}


	/**
	 * 
	 * Transoforms a given CodeSequence from OutputManager to KeyEvent-Constants and sends it to the System
	 * 
	 * @param c
	 * @return
	 */
	public boolean printString(String charSequence, int type) {
		int length = charSequence.length();
		int keyCode = 0;

		if (charSequence.charAt(0) == '\\' && charSequence.charAt(length - 1) == '\\'
				&& !charSequence.substring(0).startsWith("\\U+")) {
			keyCode = this.getKeyCode(charSequence.substring(1, length - 1));
			this.sendKey(keyCode);
			logger.info("Control Symbol printed: " + charSequence);
		} else {
			ArrayList<Integer> unicodeStart = extractUnicode(charSequence);
			for (int i = 0; i < length; i++) {
				// Unterscheidung zwischen Buchstaben (und Zahlen) und Unicode Zeichen
				if (!unicodeStart.isEmpty() && unicodeStart.get(0) == i) { // Unicode Aufruf unter Linux
					if (os == 1) {
						this.sendKey(0, 6);
						keyCode = this.getKeyCode(charSequence.substring(i + 3, i + 4));
						this.sendKey(keyCode, 6);
						keyCode = this.getKeyCode(charSequence.substring(i + 4, i + 5));
						this.sendKey(keyCode, 6);
						keyCode = this.getKeyCode(charSequence.substring(i + 5, i + 6));
						this.sendKey(keyCode, 6);
						keyCode = this.getKeyCode(charSequence.substring(i + 6, i + 7));
						this.sendKey(keyCode, 6);
						this.sendKey(KeyEvent.VK_ENTER, 6);
						i += 7;
						unicodeStart.remove(0);
					} else if (os == 2) {
						keyCode = 0;// FIXME Convertion from Unicode HexaString to Decimal
						this.sendKey(0, 7);
						i += 7;
						unicodeStart.remove(0);
					}
				} else if (Character.isUpperCase(charSequence.charAt(i)) == true) { // Big Letters
					keyCode = this.getKeyCode(charSequence.substring(i, i + 1));
					this.sendKey(keyCode, 1);
				} else { // Small letters
					keyCode = this.getKeyCode(charSequence.substring(i, i + 1));
					this.sendKey(keyCode);
				}
			}
			logger.info("String printed: " + charSequence);
		}
		return true;
	}
	

	public boolean deleteChar(int length) {
		if (length <= 0)
			return false;
		else {
			for (int i = 0; i < length; i++) {
				this.sendKey(KeyEvent.VK_BACK_SPACE);
			}
			return true;
		}
	}
	

	public boolean markChar(int length) {
		if (length <= 0)
			return false;
		else {
			this.sendKey(KeyEvent.VK_SHIFT, 0, 1);
			
			for (int i = 0; i < length; i++) {
				this.sendKey(KeyEvent.VK_LEFT);
			}
			this.sendKey(KeyEvent.VK_SHIFT, 0, 1);
			
			return true;
		}
	}
	
	public void unMark() {
		this.sendKey(KeyEvent.VK_RIGHT);
	}


	/**
	 * 
	 * Find a Unicode in a given String and returns a List with the indices
	 * 
	 * @param sequence
	 * @return
	 */
	private ArrayList<Integer> extractUnicode(String sequence) {
		ArrayList<Integer> unicodeStart = new ArrayList<Integer>();
		int help = 0;
		// TODO erkenne Sonderzeichen und Konvertiere das in Unicode
		while (help < sequence.length()) {
			if (sequence.substring(help).startsWith("\\U+")) {
				help = sequence.indexOf("\\U+", help);
				unicodeStart.add(help);
				help += 7;
			} else
				help++;
		}

		return unicodeStart;
	}
	

	/**
	 * 
	 * Converts a Stringcode into a Constant of the KeyEvent class via Reflection.
	 * These constants could be used for sending Keys.
	 * 
	 * @param code
	 * @return
	 */
	private Integer getKeyCode(String code) {
		Field f;
		try {
			f = KeyEvent.class.getField("VK_" + code.toUpperCase());
			f.setAccessible(true);
			return (Integer) f.get(null);
		} catch (SecurityException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
			return 0;
		} catch (NoSuchFieldException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
			// TODO Umlaute und andere Zeichen in Unicode Konvertieren
			return 0;
		} catch (IllegalArgumentException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
			return 0;
		} catch (IllegalAccessException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
			return 0;
		}
		
	}


	private boolean sendKey(int key) {
		return sendKey(key, 0);
	}


	private boolean sendKey(int key, int function) {
		return sendKey(key, function, 0);
	}
	

	/**
	 * 
	 * Send Key Codes to the System with a Robot and ava.awt.event.KeyEvent constants
	 * 
	 * Use function to use Shift, ... functionality
	 * 0: without; 1: Shift; 2: Control; 3: Alt; 4: Alt Gr; 5: Super
	 * 
	 * @param key, function, hold
	 * @return
	 */
	private boolean sendKey(int key, int function, int hold) {
		if (key == 0 && function != 6) {
			return false;
		}
		try {
			Robot keyRobot = new Robot();
			if (hold == 0)
				keyRobot.delay(200);
			
			switch (function) {
				case 0: {
					if (hold == 0 || hold == 1)
						keyRobot.keyPress(key);
					if (hold == 0 || hold == 2)
						keyRobot.keyRelease(key);
				}
					break;
				case 1: { // Shift function
					keyRobot.keyPress(KeyEvent.VK_SHIFT);
					keyRobot.keyPress(key);
					keyRobot.keyRelease(key);
					keyRobot.keyRelease(KeyEvent.VK_SHIFT);
				}
					break;
				case 2: { // Crtl function
					keyRobot.keyPress(KeyEvent.VK_CONTROL);
					keyRobot.keyPress(key);
					keyRobot.keyRelease(key);
					keyRobot.keyRelease(KeyEvent.VK_CONTROL);
				}
					break;
				case 3: { // Alt function
					keyRobot.keyPress(KeyEvent.VK_ALT);
					keyRobot.keyPress(key);
					keyRobot.keyRelease(key);
					keyRobot.keyRelease(KeyEvent.VK_ALT);
				}
					break;
				case 4: { // Alt Gr function
					keyRobot.keyPress(KeyEvent.VK_ALT_GRAPH);
					keyRobot.keyPress(key);
					keyRobot.keyRelease(key);
					keyRobot.keyRelease(KeyEvent.VK_ALT_GRAPH);
				}
					break;
				case 5: { // Super function
					keyRobot.keyPress(KeyEvent.VK_WINDOWS);
					keyRobot.keyPress(key);
					keyRobot.keyRelease(key);
					keyRobot.keyRelease(KeyEvent.VK_WINDOWS);
				}
					break;
				case 6: { // Unicode Linux
					if (key == 0) {
						keyRobot.keyPress(KeyEvent.VK_CONTROL);
						keyRobot.keyPress(KeyEvent.VK_SHIFT);
						keyRobot.keyPress(KeyEvent.VK_U);
						keyRobot.keyRelease(KeyEvent.VK_U);
						keyRobot.keyRelease(KeyEvent.VK_SHIFT);
						keyRobot.keyRelease(KeyEvent.VK_CONTROL);
					} else { // 4 Ziffern und ein ENTER
						keyRobot.keyPress(key);
						keyRobot.keyRelease(key);
					}
				}
					break;
				case 7: { // Unicode Windows
					keyRobot.keyPress(KeyEvent.VK_ALT);
					
					// Keycode erhalten und jede Ziffer ausgeben
					
					keyRobot.keyRelease(KeyEvent.VK_ALT);
				}
					break;
			}
			return true;
		} catch (AWTException e) {
			e.printStackTrace();
			return false;
		}
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
