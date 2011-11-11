/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 01.11.2011
 * Author(s): DanielAl
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.output;

/**
 * Exception class if the application is running on a unsupported Operating System (only Windows and Linux are
 * supported).
 * 
 * @author DanielAl
 */
public class UnknownOSException extends RuntimeException {
	
	/**  */
	private static final long	serialVersionUID	= -3099897806604277720L;
	
	
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * Calls constructor from super class (RuntimeException).
	 * 
	 * @author DanielAl
	 */
	public UnknownOSException() {
		super();
	}
	
	
	/**
	 * 
	 * Calls constructor from super class (RuntimeException) with a String to set the Exception Message.
	 * 
	 * @param s
	 * @author DanielAl
	 */
	public UnknownOSException(String s) {
		super(s);
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
