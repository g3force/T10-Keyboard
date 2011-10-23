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

import org.apache.log4j.Logger;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author Andres
 * 
 */
public class Output {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger			= Logger.getLogger(Output.class);
	Keys									keyTransform	= new Keys();

	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public Output() {
	}
		
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * 
	 * Transoforms a given CodeSequence from OutputManager to KeyEvent-Constants and sends it to the System
	 * 
	 * @param c
	 * @return
	 */
	public boolean printString(String codeSequence) {
		int length = codeSequence.length();
		int[] keyCode;
		
		if (codeSequence.charAt(0) == '\\' && codeSequence.charAt(length - 1) == '\\' && length != 2) {
			keyCode = new int[1];
			keyCode[0] = keyTransform.getKey(codeSequence.substring(1, length - 1));
			this.sendKey(keyCode[0]);
			logger.info("Control Symbol printed: " + codeSequence);
		} else if (codeSequence.charAt(0) == '\\' && codeSequence.charAt(length - 1) == '\\' && length == 2) {
			keyCode = new int[1];
			keyCode[0] = keyTransform.getKey("\\");
			this.sendKey(keyCode[0]);
			logger.info("String printed: " + codeSequence);
		} else {
			keyCode = new int[length];
			for (int i = 0; i < length; i++) {
				// keyCode[i] = KeyEvent.VK_A;
				keyCode[i] = keyTransform.getKey(codeSequence.substring(i, i + 1).toUpperCase());
				if (Character.isUpperCase(codeSequence.charAt(i)) == true) {
					this.sendKey(keyCode[i], 1);
				} else if (keyTransform.getShift(keyCode[i])) {
					this.sendKey(keyCode[i], 1);
				} else if (keyTransform.getAltGr(keyCode[i])) { // FIXME AltGr funktioniert nicht
					this.sendKey(keyCode[i], 4);
				} else if (keyTransform.getCrtl(keyCode[i])) {
					this.sendKey(keyCode[i], 2);
				} else {
					this.sendKey(keyCode[i]);
				}
			}
			logger.info("String printed: " + codeSequence);
		}
		return true;
	}
	

	protected boolean sendKey(int key) {
		return sendKey(key, 0);
	}

	protected boolean sendKey(int key, int function) {
		return sendKey(key, function, 0);
	}
	

	// FIXME
	/**
	 * 
	 * Send Key Codes to the System
	 * 
	 * Use function to use Shift, ... functionality
	 * 0: without; 1: Shift; 2: Control; 3: Alt; 4: Alt Gr; 5: Super
	 * 
	 * @param key, function, hold
	 * @return
	 */
	protected boolean sendKey(int key, int function, int hold) {
		if (key == 0) {
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
			}
			

		} catch (AWTException e) {
			e.printStackTrace();
		}
		return true;
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
