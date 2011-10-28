/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 26, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.keyboard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.dhbw.t10.type.keyboard.key.SingleKey;


/**
 * This is a loader class for a keymap. This is a static class with only a load method for getting a hashmap from a
 * given path to an keymap file
 * 
 * @author NicolaiO
 * 
 */
public class KeymapLoader {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(KeymapLoader.class);
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private KeymapLoader() {
		throw new AssertionError();
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Load a keymap file.
	 * 
	 * @param filePath to an keymap XML file
	 * @return HashMap with id->SingleKey
	 * @author NicolaiO
	 */
	public static HashMap<Integer, SingleKey> load(String filePath) {
		File layoutFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(layoutFile);
			doc.getDocumentElement().normalize();
			
			HashMap<Integer, SingleKey> keymap = new HashMap<Integer, SingleKey>();
			NodeList keytypes = doc.getElementsByTagName("keytype");
			int type = SingleKey.UNKNOWN_KEY;
			for (int i = 0; i < keytypes.getLength(); i++) {
				NodeList keys = keytypes.item(i).getChildNodes();
				try {
					String stype = keys.item(i).getAttributes().getNamedItem("name").getTextContent();
					type = convertType(stype);
				} catch (NullPointerException e) {
					logger.warn("A keytype could not be read. i=" + i);
					type = SingleKey.UNKNOWN_KEY;
				}
				for (int j = 0; j < keys.getLength(); j++) {
					Node key = keys.item(j);
					try {
						if (key.getNodeName().equals("key")) {
							int id = Integer.parseInt(key.getAttributes().getNamedItem("id").getTextContent());
							String keycode = key.getAttributes().getNamedItem("keycode").getTextContent();
							String name = key.getTextContent();
							keymap.put(id, new SingleKey(id, name, keycode, type));
						}
					} catch (NullPointerException e) {
						logger.warn("A key in keymap could not be read. j=" + j + " i=" + i);
					} catch (NumberFormatException e) {
						logger.warn("A key in keymap had a bad number format. j=" + j + " i=" + i);
					}
				}
			}
			logger.info("loaded " + keymap.size() + " Singlekeys.");
			return keymap;
		} catch (ParserConfigurationException err) {
			logger.error("Could not initialize dBuilder");
			err.printStackTrace();
		} catch (SAXException err) {
			logger.error("Could not parse document");
			err.printStackTrace();
		} catch (IOException err) {
			logger.error("Could not parse document");
			err.printStackTrace();
		}
		return new HashMap<Integer, SingleKey>();
	}
	
	
	private static int convertType(String stype) {
		if (stype.equals("control"))
			return SingleKey.CONTROL_KEY;
		else if (stype.equals("char"))
			return SingleKey.CHAR_KEY;
		else if (stype.equals("unicode"))
			return SingleKey.UNICODE_KEY;
		return SingleKey.UNKNOWN_KEY;
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
