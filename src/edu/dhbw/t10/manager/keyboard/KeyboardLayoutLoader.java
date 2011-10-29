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
import edu.dhbw.t10.type.keyboard.key.ModeButton;
import edu.dhbw.t10.type.keyboard.key.PhysicalButton;


/**
 * This class loads a layout file. It needs a keymap to map the buttons to their according keys!
 * 
 * @author NicolaiO
 * 
 */
public class KeyboardLayoutLoader {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger				logger	= Logger.getLogger(KeyboardLayoutLoader.class);
	private static HashMap<Integer, Key>	keymap;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private KeyboardLayoutLoader() {
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
	public static KeyboardLayout load(String filePath, HashMap<Integer, Key> _keymap) {
		DocumentBuilder dBuilder;
		Document doc;
		keymap = _keymap;
		KeyboardLayout kbdLayout = new KeyboardLayout(0, 0, 1, 1, 1);
		File layoutFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(layoutFile);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException err) {
			logger.error("Could not initialize dBuilder");
			err.printStackTrace();
			return kbdLayout;
		} catch (SAXException err) {
			logger.error("Could not parse document");
			err.printStackTrace();
			return kbdLayout;
		} catch (IOException err) {
			logger.error("Could not parse document");
			err.printStackTrace();
			return kbdLayout;
		}
		NodeList nList;
		
		// ########################## read ModeButtons ########################
		HashMap<Integer, ModeButton> modeButtons = new HashMap<Integer, ModeButton>();
		// ########################## read Buttons ############################
		ArrayList<Button> buttons = getButtons(doc, modeButtons);
		// ########################## read MuteButtons ###########################
		
		int sizex = 0, sizey = 0;
		float scalex = 1.0f, scaley = 1.0f, scale_font = 1.0f;

		nList = doc.getElementsByTagName("sizex");
		if (nList.getLength() > 0)
			sizex = Integer.parseInt(nList.item(0).getTextContent());

		nList = doc.getElementsByTagName("sizey");
		if (nList.getLength() > 0)
			sizey = Integer.parseInt(nList.item(0).getTextContent());

		nList = doc.getElementsByTagName("scalex");
		if (nList.getLength() > 0)
			scalex = Float.parseFloat(nList.item(0).getTextContent());

		nList = doc.getElementsByTagName("scaley");
		if (nList.getLength() > 0)
			scaley = Float.parseFloat(nList.item(0).getTextContent());
		
		nList = doc.getElementsByTagName("scale_font");
		if (nList.getLength() > 0)
			scale_font = Float.parseFloat(nList.item(0).getTextContent());

		kbdLayout = new KeyboardLayout(sizex, sizey, scalex, scaley, scale_font);
		
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
					// TODO NicolaiO, Felix listener for dropdown list
				}
			} catch (NullPointerException e) {
				logger.warn("Dropdown-element found, but can not be read correctly! node nr " + temp + ": "
						+ nNode.toString());
			}
		}
		
		
		kbdLayout.setFont(new Font(fname, fstyle, fsize));
		kbdLayout.rescale();
		logger.info("loaded " + buttons.size() + " Buttonkeys.");

		return kbdLayout;
	}
	
	
	/**
	 * Return Key-Object from given element
	 * 
	 * @param eElement must be a <key> node
	 * @return Key
	 */
	private static ArrayList<Button> getButtons(Document doc, HashMap<Integer, ModeButton> modeButtons) {
		ArrayList<Button> buttons = new ArrayList<Button>();
		NodeList nList = doc.getElementsByTagName("button");
		
		// loop through buttons
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				
				try {
					Button button = (Button) getPhysicalButton(nNode);
					
					// receive Modes
					NodeList modes = eElement.getElementsByTagName("mode");
					for (int i = 0; i < modes.getLength(); i++) {
						Node item = modes.item(i);
						if (item != null) {
							int iModeName = 0;
							String sColor = "";
							boolean accept = false;
							Node modeName = item.getAttributes().getNamedItem("modename");
							Node color = item.getAttributes().getNamedItem("color");
							Node nAccept = item.getAttributes().getNamedItem("accept");
							if (modeName != null) {
								iModeName = Integer.parseInt(modeName.getTextContent());
							}
							if (color != null) {
								sColor = color.getTextContent();
							}
							if (nAccept != null && nAccept.getTextContent().equals("true")) {
								accept = true;
							}
							button.addActionListener(Controller.getInstance()); // use EventCollector as listener
							// TODO NicolaiO add mode: color and accept
							button.addMode(modeButtons.get(iModeName), keymap.get(item.getTextContent()));
							buttons.add(button);
						}
					}
				} catch (NullPointerException e) {
					System.out.println("In getKey:");
					e.printStackTrace();
				}


			} else {
				logger.warn("key-node is not an element-node");
			}
		}
		return null;
	}
	
	
	private static PhysicalButton getPhysicalButton(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			try {
				Element eElement = (Element) node;
				NamedNodeMap attr = eElement.getAttributes();
				return new PhysicalButton(getIntAttribute(attr, "size_x"), getIntAttribute(attr, "size_y"),
						getIntAttribute(attr, "pos_x"), getIntAttribute(attr, "pos_y"));
			} catch (NullPointerException e) {
				logger.warn("Could not read node as PhysicalButton. Node: " + node);
			}
		}
		return null;
	}


	/**
	 * Helper function for receiving an attribute by name
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
	
	
	/**
	 * Helper function for receiving an attribute by name, returning as int
	 * 
	 * @param attr
	 * @param name
	 * @return
	 * @throws NullPointerException
	 * @author NicolaiO
	 */
	private static int getIntAttribute(NamedNodeMap attr, String name) throws NullPointerException {
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
}
