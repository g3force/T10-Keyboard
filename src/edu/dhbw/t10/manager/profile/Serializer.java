/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 24, 2011
 * Author(s): dirk
 *
 * *********************************************************
 */
package edu.dhbw.t10.manager.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import edu.dhbw.t10.type.tree.PriorityTree;


/**
 * Serializes an arbitrary Object
 * @author dirk
 * 
 */
public class Serializer {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Serializer.class);

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String pathToFile) {
		T out = null;
		try {
			FileInputStream file = new FileInputStream(pathToFile);
			ObjectInputStream o = new ObjectInputStream(file);
			out = (T) o.readObject();
			o.close();
		} catch (IOException e) {
			logger.error("Failure during Deserializing: " + e);
		} catch (ClassNotFoundException e) {
			logger.error("Class not found, not possible: " + e);
		}
		return out;
	}

	
	public static <T> void serialize(T toS, String pathToFile) {
		try {
			FileOutputStream file = new FileOutputStream(pathToFile);
			ObjectOutputStream o = new ObjectOutputStream(file);
			o.writeObject(toS);
			o.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
