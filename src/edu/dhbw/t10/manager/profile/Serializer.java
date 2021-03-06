/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 24, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Serializes an arbitrary Object
 * 
 * @author DirkK
 */
public class Serializer {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * deserializes a arbitrary object
	 * 
	 * @param pathToFile the path of the serialized file
	 * @return
	 * @throws IOException
	 * @author DirkK
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String pathToFile) throws IOException {
		T out = null;
		FileInputStream file = new FileInputStream(pathToFile);
		ObjectInputStream o = new ObjectInputStream(file);
		try {
			out = (T) o.readObject();
		} catch (ClassNotFoundException err) {
			err.printStackTrace();
		}
		o.close();
		return out;
	}
	
	
	/**
	 * 
	 * serializes a arbitrary object
	 * 
	 * @param toS the object which shall be serialized
	 * @param pathToFile the path to where the object shall be serialized
	 * @throws IOException
	 * @author DirkK
	 */
	public static <T> void serialize(T toS, String pathToFile) throws IOException {
		FileOutputStream file = new FileOutputStream(pathToFile);
		ObjectOutputStream o = new ObjectOutputStream(file);
		o.writeObject(toS);
		o.close();
	}
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
