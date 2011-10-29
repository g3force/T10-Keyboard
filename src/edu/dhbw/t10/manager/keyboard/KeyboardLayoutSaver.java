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

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

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
			Element layout = doc.createElement("layout");

			Element sizex = doc.createElement("sizex");
			Text text = doc.createTextNode(kbdLayout.getSize_x() + "");
			sizex.appendChild(text);
			layout.appendChild(sizex);

			Element sizey = doc.createElement("sizey");
			text = doc.createTextNode(kbdLayout.getSize_y() + "");
			sizey.appendChild(text);
			layout.appendChild(sizey);

			Element scalex = doc.createElement("scalex");
			text = doc.createTextNode(kbdLayout.getScale_x() + "");
			scalex.appendChild(text);
			layout.appendChild(scalex);

			Element scaley = doc.createElement("scaley");
			text = doc.createTextNode(kbdLayout.getScale_y() + "");
			scaley.appendChild(text);
			layout.appendChild(scaley);
			
			Element scale_font = doc.createElement("scale_font");
			text = doc.createTextNode(kbdLayout.getScale_font() + "");
			scale_font.appendChild(text);
			layout.appendChild(scale_font);

			Element font = doc.createElement("font");
			layout.appendChild(font);
			
			Element modekey = doc.createElement("modekey");
			font.appendChild(modekey);
			
			Element style = doc.createElement("style");
			font.appendChild(style);
			
			Element size = doc.createElement("size");
			text = doc.createTextNode(kbdLayout.getFontSize() + "");
			size.appendChild(text);
			font.appendChild(size);
			
			for (DropDownList dd : kbdLayout.getDdls()) {
				Element dropdown = doc.createElement("dropdown");
				dropdown.setAttribute("type", dd.getName());
				dropdown.setAttribute("size_x", dd.getWidth() + "");
				dropdown.setAttribute("size_y", dd.getHeight() + "");
				dropdown.setAttribute("pos_x", dd.getX() + "");
				dropdown.setAttribute("pos_y", dd.getX() + "");
				layout.appendChild(dropdown);
			}
			for (ModeButton modeB : kbdLayout.getModeButtons()) {

			}
			ArrayList<Button> keys = new ArrayList<Button>();
			
			// OUTPUT TO FILE
			TransformerFactory transfac = TransformerFactory.newInstance();
         Transformer trans;
			trans = transfac.newTransformer();
			
         trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         trans.setOutputProperty(OutputKeys.INDENT, "yes");

         //create string from xml tree
         StringWriter sw = new StringWriter();
         StreamResult result = new StreamResult(sw);
         DOMSource source = new DOMSource(doc);
			try {
				trans.transform(source, result);
			} catch (TransformerException err) {
				// TODO Auto-generated catch block
				err.printStackTrace();
			}
         String xmlString = sw.toString();

         //print xml
         System.out.println("Here's the xml:\n\n" + xmlString);
		} catch (ParserConfigurationException err) {
			logger.error("Could not initialize dBuilder");
			err.printStackTrace();
		} catch (TransformerConfigurationException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}

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
