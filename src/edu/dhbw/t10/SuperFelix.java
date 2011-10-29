/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.view.Presenter;


/**
 * TODOs
 * 
 * TODO ALL graphical buttons (keys) e.g. pause- and arrow keys
 * 
 */
/**
 * This is the main class of the t10 keyboard. It only initializes the first important classes.
 * 
 * @author NicolaiO
 * 
 */
public class SuperFelix {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(SuperFelix.class);
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * initialize log4j, a logger from apache.
		 * See http://logging.apache.org/log4j/1.2/manual.html for more details
		 * Log Levels: TRACE, DEBUG, INFO, WARN, ERROR and FATAL
		 */
		logger.setLevel(Level.ALL);
		BasicConfigurator.configure();

		logger.debug("calling Controller first time.");
		Controller.getInstance();
		logger.debug("calling Presenter first time.");
		Presenter.getInstance();
		logger.info("Keyboard started.");
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
