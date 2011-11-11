/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 11.11.2011
 * Author(s): DanielAl
 *
 * *********************************************************
 */
package edu.dhbw.t10.view.menus;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * This class provides a JPanel with two StatusBars...
 * 
 * @author DanielAl
 * 
 */
public class StatusPane extends JPanel {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	/**  */
	private static final long	serialVersionUID	= -189926188731044511L;
	
	public static final byte	LEFT					= 1;
	public static final byte	RIGHT					= 2;
	
	private StatusBar				statusBarL;
	private StatusBar				statusBarR;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Adds two Statusbars to the Panel, left and right. The Border is set as black.
	 * 
	 * @author DanielAl
	 */
	public StatusPane() {
		statusBarL = new StatusBar(JLabel.LEFT, 2000);
		statusBarR = new StatusBar(JLabel.RIGHT, 0);
		this.setLayout(new GridLayout(1, 2));
		this.add(statusBarL);
		this.add(statusBarR);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Enqueue a Message to the left or right StatusBar.
	 * 
	 * @param message
	 * @param pos
	 * @author DanielAl
	 */
	public void enqueueMessage(String message, byte pos) {
		switch (pos) {
			case LEFT:
				statusBarL.enqueueMessage(message);
				break;
			case RIGHT:
				statusBarR.enqueueMessage(message);
				break;
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
}
