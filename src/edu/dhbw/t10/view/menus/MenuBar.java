/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 21, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view.menus;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MenuBar extends JMenuBar {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long			serialVersionUID	= -2903181098465204289L;
	protected static final Object[]	eventCache			= null;
	private static MenuBar				instance;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private MenuBar() {
		// sample for creating a Menu and a menu item.
		// ActionListener might be outsourced ;) e.g. by implementing it or by creating a new class
		JMenu mFile = new JMenu("File");
		JMenuItem iClose = new JMenuItem("Close");
		JMenu mProfile = new JMenu("Profile");
		final JMenuItem iChange = new JMenuItem("Change Name");
		JMenuItem iT2D = new JMenuItem("Extend Dictionary By Text");
		JMenuItem iF2D = new JMenuItem("Extend Dictionary From File");
		JMenuItem iD2F = new JMenuItem("Export Dictionary To File");
		
		mProfile.add(iChange);
		add(mProfile);
		ActionListener profile = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// switch () {
				// case 0:
				if (e.getSource().equals(iChange)) {
					Component source = (Component) e.getSource();
					Object response = JOptionPane.showInputDialog(source, "Where would you like to go to lunch?",
							"Select a Destination", ABORT, null, eventCache, JOptionPane.PLAIN_MESSAGE);
					// break;
				}
			}
		};
		iChange.addActionListener(profile);
		iClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		mFile.add(iClose);
		add(mFile);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public static MenuBar getInstance() {
		if (instance == null) {
			instance = new MenuBar();
		}
		return instance;
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
