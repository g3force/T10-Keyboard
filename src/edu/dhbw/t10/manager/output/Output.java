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
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Stack;

import org.apache.log4j.Logger;

import edu.dhbw.t10.type.keyboard.key.Key;


/**
 * 
 * This class provides the functionallity of printing Strings via sending Key Strokes to the system.
 * Letters, big letters and numbers are converted directly to their own java.awt.event.KeyEvent constant, which is sent.
 * All other symbols are written via their Unicode.
 * FIXME WIndows support untested
 * 
 * Control symbols are sent via their java.awt.event.KeyEvent constant
 * 
 * TODO Daniel Get several Control symbols and combine them to a key combination
 * TODO Daniel Get last active window and write there
 * @author Andres
 * 
 */
public class Output {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Output.class);
	public static final int			UNKNOWN	= 0;
	public static final int			LINUX		= 1;
	public static final int			WINDOWS	= 2;


	private static int				os;
	private static int				delay		= 0;

	private static Output			instance;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private Output() throws UnknownOSException {
		String osName = System.getProperty("os.name");
		if (osName.equals("Linux"))
			os = LINUX;
		else if (osName.equals("Windows"))
			os = WINDOWS;
		else {
			os = UNKNOWN;
			throw new UnknownOSException("Unknown Operating System: " + osName);
		}
		logger.info("OS: " + osName);

	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	

	public boolean printChar(Key c) {
		return printString(c.getKeycode(), c.getType());
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
		
		// if (charSequence.charAt(0) == '\\' && charSequence.charAt(length - 1) == '\\'
		// && !charSequence.substring(0).startsWith("\\U+")) {
		if (type == Key.CONTROL) {
			sendKey(getKeyCode(charSequence.substring(1, length - 1)));
			logger.info("Control Symbol printed: " + charSequence);
		} else if (type == Key.UNICODE) {
			sendUnicode(charSequence);
			logger.info("Unicode Symbol printed: " + charSequence);
		} else {
			ArrayList<Integer> unicodeStart = extractUnicode(charSequence);
			for (int i = 0; i < length; i++) {
				// Unterscheidung zwischen Buchstaben (und Zahlen) und Unicode Zeichen
				if (!unicodeStart.isEmpty() && unicodeStart.get(0) == i) { // Unicode Aufruf unter Linux
					sendUnicode(charSequence.substring(i, i + 7));
					unicodeStart.remove(0);
				} else if (Character.isUpperCase(charSequence.charAt(i)) == true) { // Big Letters
					sendKey(getKeyCode(charSequence.substring(i, i + 1)), 1);
				} else { // Small letters
					sendKey(getKeyCode(charSequence.substring(i, i + 1)));
				}
			}
			logger.info("String printed: " + charSequence);
		}
		return true;
	}
	

	public boolean markChar(int length) {
		if (length <= 0)
			return false;
		else {
			sendKey(KeyEvent.VK_SHIFT, 0, 1);
			
			for (int i = 0; i < length; i++) {
				sendKey(KeyEvent.VK_LEFT);
			}
			sendKey(KeyEvent.VK_SHIFT, 0, 1);
			return true;
		}
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
		// TODO Daniel erkenne Sonderzeichen und Konvertiere das in Unicode
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
			logger.error("getKeyCode: Security: " + code);
			return 0;
		} catch (NoSuchFieldException err) {
			logger.error("getKeyCode: No Such Field: " + code);
			// TODO Umlaute und andere Zeichen in Unicode Konvertieren
			return 0;
		} catch (IllegalArgumentException err) {
			logger.error("getKeyCode: Illegal Argument: " + code);
			return 0;
		} catch (IllegalAccessException err) {
			logger.error("getKeyCode: Illegal Access: " + code);
			return 0;
		}
		
	}
	

	private boolean sendUnicode(String uni) {
		if (uni.length() != 8 || !uni.substring(0, 3).equals("\\U+") || !uni.substring(7, 8).equals("\\")) {
			logger.error("UNICODE wrong format: length:" + uni.length() + ", Start: " + uni.substring(0, 2) + ", End: "
					+ uni.substring(7, 8));
			return false;
		}
		if (os == 1) {
			sendKey(KeyEvent.VK_CONTROL, 0, 1);
			sendKey(KeyEvent.VK_SHIFT, 0, 1);
			sendKey(KeyEvent.VK_U, 0);
			sendKey(KeyEvent.VK_SHIFT, 0, 2);
			sendKey(KeyEvent.VK_CONTROL, 0, 2);
			sendKey(getKeyCode(uni.substring(3, 4).toLowerCase()), 0);
			sendKey(getKeyCode(uni.substring(4, 5).toLowerCase()), 0);
			sendKey(getKeyCode(uni.substring(5, 6).toLowerCase()), 0);
			sendKey(getKeyCode(uni.substring(6, 7).toLowerCase()), 0);
			sendKey(KeyEvent.VK_ENTER, 0);
		} else if (os == 2) {
			try {
				// Convertion from HexaNumber as String to Decimal Number as String (without leading zeros)
				String uniDecimal;
				
				Integer uninumber = Integer.decode("0x" + uni.substring(3, 7));
				uniDecimal = Integer.toString(uninumber, 10);

				boolean num_lock;

				// Chekcs if Num_Lock is on an turns it on, if necessary
				Toolkit tool = Toolkit.getDefaultToolkit();
				num_lock = tool.getLockingKeyState(KeyEvent.VK_NUM_LOCK);
				logger.info((num_lock ? "Num Lock is on" : "Num Lock is off"));
				if (!num_lock) { // If Num_Lock is off, turn it on
					sendKey(KeyEvent.VK_NUM_LOCK, 0);
				}

				// Sending KeyCombination for Unicode input to Windows...
				sendKey(KeyEvent.VK_ALT, 0, 1);
				// Sends leading zeros to the system. Windows interpret only 5 digit long values correct.
				for (int i = 5; i > uniDecimal.length(); i--)
					sendKey(KeyEvent.VK_0, 0);
				for (int i = 0; i < uniDecimal.length(); i++) {
					sendKey(getKeyCode(uniDecimal.substring(i, i + 1)), 0);
				}
				sendKey(KeyEvent.VK_ALT, 0, 2);


			} catch (UnsupportedOperationException err) {
				logger.error("Unsopported Operation: Check Num_Lock state");
				// In Linux it throws always this Exception, but here it isn't needed
				// TODO test it in Windows, where it is needed
			} catch (NumberFormatException err) {
				logger.error("Wrong number format:" + uni.substring(3, 7));
			}
		}
		return true;
	}
	

	private boolean sendKey(int key) {
		return sendKey(key, 0);
	}
	

	private boolean sendKey(int key, int function) {
		return sendKey(key, function, 0);
	}
	

	// TODO Input argument is a List of Keys not a single one...
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
				keyRobot.delay(delay);
			
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
				// case 2: { // Crtl function
				// keyRobot.keyPress(KeyEvent.VK_CONTROL);
				// keyRobot.keyPress(key);
				// keyRobot.keyRelease(key);
				// keyRobot.keyRelease(KeyEvent.VK_CONTROL);
				// }
				// break;
				// case 3: { // Alt function
				// keyRobot.keyPress(KeyEvent.VK_ALT);
				// keyRobot.keyPress(key);
				// keyRobot.keyRelease(key);
				// keyRobot.keyRelease(KeyEvent.VK_ALT);
				// }
				// break;
				// case 4: { // Alt Gr function
				// keyRobot.keyPress(KeyEvent.VK_ALT_GRAPH);
				// keyRobot.keyPress(key);
				// keyRobot.keyRelease(key);
				// keyRobot.keyRelease(KeyEvent.VK_ALT_GRAPH);
				// }
				// break;
				// case 5: { // Super function
				// keyRobot.keyPress(KeyEvent.VK_WINDOWS);
				// keyRobot.keyPress(key);
				// keyRobot.keyRelease(key);
				// keyRobot.keyRelease(KeyEvent.VK_WINDOWS);
				// }
				// break;
				case 6: { // Kombination
					Stack<Integer> combi = new Stack<Integer>();
					// Input ist Liste... von vorne nach hinten wird diese abgearbeitet und das aktuelle Element ausgegeben,
					// in den Stack gespeichert und gelöscht... Wenn die Liste leer ist wird das oberste Element des STacks
					// ausgegeben und gelöscht bis der Stack leer ist...
					for (Integer i : new Integer[] { 0, 0, 0 }) {
						keyRobot.keyPress((int) i);
						combi.push(i);
					}
					while (!combi.isEmpty()) {
						Integer i = combi.pop();
						keyRobot.keyRelease((int) i);
					}
				}
					break;
			}
			logger.debug("sendKey: Key sent: " + key);
			return true;
		} catch (AWTException e) {
			logger.error("sendKey: AWT: " + key);
			return false;
		}
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public static Output getInstance() {
		if (instance == null) {
			try {
				instance = new Output();
			} catch (UnknownOSException err) {
				logger.error(err.getMessage());
				// TODO was passiert dann???
			}
		}
		return instance;
	}
}
