/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager.profile;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class LayoutFileManager
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private File							layoutFile;
	private DocumentBuilderFactory	dbFactory;
	private DocumentBuilder				dBuilder;
	private Document						doc;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public LayoutFileManager()
	{
		layoutFile = new File("conf/keyboard_layout_de_default.xml");
		dbFactory = DocumentBuilderFactory.newInstance();
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(layoutFile);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException err)
		{
			// TODO Auto-generated catch block
			err.printStackTrace();
		} catch (SAXException err)
		{
			// TODO Auto-generated catch block
			err.printStackTrace();
		} catch (IOException err)
		{
			// TODO Auto-generated catch block
			err.printStackTrace();
		}

	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------

	// private static String getTagValue(String sTag, Element eElement)
	// {
	// NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	//
	// Node nValue = (Node) nlList.item(0);
	//
	// return nValue.getNodeValue();
	// }
	//
	//
	// private static String getTagAttribute(String sTag, String arg, Element eElement)
	// {
	// NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getAttributes().getNamedItem(arg);
	//
	// Node nValue = (Node) nlList.item(0);
	//
	// return nValue.getNodeValue();
	// }
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
