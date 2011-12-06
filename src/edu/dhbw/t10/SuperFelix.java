/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): NicolaiO, DanielAl, DirkK, SebastianN, FelixP
 * 
 * *********************************************************
 */
package edu.dhbw.t10;

import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.dhbw.t10.manager.Controller;


/**
 * This is the main class of the t10 keyboard. It only initializes the first important classes.
 * 
 * @author NicolaiO, DanielAl, FelixP, DirkK, SebastianN
 * 
 */
public class SuperFelix {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(SuperFelix.class);
	/**
	 * VERSION and REV are used and manipulated by a script! Please be careful, when editing anything...
	 * VERSION shouldn't start with a V... it can be added in the output.
	 * REV will be filled by a script, when generating the deb package, so do not change this value!
	 * 
	 * For information:
	 * Revision of Git Repository: Look in file .git/refs/heads/master
	 * automatic: git shortlog | grep -E '^[ ]+\w+' | wc -l
	 */
	public static final String		VERSION	= "1.0";
	public static final String		REV		= "639";


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private SuperFelix() {
		/*
		 * initialize log4j, a logger from apache.
		 * See http://logging.apache.org/log4j/1.2/manual.html for more details
		 * Log Levels: TRACE, DEBUG, INFO, WARN, ERROR and FATAL
		 * 
		 * configuration is stored in a config file. If it does not exist, use basic config
		 */
		URL logUrl = getClass().getResource("/res/log4j.conf");

		if (logUrl != null) {
			PropertyConfigurator.configure(logUrl);
		} else {
			// basic config with only a console appender
			BasicConfigurator.configure();
			logger.setLevel(Level.ALL);
		}
		
		Controller.getInstance();
		logger.info("Keyboard started.");
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Initialize the logger and start the application
	 * 
	 * @param args
	 * @author NicolaiO
	 */
	public static void main(String[] args) {
		
		if (args.length == 1) {
			if (args[0].equals("-v") || args[0].equals("--version")) {
				System.out.println("Version: " + VERSION);
				System.out.println("Revision: " + REV);
				System.exit(0);
			}
		}

		new SuperFelix();

	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
