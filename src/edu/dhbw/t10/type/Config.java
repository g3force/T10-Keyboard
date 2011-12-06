/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Dec 5, 2011
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.t10.type;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * TODO dirk, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author dirk
 * 
 */
public class Config {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger		= Logger.getLogger(Config.class);
	private static Properties		conf;
	private static String			configFile	= "t10keyboard.conf";
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * loads the config file, fills the conf property attribute
	 * @author dirk
	 */
	public static void loadConfig(String datapath) {
		conf = new Properties();
		FileInputStream fis;
		try {
			// reading the config file
			fis = new FileInputStream(datapath + "/" + configFile);
			conf.load(fis);
			logger.info("Config file read");
		} catch (IOException err) {
			logger.warn("Could not read the config file");
			// config file not found, set the config values to default
		}
		if (!conf.containsKey("ActiveProfile")) {
			logger.debug("ActiveProfile was not in the config file");
			conf.setProperty("ActiveProfile", "default");
		}
		if (!conf.containsKey("PROFILE_PATH"))
			conf.setProperty("PROFILE_PATH", "");
		if (!conf.contains("defaultAllowedChars"))
			conf.setProperty("defaultAllowedChars","A-Za-z\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC");
	}
	
	
	/**
	 * saves the config, should be called at program exit
	 * @param datapath
	 * @author dirk
	 */
	public static void saveConfig(String datapath) {
		try {
			FileOutputStream fos = new FileOutputStream(datapath + "/" + configFile);
			conf.store(fos, "Stored by closing the program");
			logger.debug("config file saved to" + datapath + "/" + configFile);
		} catch (IOException err) {
			logger.error("Could not store the properties at " + datapath + " / " + configFile);
			err.printStackTrace();
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public static Properties getConf() {
		return conf;
	}
	
	
	public static void setConf(Properties conf) {
		Config.conf = conf;
	}

}
