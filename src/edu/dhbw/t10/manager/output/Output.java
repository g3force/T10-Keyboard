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
 * TODO DanielAl Get several Control symbols and combine them to a key combination
 * TODO DanielAl Get last active window and write there
 * @author DanielAl
 * 
 */
public class Output {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Output.class);
	// OS Constants
	public static final int			UNKNOWN	= 0;
	public static final int			LINUX		= 1;
	public static final int			WINDOWS	= 2;
	// SendKey Function Constants
	public static final int			TYPE		= 0;
	public static final int			PRESS		= 1;
	public static final int			RELEASE	= 2;
	public static final int			COMBI		= 3;
	public static final int			SHIFT		= 10;


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
		if (length <= 0)
			return false;
		
		if (type == Key.CONTROL) {
			sendKey(getKeyCode(charSequence.substring(1, length - 1)));
			logger.info("Control Symbol printed: " + charSequence);
			return true;
		} else if (type == Key.UNICODE) {
			sendUnicode(charSequence);
			logger.info("Unicode Symbol printed: " + charSequence);
			return true;
		} else if (type == Key.UNKNOWN || type == Key.CHAR) { // print String... first convert Unicodes and then print all
																				// chars and unicodes...
			ArrayList<Integer> unicodeStart = extractUnicode(charSequence);
			for (int i = 0; i < length; i++) {
				// Unterscheidung zwischen Buchstaben (und Zahlen) und Unicode Zeichen
				if (!unicodeStart.isEmpty() && unicodeStart.get(0) == i) { // Unicode Aufruf unter Linux
					sendUnicode(charSequence.substring(i, i + 7));
					unicodeStart.remove(0);
				} else if (Character.isUpperCase(charSequence.charAt(i)) == true) { // Big Letters
					sendKey(getKeyCode(charSequence.substring(i, i + 1)), SHIFT);
				} else { // Small letters
					sendKey(getKeyCode(charSequence.substring(i, i + 1)), TYPE);
				}
			}
			logger.info("String printed: " + charSequence);
			return true;
		} else {
			logger.info("Undefined type for printing:" + type);
			return false;
		}
	}
	

	public boolean markChar(int length) {
		if (length <= 0)
			return false;
		else {
			sendKey(KeyEvent.VK_SHIFT, PRESS);
			
			for (int i = 0; i < length; i++) {
				sendKey(KeyEvent.VK_LEFT, TYPE);
			}
			sendKey(KeyEvent.VK_SHIFT, RELEASE);
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
		// TODO DanielAl erkenne Sonderzeichen und Konvertiere das in Unicode
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
			return KeyEvent.VK_UNDEFINED;
		} catch (NoSuchFieldException err) {
			logger.error("getKeyCode: No Such Field: " + code);
			// TODO Umlaute und andere Zeichen in Unicode Konvertieren
			return KeyEvent.VK_UNDEFINED;
		} catch (IllegalArgumentException err) {
			logger.error("getKeyCode: Illegal Argument: " + code);
			return KeyEvent.VK_UNDEFINED;
		} catch (IllegalAccessException err) {
			logger.error("getKeyCode: Illegal Access: " + code);
			return KeyEvent.VK_UNDEFINED;
		}
		
	}
	

	private boolean sendUnicode(String uni) {
		if (uni.length() != 8 || !uni.substring(0, 3).equals("\\U+") || !uni.substring(7, 8).equals("\\")) {
			logger.error("UNICODE wrong format; length: " + uni.length());
			return false;
		}
		if (os == LINUX) {
			sendKey(KeyEvent.VK_CONTROL, PRESS);
			sendKey(KeyEvent.VK_SHIFT, PRESS);
			sendKey(KeyEvent.VK_U, TYPE);
			sendKey(KeyEvent.VK_SHIFT, RELEASE);
			sendKey(KeyEvent.VK_CONTROL, RELEASE);
			sendKey(getKeyCode(uni.substring(3, 4).toLowerCase()), TYPE);
			sendKey(getKeyCode(uni.substring(4, 5).toLowerCase()), TYPE);
			sendKey(getKeyCode(uni.substring(5, 6).toLowerCase()), TYPE);
			sendKey(getKeyCode(uni.substring(6, 7).toLowerCase()), TYPE);
			sendKey(KeyEvent.VK_ENTER, TYPE);
		} else if (os == WINDOWS) {
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
					sendKey(KeyEvent.VK_NUM_LOCK, TYPE);
				}

				// Sending KeyCombination for Unicode input to Windows...
				sendKey(KeyEvent.VK_ALT, PRESS);
				// Sends leading zeros to the system. Windows interpret only 5 digit long values correct.
				for (int i = 5; i > uniDecimal.length(); i--)
					sendKey(KeyEvent.VK_0, TYPE);
				for (int i = 0; i < uniDecimal.length(); i++) {
					sendKey(getKeyCode(uniDecimal.substring(i, i + 1)), TYPE);
				}
				sendKey(KeyEvent.VK_ALT, RELEASE);

			} catch (UnsupportedOperationException err) {
				logger.error("Unsupported Operation: Check Num_Lock state");
				// In Linux it throws always this Exception, but here it isn't needed
				// TODO test it in Windows, where it is needed
			} catch (NumberFormatException err) {
				logger.error("Wrong number format:" + uni.substring(3, 7));
			}
		} else {
			logger.error("OS not supported: Unicode");
			return false;
		}
		return true;
	}
	

	private boolean sendKey(int key) {
		return sendKey(key, TYPE);
	}
	



	// TODO Input argument is a List of Keys not a single one...
	/**
	 * 
	 * Send Key Codes to the System with a Robot and ava.awt.event.KeyEvent constants
	 * 
	 * Use function to use Shift, ... functionality
	 * for function definitions look at constants...
	 * 
	 * Hint: keyPress('ö') tested and it don't work
	 * 
	 * @param key, function
	 * @return
	 */
	private boolean sendKey(int key, int function) {
		if (key == 0) {
			logger.error("sendKey: UNKNOWN Key");
			return false;
		}
		try {
			Robot keyRobot = new Robot();
			keyRobot.delay(delay);
			// requestFocus();
			switch (function) {
				case TYPE:
					keyRobot.keyPress(key);
					keyRobot.keyRelease(key);
					break;
				case PRESS:
					keyRobot.keyPress(key);
					break;
				case RELEASE:
					keyRobot.keyRelease(key);
					break;
				case COMBI: // Kombination
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
					break;
				case SHIFT: // Shift function
					keyRobot.keyPress(KeyEvent.VK_SHIFT);
					keyRobot.keyPress(key);
					keyRobot.keyRelease(key);
					keyRobot.keyRelease(KeyEvent.VK_SHIFT);
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
				logger.fatal(err.getMessage());
				System.exit(-1);
			}
		}
		return instance;
	}
}
