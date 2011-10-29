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
		logger.debug("initializing...");
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
		HashMap<Integer, ModeButton> modeButtons = getModeButtons(doc);
		ArrayList<ModeButton> modeButtonArray = new ArrayList<ModeButton>();
		for (ModeButton b : modeButtons.values()) {
			modeButtonArray.add(b);
		}
		logger.info("loaded " + modeButtonArray.size() + " ModeButtons.");
		// ########################## read Buttons ############################
		ArrayList<Button> buttons = getButtons(doc, modeButtons);
		logger.info("loaded " + buttons.size() + " Buttons.");
		// ########################## read MuteButtons ###########################
		
		
		// get other infos from layout file
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
		
		
		kbdLayout.setModeButtons(modeButtonArray);
		kbdLayout.setButtons(buttons);
		kbdLayout.setFont(new Font(fname, fstyle, fsize));
		kbdLayout.rescale();

		logger.debug("initialized.");
		return kbdLayout;
	}
	
	
	/**
	 * TODO Nicolai add comment...
	 * 
	 * @param eElement must be a <key> node
	 * @return Key
	 */
	private static ArrayList<Button> getButtons(Document doc, HashMap<Integer, ModeButton> modeButtons) {
		ArrayList<Button> buttons = new ArrayList<Button>();
		NodeList nList = doc.getElementsByTagName("button");
		
		// loop through buttons
		for (int temp = 0; temp < nList.getLength(); temp++) {
			try {
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() != Node.ELEMENT_NODE) {
					logger.warn("key-node is not an element-node");
					continue;
				}

				Element eElement = (Element) nNode;
				Bounds b = getBounds(nNode);
				Button button = new Button(b.size_x, b.size_y, b.pos_x, b.pos_y);

				// receive default key
				NodeList defkey = eElement.getElementsByTagName("key");
				if (defkey.getLength() == 1) {
					try {
						int id = Integer.parseInt(defkey.item(0).getTextContent());
						Key key = keymap.get(id);
						if (key == null) {
							logger.warn("key not found keymap. temp=" + temp + " id=" + id);
							continue;
						}
						button.setKey(key);
					} catch (NumberFormatException e) {
						logger.warn("key id could not be parsed to Integer. id=" + defkey.item(0).getTextContent());
					}
					
				} else {
					logger.warn("Number of key-elements is not 1: " + defkey.getLength());
				}
				
				buttons.add(button);

				// receive Modes
				NodeList modes = eElement.getElementsByTagName("mode");
				for (int i = 0; i < modes.getLength(); i++) {
					Node item = modes.item(i);
					if (item != null) {
						int iModeName = 0;
						// String sColor = "";
						boolean accept = false;
						Node modeName = item.getAttributes().getNamedItem("modename");
						// Node color = item.getAttributes().getNamedItem("color");
						Node nAccept = item.getAttributes().getNamedItem("accept");
						if (modeName != null) {
							try {
								iModeName = Integer.parseInt(modeName.getTextContent());
							} catch (NumberFormatException e) {
								logger.warn("modename could not be parsed to Integer. modename=" + modeName.getTextContent());
							}
						}
						// if (color != null) {
						// sColor = color.getTextContent();
						// }
						if (nAccept != null && nAccept.getTextContent().equals("true")) {
							accept = true;
						}
						button.addActionListener(Controller.getInstance()); // use EventCollector as listener
						try {
							Key key = keymap.get(Integer.parseInt(item.getTextContent()));
							if (key == null) {
								logger.warn("key not found in keymap. key content=" + item.getTextContent());
							}
							key.setAccept(accept);
							button.addMode(modeButtons.get(iModeName), key);
						} catch (NumberFormatException e) {
							logger.warn("Could not parse key to Integer. i=" + i);
						}
					}
				}
			} catch (NullPointerException e) {
				logger.warn("A Button could not be read: NullPointerException");
				e.printStackTrace();
			} catch (NumberFormatException e) {
				logger.warn("A Button could not be read: NumberFormatException");
				e.printStackTrace();
			}
		}
		return buttons;
	}
	
	
	private static HashMap<Integer, ModeButton> getModeButtons(Document doc) {
		HashMap<Integer, ModeButton> modeButtons = new HashMap<Integer, ModeButton>();
		NodeList nList = doc.getElementsByTagName("modebutton");
		
		// loop through buttons
		for (int temp = 0; temp < nList.getLength(); temp++) {
			try {
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() != Node.ELEMENT_NODE) {
					logger.warn("key-node is not an element-node");
					continue;
				}
				
				Element eElement = (Element) nNode;
				Key key = null;
				// receive default key
				NodeList defkey = eElement.getElementsByTagName("key");
				if (defkey.getLength() == 1) {
					try {
						key = keymap.get(Integer.parseInt(defkey.item(0).getTextContent()));
					} catch (NumberFormatException e) {
						logger.warn("Could not parse key: " + defkey.item(0).getTextContent());
					}
					if (key == null)
						continue;
				} else
					continue;
				Bounds b = getBounds(nNode);
				ModeButton button = new ModeButton(key, b.size_x, b.size_y, b.pos_x, b.pos_y);
				modeButtons.put(key.getId(), button);
			} catch (NullPointerException e) {
				logger.warn("A ModeButton could not be read.");
			}
		}
		return modeButtons;
	}


	private static Bounds getBounds(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			try {
				Element eElement = (Element) node;
				NamedNodeMap attr = eElement.getAttributes();
				return new Bounds(getIntAttribute(attr, "size_x"), getIntAttribute(attr, "size_y"), getIntAttribute(attr,
						"pos_x"), getIntAttribute(attr, "pos_y"));

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
	private static class Bounds {
		public int	size_x, size_y, pos_x, pos_y;
		
		
		public Bounds(int size_x, int size_y, int pos_x, int pos_y) {
			this.size_x = size_x;
			this.size_y = size_y;
			this.pos_x = pos_x;
			this.pos_y = pos_y;
		}
	}
}
