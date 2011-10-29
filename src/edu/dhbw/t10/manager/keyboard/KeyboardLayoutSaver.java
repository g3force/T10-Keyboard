/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 29, 2011
 * Author(s): dirk
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.keyboard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map.Entry;

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
import org.w3c.dom.Text;

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
	 * Save the keyboardLayout
	 * 
	 * @param kbdLayout the layout which shall be converted to a xml file
	 * @param filePath to an keymap XML file
	 * 
	 * @author dirk
	 */
	public static void save(KeyboardLayout kbdLayout, String filePath) {
		logger.info("Starting to save the KeyboardLayout to XML");
		File layoutFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();// dBuilder.parse(layoutFile);
			// doc.getDocumentElement().normalize();
			Element layout = doc.createElement("layout");
			doc.appendChild(layout);

			Element sizex = doc.createElement("sizex");
			Text text = doc.createTextNode(kbdLayout.getOrigSize_x() + "");
			sizex.appendChild(text);
			layout.appendChild(sizex);

			Element sizey = doc.createElement("sizey");
			text = doc.createTextNode(kbdLayout.getOrigSize_y() + "");
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
			
			Element name = doc.createElement("name");
			font.appendChild(name);
			
			Element style = doc.createElement("style");
			font.appendChild(style);
			
			Element size = doc.createElement("size");
			text = doc.createTextNode(kbdLayout.getFontSize() + "");
			size.appendChild(text);
			font.appendChild(size);
			
			for (DropDownList dd : kbdLayout.getDdls()) {
				Element dropdown = doc.createElement("dropdown");
				dropdown.setAttribute("type", dd.getName());
				dropdown.setAttribute("size_x", dd.getOrigSize().getWidth() + "");
				dropdown.setAttribute("size_y", dd.getOrigSize().getHeight() + "");
				dropdown.setAttribute("pos_x", dd.getX() + "");
				dropdown.setAttribute("pos_y", dd.getY() + "");
				System.out.println("Dropdown");
				layout.appendChild(dropdown);
			}
			for (Button button : kbdLayout.getButtons()) {
				Element buttonEl = doc.createElement("button");
				setSizeOfPhysicalButton(buttonEl, button);

				Element key = doc.createElement("key");
				key.setAttribute("modename", "0");
				text = doc.createTextNode(button.getKey().getId() + "");
				key.appendChild(text);
				buttonEl.appendChild(key);

				for (Entry<ModeButton, Key> mode : button.getModes().entrySet()) {
					Element modeEl = doc.createElement("dropdown");
					modeEl.setAttribute("modename", mode.getKey().getModeKey().getId() + "");
					text = doc.createTextNode(mode.getValue().getId() + "");
					modeEl.appendChild(text);
					buttonEl.appendChild(modeEl);
				}
				layout.appendChild(buttonEl);
			}

			for (ModeButton modeButton : kbdLayout.getModeButtons()) {
				Element modeButtonEl = doc.createElement("modebutton");
				setSizeOfPhysicalButton(modeButtonEl, modeButton);
				
				Element key = doc.createElement("key");		
				text = doc.createTextNode(modeButton.getModeKey().getId() + "");
				key.appendChild(text);
				modeButtonEl.appendChild(key);
				
				layout.appendChild(modeButtonEl);
			}
			String xml = convertDocToString(doc);
			printToPath(xml, filePath);

		} catch (ParserConfigurationException err) {
			logger.error("Could not initialize dBuilder");
			err.printStackTrace();
		}
		logger.info("The KeyboardLayout is saved to XML");
	}
	
	
	private static String convertDocToString(Document doc) {
		// OUTPUT TO FILE
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans;
		try {
			trans = transfac.newTransformer();
		} catch (TransformerConfigurationException err1) {
			logger.error("Failed to convert the keyboard XML-DOM to String (1)");
			trans = null;
			err1.printStackTrace();
		}
		
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		trans.setOutputProperty(OutputKeys.INDENT, "yes");
		
		// create string from xml tree
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		try {
			trans.transform(source, result);
		} catch (TransformerException err) {
			logger.error("Failed to convert the keyboard XML-DOM to String (2)");
			err.printStackTrace();
		}
		String xmlString = sw.toString();
		
		return xmlString;
	}
	
	
	private static void printToPath(String xmlString, String file) {
		File confFile = new File(file);
		FileWriter fw;
		try {
			fw = new FileWriter(confFile);
		} catch (IOException err1) {
			logger.error("Failed to write the keyboard xml string to file (1)");
			fw = null;
			err1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(xmlString);
			bw.close();
			logger.debug("XML written to file " + file);
		} catch (IOException err) {
			logger.error("Failed to write the keyboard xml string to file (2)");
			err.printStackTrace();
		}
	}

	
	private static void setSizeOfPhysicalButton(Element el, PhysicalButton button) {
		el.setAttribute("size_x", button.getOrigSize().getWidth() + "");
		el.setAttribute("size_y", button.getOrigSize().getHeight() + "");
		el.setAttribute("pos_x", button.getPos_x() + "");
		el.setAttribute("pos_y", button.getPos_y() + "");
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
