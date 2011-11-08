/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 29, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.keyboard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
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
import edu.dhbw.t10.type.keyboard.key.MuteButton;
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
	 * @author DirkK
	 */
	public static void save(KeyboardLayout kbdLayout, String filePath) {
		logger.info("Starting to save the KeyboardLayout to XML");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Element layout = doc.createElement("layout");
			doc.appendChild(layout);
			// ---------------HEADER-----------------
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
			// ---------------DROPDOWN-----------------
			for (DropDownList dd : kbdLayout.getDdls()) {
				Element dropdown = doc.createElement("dropdown");
				dropdown.setAttribute("type", dd.getTypeAsString());
				dropdown.setAttribute("size_x", ((int) dd.getOrigSize().getWidth()) + "");
				dropdown.setAttribute("size_y", ((int) dd.getOrigSize().getHeight()) + "");
				dropdown.setAttribute("pos_x", dd.getPos_x() + "");
				dropdown.setAttribute("pos_y", dd.getPos_y() + "");
				layout.appendChild(dropdown);
			}
			// ---------------MUTEBUTTONS-----------------
			for (MuteButton muteButton : kbdLayout.getMuteButtons()) {
				Element muteButtonEl = doc.createElement("mutebutton");
				setSizeOfPhysicalButton(muteButtonEl, muteButton);
				// muteButtonEl.setAttribute("type", muteButton.getType());
				// ON
				Element on = doc.createElement("on");
				on.setAttribute("color", muteButton.getStringFromColor(muteButton.getOnColor()));
				text = doc.createTextNode(muteButton.getOnName());
				on.appendChild(text);
				muteButtonEl.appendChild(on);
				// OFF
				Element off = doc.createElement("off");
				off.setAttribute("color", muteButton.getStringFromColor(muteButton.getOffColor()));
				text = doc.createTextNode(muteButton.getOffName());
				off.appendChild(text);
				muteButtonEl.appendChild(off);
				
				layout.appendChild(muteButtonEl);
			}
			// ---------------BUTTONS-----------------
			for (Button button : kbdLayout.getButtons()) {
				Element buttonEl = doc.createElement("button");
				setSizeOfPhysicalButton(buttonEl, button);
				
				Element key = doc.createElement("key");
				text = doc.createTextNode(button.getKey().getId() + "");
				key.appendChild(text);
				buttonEl.appendChild(key);
				
				// workaround for double mode buttons (two phys. shift buttons)
				ArrayList<Integer> workaroundList = new ArrayList<Integer>();
				for (Entry<ModeButton, Key> mode : button.getModes().entrySet()) {
					if (workaroundList.contains(mode.getKey().getModeKey().getId())) {
						continue;
					}
					workaroundList.add(mode.getKey().getModeKey().getId());
					Element modeEl = doc.createElement("mode");
					modeEl.setAttribute("modename", mode.getKey().getModeKey().getId() + "");
					text = doc.createTextNode(mode.getValue().getId() + "");
					modeEl.appendChild(text);
					buttonEl.appendChild(modeEl);
				}
				layout.appendChild(buttonEl);
			}
			// ---------------MODEBUTTONS-----------------
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
		el.setAttribute("size_x", ((int) button.getOrigSize().getWidth()) + "");
		el.setAttribute("size_y", ((int) button.getOrigSize().getHeight()) + "");
		el.setAttribute("pos_x", button.getPos_x() + "");
		el.setAttribute("pos_y", button.getPos_y() + "");
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
