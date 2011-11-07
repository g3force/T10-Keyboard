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

import edu.dhbw.t10.helper.StringHelper;
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


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public Output() throws UnknownOSException {
		String osName = System.getProperty("os.name");
		/*
		 * Possible Names:
		 * Windows XP ??
		 * Windows 7
		 * Linux
		 */
		if (osName.equals("Linux"))
			os = LINUX;
		else if (osName.startsWith("Windows"))
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
	 * @author DanielAl
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
			ArrayList<Integer> unicodeStart = StringHelper.extractUnicode(charSequence);
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
	

	private Integer getKeyCode(String code) {
		return getKeyCode(code, 0);
	}


	/**
	 * 
	 * Converts a Stringcode into a Constant of the KeyEvent class via Reflection.
	 * These constants could be used for sending Keys.
	 * 
	 * @param code
	 * @param i
	 * @return
	 */
	private Integer getKeyCode(String code, int type) {
		Field f;
		try {
			if (type == 0) {
				f = KeyEvent.class.getField("VK_" + code.toUpperCase());
				f.setAccessible(true);
				return (Integer) f.get(null);
			} else if (type == 1) {
				f = KeyEvent.class.getField("VK_NUMPAD" + code.toUpperCase());
				f.setAccessible(true);
				return (Integer) f.get(null);
			} else
				return KeyEvent.VK_UNDEFINED;
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
	

	/**
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param uni
	 * @return
	 * @author DanielAl
	 */
	private boolean sendUnicode(String uni) {
		if (uni.length() != 8 || !uni.substring(0, 3).equals("\\U+") || !uni.substring(7, 8).equals("\\")) {
			logger.error("UNICODE wrong format; length: " + uni.length());
			return false;
		}
		
		char[] uniArr = uni.substring(3, 7).toLowerCase().toCharArray();

		if (os == LINUX) {
			sendKey(KeyEvent.VK_CONTROL, PRESS);
			sendKey(KeyEvent.VK_SHIFT, PRESS);
			sendKey(KeyEvent.VK_U, TYPE);
			sendKey(KeyEvent.VK_SHIFT, RELEASE);
			sendKey(KeyEvent.VK_CONTROL, RELEASE);
			sendKey(getKeyCode(uniArr[0] + ""), TYPE);
			sendKey(getKeyCode(uniArr[1] + ""), TYPE);
			sendKey(getKeyCode(uniArr[2] + ""), TYPE);
			sendKey(getKeyCode(uniArr[3] + ""), TYPE);
			sendKey(KeyEvent.VK_ENTER, TYPE);
		} else if (os == WINDOWS) {
			// for the Windows Unicode Hexadecimal Input the following Registry data is needed, to install use install.reg
			//[HKEY_CURRENT_USER\Control Panel\Input Method]:	"EnableHexNumpad"="1"

			try {
				boolean num_lock;

				// Checks the status of Num_Lock
				Toolkit tool = Toolkit.getDefaultToolkit();
				num_lock = tool.getLockingKeyState(KeyEvent.VK_NUM_LOCK);
				logger.info((num_lock ? "Num Lock is on" : "Num Lock is off"));
				// If Num_Lock is off, turn it on
				if (!num_lock) {
					sendKey(KeyEvent.VK_NUM_LOCK, TYPE);
				}

				// Sending KeyCombination for Unicode input to Windows...
				sendKey(KeyEvent.VK_ALT, PRESS);
				// FIXME ADD Symbol really needed??
				sendKey(KeyEvent.VK_ADD, TYPE);
				// send the Hexa Decimal number with digits as a numpad key and the chars from the normal keyboard...
				sendKey(Character.isDigit(uniArr[0]) ? getKeyCode(uniArr[0] + "", 1) : getKeyCode(uniArr[0] + "", 0), TYPE);
				sendKey(Character.isDigit(uniArr[1]) ? getKeyCode(uniArr[1] + "", 1) : getKeyCode(uniArr[1] + "", 0), TYPE);
				sendKey(Character.isDigit(uniArr[2]) ? getKeyCode(uniArr[2] + "", 1) : getKeyCode(uniArr[2] + "", 0), TYPE);
				sendKey(Character.isDigit(uniArr[3]) ? getKeyCode(uniArr[3] + "", 1) : getKeyCode(uniArr[3] + "", 0), TYPE);
				
				sendKey(KeyEvent.VK_ALT, RELEASE);
				
				// If Num_Lock was off, turn it off again, so that you have the same status as before...
				if (!num_lock) {
					sendKey(KeyEvent.VK_NUM_LOCK, TYPE);
				}

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
	

}
