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

import edu.dhbw.t10.manager.profile.LayoutFileManager;
import edu.dhbw.t10.type.Key;


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
	
	
	// private KeyboardPanel keyboardPanel;
	// private MutePanel mutePanel;
	// private ProfilePanel profilePanel;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public MainPanel() {
		this.setLayout(new FlowLayout());
		JButton btn = new JButton();
		LayoutFileManager lfm = new LayoutFileManager();
		for (Key key : lfm.getKeys()) {
			btn = new JButton();
			btn.setLayout(null);
			btn.setPreferredSize(new Dimension(100, 50));
			btn.setText(key.getName());
			this.add(btn);
		}
		

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
