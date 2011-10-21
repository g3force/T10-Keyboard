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
import java.util.LinkedList;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.KeyboardLayoutGenerator;
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
public class MainPanel extends JPanel {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final long	serialVersionUID	= -52892520461804389L;
	@SuppressWarnings("unused")
	private static final Logger	logger				= Logger.getLogger(MainPanel.class);
	
	// private KeyboardPanel keyboardPanel;
	// private MutePanel mutePanel;
	// private ProfilePanel profilePanel;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public MainPanel() {
		this.setLayout(null);
		LinkedList<Key> keys = new LinkedList<Key>();
		KeyboardLayoutGenerator lfm = new KeyboardLayoutGenerator();
		KeyboardLayout kbd = lfm.getKbdLayout();
		
		this.setPreferredSize(new Dimension(kbd.getSize_x(), kbd.getSize_y()));
		String mode = "default";
		for (Key key : kbd.getKeys()) {
			key.setText(key.getName(mode));
			this.add(key);
			keys.add(key);
		}
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
