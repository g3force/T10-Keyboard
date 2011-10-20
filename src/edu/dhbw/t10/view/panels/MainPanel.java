/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 18, 2011
 * Author(s): NicolaiO
 *
 * *********************************************************
 */
package edu.dhbw.t10.view.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MainPanel extends JPanel
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final long	serialVersionUID	= -52892520461804389L;
	private KeyboardPanel		keyboardPanel;
	private MutePanel				mutePanel;
	private ProfilePanel			profilePanel;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public MainPanel()
	{
		this.setLayout(new FlowLayout());
		JButton btns[] = new JButton[10];
		for (int i = 0; i < 10; i++)
		{
			btns[i] = new JButton();
			btns[i].setLayout(null);
			btns[i].setPreferredSize(new Dimension(100, 50));
			this.add(btns[i]);
		}
		
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//			//NodeList nList = doc.getElementsByTagName("key");
//			System.out.println("-----------------------");
//			
//			for (int temp = 0; temp < nList.getLength(); temp++)
//			{
//				
//				Node nNode = nList.item(temp);
//				if (nNode.getNodeType() == Node.ELEMENT_NODE)
//				{
//					
//					Element eElement = (Element) nNode;
//					System.out.println(nNode);
//					
//					System.out.println("First Name : " + getTagValue("name", eElement));
//					System.out.println("Last Name : " + getTagValue("id", eElement));
//					System.out.println("Nick Name : " + getTagValue("keycode", eElement));
//					System.out.println("Salary : " + getTagValue("size_x", eElement));
//					
//				}
//			}

		
		// this.setLayout(new BorderLayout());
		// this.setSize(300, 150);
		// keyboardPanel = new KeyboardPanel();
		// mutePanel = new MutePanel();
		// profilePanel = new ProfilePanel();
		// this.add(keyboardPanel, BorderLayout.SOUTH);
		// // TODO new Layout
		// this.add(mutePanel, BorderLayout.NORTH);
		// this.add(profilePanel, BorderLayout.NORTH);
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
