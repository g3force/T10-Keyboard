/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.keyboard;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;


/**
 * This class loads a layout file. It needs a keymap to map the buttons to their according keys!
 * 
 * @author NicolaiO
 * 
 */
public class KeyboardLayoutSaver {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(KeyboardLayoutSaver.class);


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private KeyboardLayoutSaver() {
		throw new AssertionError();
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------

	/**
	 * Load a keyboardLayout
	 * 
	 * @param filePath to an keymap XML file
	 * @param keymap that was loaded from file
	 * @return KeyboardLayout
	 * @author NicolaiO
	 */
	public static void save(KeyboardLayout kbdLayout, String filePath, HashMap<Integer, Key> keymap) {
		File layoutFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.newDocument();// dBuilder.parse(layoutFile);
			// doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("key");
			ArrayList<Button> keys = new ArrayList<Button>();
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Button newKey = getKey(eElement, keymap);
					if (newKey != null) {
						keys.add(newKey);
						// TODO NicolaiO, Felix listener
						newKey.addActionListener(Controller.getInstance()); // use EventCollector as listener
					}
				} else {
					logger.warn("key-node is not an element-node");
				}
			}
			
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
			
			nList = doc.getElementsByTagName("font");
			String fname = "";
			int fstyle = 0, fsize = 0;
			if (nList.getLength() > 0) {
				NodeList font = nList.item(0).getChildNodes();
				for (int i = 0; i < font.getLength(); i++) {
					Node n = font.item(i);
					if (n.getNodeName() == "name") {
						fname = n.getTextContent();
					} else if (n.getNodeName() == "style") {
						try {
							fstyle = Integer.parseInt(n.getTextContent());
						} catch (NumberFormatException e) {
						}
					} else if (n.getNodeName() == "size") {
						try {
							fsize = Integer.parseInt(n.getTextContent());
						} catch (NumberFormatException e) {
						}
					}
				}
			}
			
			nList = doc.getElementsByTagName("dropdown");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				try {
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						NamedNodeMap attr = eElement.getAttributes();
						DropDownList cb = new DropDownList(getAttribute(attr, "type"), getIntAttribute(attr, "size_x"),
								getIntAttribute(attr, "size_y"), getIntAttribute(attr, "pos_x"), getIntAttribute(attr, "pos_y"));
						kbdLayout.addDdl(cb);
						// TODO NicolaiO, Felix listener
					}
				} catch (NullPointerException e) {
					logger.warn("Dropdown-element found, but can not be read correctly! node nr " + temp + ": "
							+ nNode.toString());
				}
			}
			
			
			kbdLayout.setFont(new Font(fname, fstyle, fsize));
			kbdLayout.setKeys(keys);
			// kbdLayout.setMode("default"); TODO NicolaiO to be fixed by Nico ;) Dirk
			kbdLayout.rescale();
			logger.info("loaded " + keys.size() + " Buttonkeys.");
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
		return kbdLayout;
	}
	
	
	/**
	 * Return Key-Object from given element
	 * 
	 * @param eElement must be a <key> node
	 * @return Key
	 */
	private static Button getKey(Element eElement, HashMap<Integer, Key> keymap) {
		try {
			NamedNodeMap attr = eElement.getAttributes();
			Button key = new Button(getIntAttribute(attr, "size_x"), getIntAttribute(attr, "size_y"), getIntAttribute(
					attr, "pos_x"), getIntAttribute(attr, "pos_y"));
			
			// Modes
			NodeList modes = eElement.getElementsByTagName("mode");
			for (int i = 0; i < modes.getLength(); i++) {
				Node item = modes.item(i);
				if (item != null) {
					String sModeName = "";
					String sColor = "";
					Node modeName = item.getAttributes().getNamedItem("name");
					Node color = item.getAttributes().getNamedItem("color");
					if (modeName != null) {
						sModeName = modeName.getTextContent();
					}
					if (color != null) {
						sColor = color.getTextContent();
					}
					// TODO NicolaiO add mode...
					// key.addMode(sModeName, keymap.get(item.getTextContent()));
				}
			}
			
			return key;
		} catch (NullPointerException e) {
			System.out.println("In getKey:");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function for receiving an attribute by name
	 * TODO NicolaiO, add comment!
	 * 
	 * @param attr
	 * @param name
	 * @return
	 * @throws NullPointerException
	 * @author NicolaiO
	 */
	private static String getAttribute(NamedNodeMap attr, String name) throws NullPointerException {
		Node node = attr.getNamedItem(name);
		if (node != null) {
			return node.getTextContent();
		}
		return "";
	}
	

	private static int getIntAttribute(NamedNodeMap attr, String name) throws NullPointerException {
		try {
			int value = Integer.parseInt(getAttribute(attr, name));
			return value;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	
	// private static int getModeFromString(String mode) {
	// if(mode.equals("default")) {
	// return null;
	// }
	// else {
	// return new ModeKey(modeKey, size_x, size_y, pos_x, pos_y)
	// }
	// }
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
