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
 * TODO FelixP optional menu item "Modify"
 * TODO DanielAl CAPS keycode?? for system LED? :P Another idea? delete caps? with what to be replaced??
 * TODO ALL Read: Ich habe die Symphony Kompatibilität hergestellt. allerdings geht das auf Kosten von Performance...
 * Jetzt wird ein Vorschlag, wenn er accepted wird, nicht mehr demarkiert, sondern neu geschrieben.
 * Wenn gelesen, löschen! *
 * @author NicolaiO, DanielAl, FelixP, DirkK, SebastianN
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

		new SuperFelix();

	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
