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
 * This class provides the functionallity of printing Strings via sending Key Strokes to the system.<br>
 * Letters, big letters and numbers are converted directly to their own java.awt.event.KeyEvent constant, which is sent.<br>
 * All other symbols are written via their Unicode.<br>
 * 
 * Control symbols are sent via their java.awt.event.KeyEvent constant<br>
 * FIXME Windows support untested
 * 
 * 
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

	private static int				delay		= 0;
	// 0 represents UNKNOWN OS, 1 Linux, 2 any Windows
	private static int				os;
	// Stack for Key combination. See Method: printCombi(Button b)
	private Stack<Integer>			combi;
	// Robot for sending Keys to the system - used in sendKey()
	private Robot						keyRobot;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Constructor for this class with no parameters<br>
	 * The Operating System is set. This is important for the sendUnicode(String) method, because the input of Unicode
	 * Symbols differs in the OS. <br>
	 * Possible OS Names: Windows XP (?), Windows 7, Linux<br>
	 * 
	 * @throws UnknownOSException
	 * @author DanielAl
	 * 
	 */
	protected Output() throws UnknownOSException {
		String osName = System.getProperty("os.name");
		logger.info("OS: " + osName);
		if (osName.equals("Linux"))
			os = LINUX;
		else if (osName.startsWith("Windows"))
			os = WINDOWS;
		else {
			os = UNKNOWN;
			throw new UnknownOSException("Unsupported Operating System: " + osName);
		}
		try {
			keyRobot = new Robot();
			logger.debug("Output: Robot initialized");
		} catch (AWTException err) {
			logger.error("sendKey: AWTException: " + err.getMessage());
		}
		combi = new Stack<Integer>();
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Calls the printString method with the Keycode and the Type of c.<br>
	 * 
	 * @param Key c
	 * @return booelan
	 * @author DanielAl
	 */
	protected boolean printChar(Key c) {
		return printString(c.getKeycode(), c.getType());
	}
	

	/**
	 * 
	 * Switch with type over different sendKey calls. <br>
	 * - Key.CONTROL is used for Control Symbols like Enter or Space. <br>
	 * - Key.UNICODE is used for a Unicode Sequence. <br>
	 * - Key.CHAR is sed for normal chars. <br>
	 * - Key.UNKNOWN //TODO for what is this used? <br>
	 * The Key.CHAR type differntiate between Big, Small an Unicode Letters...<br>
	 * Converts a char with getKeyCode to a Key.Constant
	 * 
	 * @param String charSequence, int type
	 * @return boolean
	 * @author DanielAl
	 */
	protected boolean printString(String charSequence, int type) {
		int length = charSequence.length();
		if (length <= 0)
			return false;
		
		switch(type){
			// Print Control Symbol, like ENTER or SPACEm,,,höll hlööp ho
			case Key.CONTROL: 
				sendKey(getKeyCode(charSequence.substring(1, length - 1)));
				logger.info("Control Symbol printed: " + charSequence);
				break;
			case Key.UNICODE:
				sendUnicode(charSequence);
				logger.info("Unicode Symbol printed: " + charSequence);
				break;
			case Key.UNKNOWN: // FIXME
			case Key.CHAR:
				// Get the starter Positions of Unicodes in a String...
				charSequence = StringHelper.convertToUnicode(charSequence);
				ArrayList<Integer> unicodeStart = StringHelper.extractUnicode(charSequence);
				
				for (int i = 0; i < length; i++) {
					// Unicode Zeichen
					if (!unicodeStart.isEmpty() && unicodeStart.get(0) == i) { 
						sendUnicode(charSequence.substring(i, i + 7));
						unicodeStart.remove(0);
						// Big Letters
					} else if (Character.isUpperCase(charSequence.charAt(i)) == true) {
						sendKey(getKeyCode(charSequence.substring(i, i + 1)), SHIFT);
						// Small letters
					} else {
						sendKey(getKeyCode(charSequence.substring(i, i + 1)), TYPE);
					}
				}
				logger.info("String printed: " + charSequence);
					break;
			// No correct type can't be handeld...
			default: 
				logger.info("Undefined type for printing:" + type);
				return false;
		}
		return true;
	}
	

	/**
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param Button b
	 * @return boolean
	 * @author DanielAl
	 */
	protected boolean printCombi(ArrayList<Key> b) {
		for (Key key : b) {
			sendKey(convertKeyCode(key.getKeycode(), COMBI));
		}
		sendKey(0, COMBI);
		logger.info("Key Combi printed");
		return true;
	}


	/**
	 * Calls getKeyCode(code, 0)
	 * @param String code
	 * @return Integer
	 * @author DanielAl
	 */
	private Integer getKeyCode(String code) {
		return convertKeyCode(code, 0);
	}
	

	/**
	 * Converts a Stringcode into a Constant of the KeyEvent class via Reflection.<br>
	 * These constants could be used for sending Keys.<br>
	 * The type parameter is for diefferentiate a number to be a normal Keynumber oder a NUMPAD Number.<br>
	 * <br>
	 * 
	 * Exceptions SecurityException, NoSuchFieldException,IllegalArgumentException, IllegalAccessException which are
	 * thrown by reflection returned the KeyEvent.UNKNOWN
	 * 
	 * @param String code, int type
	 * @return Integer
	 */
	private Integer convertKeyCode(String code, int type) {
		Field f;
		try {
			switch (type){
				case 0:
					f = KeyEvent.class.getField("VK_" + code.toUpperCase());
					f.setAccessible(true);
					return (Integer) f.get(null);
				case 1: 
					f = KeyEvent.class.getField("VK_NUMPAD" + code.toUpperCase());
					f.setAccessible(true);
					return (Integer) f.get(null);
				default:		
					return KeyEvent.VK_UNDEFINED;
			}
		} catch (SecurityException err) {
			logger.error("getKeyCode: Security: " + code);
			return KeyEvent.VK_UNDEFINED;
		} catch (NoSuchFieldException err) {
			logger.error("getKeyCode: No Such Field: " + code);
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
	 * Sends a Unicode in the Format \U+XXXX\ to the System by using a System specific Key combination. <br>
	 * Windows and Linux are supported.<br>
	 * For Windows compability a Registry hack is necessary. Use the install.reg to enable HexaDecimal Unicode Input in
	 * Windows and restart your System. <br>
	 * 
	 * @param String uni
	 * @return boolean
	 * @author DanielAl
	 */
	private boolean sendUnicode(String uni) {
		// Chekcs for the correct Unicode length, begin and end
		if (uni.length() != 8 || !uni.substring(0, 3).equals("\\U+") || !uni.substring(7, 8).equals("\\")) {
			logger.error("UNICODE wrong format; length: " + uni.length());
			return false;
		}
		// Extract the Unicode Hexadecimal digit from the surrounding meta symbols (\\u+FFFF\\)
		char[] uniArr = uni.substring(3, 7).toLowerCase().toCharArray();

		switch (os) {
			case LINUX:
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
				return true;

			case WINDOWS:
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
				sendKey(Character.isDigit(uniArr[0]) ? convertKeyCode(uniArr[0] + "", 1) : convertKeyCode(uniArr[0] + "", 0), TYPE);
				sendKey(Character.isDigit(uniArr[1]) ? convertKeyCode(uniArr[1] + "", 1) : convertKeyCode(uniArr[1] + "", 0), TYPE);
				sendKey(Character.isDigit(uniArr[2]) ? convertKeyCode(uniArr[2] + "", 1) : convertKeyCode(uniArr[2] + "", 0), TYPE);
				sendKey(Character.isDigit(uniArr[3]) ? convertKeyCode(uniArr[3] + "", 1) : convertKeyCode(uniArr[3] + "", 0), TYPE);
				
				sendKey(KeyEvent.VK_ALT, RELEASE);
				
				// If Num_Lock was off, turn it off again, so that you have the same status as before...
				if (!num_lock) {
					sendKey(KeyEvent.VK_NUM_LOCK, TYPE);
				}
			} catch (UnsupportedOperationException err) {
					logger.error("Unsupported Operation: Check Num_Lock state; can't write Unicode" + uniArr.toString());
			}
				return true;
			default:
				logger.error("OS not supported: Unicode");
				return false;
		}
	}
	

	/**
	 * 
	 * Calls sendKey(key, TYPE)
	 * 
	 * @param int key
	 * @return boolean
	 * @author DanielAl
	 */
	private boolean sendKey(int key) {
		return sendKey(key, TYPE);
	}
	

	/**
	 * Send Key Codes to the System with a Robot and java.awt.event.KeyEvent constants. <br>
	 * Functions:<br>
	 * - TYPE for type a Key<br>
	 * - PRESS for pressing and holding a key<br>
	 * - RELEASE for releasing a key<br>
	 * - COMBI for Key COmbination functionallity; used in printCombi()<br>
	 * - SHIFT for shift a Key to its Uppercase and type it...<br>
	 * 
	 * Hint: keyPress('ö') tested and it don't work<br>
	 * 
	 * @param int key, int function
	 * @return boolean
	 */
	private boolean sendKey(int key, int function) {
		if (key == 0 && function != COMBI) {
			logger.error("sendKey: UNKNOWN Key");
			return false;
		}
		keyRobot.delay(delay);
		switch (function) {
			case TYPE:
				keyRobot.keyPress(key);
				keyRobot.keyRelease(key);
				logger.trace("sendKey: Key sent: " + key);
				break;
			case PRESS:
				keyRobot.keyPress(key);
				logger.trace("sendKey: Key pressed: " + key);
				break;
			case RELEASE:
				keyRobot.keyRelease(key);
				logger.trace("sendKey: Key released: " + key);
				break;
			case COMBI: // Combination
				// Input are keys from the printCombi method...
				// each Key is pressed and pushed to the stack combi
				// if Key is 0, the Stack elements are released...
				// log is written in printCombi()
				switch (key) {
					case 0:
						while (!combi.isEmpty()) {
							Integer i = combi.pop();
							sendKey((int) i, RELEASE);
						}
						break;
					default:
					sendKey((int) key, PRESS);
					combi.push(key);
				}
				break;
			case SHIFT: // Shift function
				keyRobot.keyPress(KeyEvent.VK_SHIFT);
				keyRobot.keyPress(key);
				keyRobot.keyRelease(key);
				keyRobot.keyRelease(KeyEvent.VK_SHIFT);
				logger.trace("sendKey: Key sent with SHIFT: " + key);
				break;
		}
		return true;
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	

}
