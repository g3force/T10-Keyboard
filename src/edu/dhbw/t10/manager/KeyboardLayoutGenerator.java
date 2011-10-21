/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.dhbw.t10.type.Key;
import edu.dhbw.t10.type.KeyboardLayout;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class KeyboardLayoutGenerator {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger		logger		= Logger.getLogger(KeyboardLayoutGenerator.class);
	public static final String			KEY			= "key";
	private File							layoutFile;
	private DocumentBuilderFactory	dbFactory;
	private DocumentBuilder				dBuilder;
	private Document						doc;
	private ArrayList<Key>				keys;
	private String							filePath		= "conf/keyboard_layout_de_default.xml";
	private KeyboardLayout				kbdLayout	= new KeyboardLayout(0, 0, 1);


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public KeyboardLayoutGenerator() {
		init();
	}
	

	public KeyboardLayoutGenerator(String filePath) {
		this.filePath = filePath;
		init();
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * initialize the document parser
	 * 
	 */
	private void init() {
		layoutFile = new File(filePath);
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			reload();
			logger.debug("LayoutFileManager initialized");
		} catch (ParserConfigurationException err) {
			logger.error("Could not initialize dBuilder");
			err.printStackTrace();
		}
	}
	

	/**
	 * Reload (or load first time) the keys from given layout file
	 * 
	 * @return number of loaded key, -1 if error occurred
	 */
	private void reload() {
		if (dBuilder != null) {
			try {
				doc = dBuilder.parse(layoutFile);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName(KEY);
				keys = new ArrayList<Key>();
				
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						Key newKey = getKey(eElement);
						if (newKey != null) {
							keys.add(newKey);
						}
					} else {
						logger.warn("key-node is not an element-node");
					}
				}
				logger.debug("Loaded " + keys.size() + " keys.");
				
				int sizex = 0, sizey = 0;
				float scale = 1.0f;
				nList = doc.getElementsByTagName("sizex");
				if (nList.getLength() > 0)
					sizex = Integer.parseInt(nList.item(0).getTextContent());
				nList = doc.getElementsByTagName("sizey");
				if (nList.getLength() > 0)
					sizey = Integer.parseInt(nList.item(0).getTextContent());
				nList = doc.getElementsByTagName("scale");
				if (nList.getLength() > 0)
					scale = Float.parseFloat(nList.item(0).getTextContent());
				kbdLayout = new KeyboardLayout(sizex, sizey, scale);
				
				kbdLayout.setKeys(keys);
				kbdLayout.rescale();

			} catch (SAXException err) {
				err.printStackTrace();
			} catch (IOException err) {
				err.printStackTrace();
			} catch (NullPointerException err) {
				System.out.println("In reload():");
				err.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Return Key-Object from given element
	 * 
	 * @param eElement must be a <key> node
	 * @return Key
	 */
	private Key getKey(Element eElement) {
		try {
			NamedNodeMap attr = eElement.getAttributes();
			Key key = new Key(getIntAttribute(attr, "size_x"), getIntAttribute(attr, "size_y"), getIntAttribute(attr,
					"pos_x"), getIntAttribute(attr, "pos_y"));
			
			NodeList modes = eElement.getElementsByTagName("mode");
			for (int i = 0; i < modes.getLength(); i++) {
				Node item = modes.item(i);
				if (item != null) {
					Node keycode = item.getAttributes().getNamedItem("keycode");
					Node modeName = item.getAttributes().getNamedItem("name");
					if (keycode != null && modeName != null) {
						key.addMode(modeName.getTextContent(), item.getTextContent(), keycode.getTextContent());
					} else {
						logger.warn("key-node could not be read. Item: " + item.getTextContent());
					}
				}
			}
			
			return key;
		} catch (NullPointerException e) {
			System.out.println("In getKey:");
			e.printStackTrace();
		}
		return new Key();
	}
	

	private String getAttribute(NamedNodeMap attr, String name) throws NullPointerException {
		Node node = attr.getNamedItem(name);
		if (node != null) {
			return node.getTextContent();
		}
		return "";
	}
	

	private int getIntAttribute(NamedNodeMap attr, String name) throws NullPointerException {
		try {
			int value = Integer.parseInt(getAttribute(attr, name));
			return value;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public String getFilePath() {
		return filePath;
	}
	

	/**
	 * Set a new file path and reload keys
	 * 
	 * @param filePath to xml layout file
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		reload();
	}

	
	public KeyboardLayout getKbdLayout() {
		return kbdLayout;
	}
}
