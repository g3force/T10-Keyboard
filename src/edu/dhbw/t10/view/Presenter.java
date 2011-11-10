/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 15, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.view.menus.MenuBar;
import edu.dhbw.t10.view.menus.StatusBar;
import edu.dhbw.t10.view.panels.MainPanel;


/**
 * This is the main Window. This class initializes settings for the window and adds the MainPanel.
 * @author NicolaiO, DanielAl
 * 
 */
public class Presenter extends JFrame {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger				= Logger.getLogger(Presenter.class);
	private static final long		serialVersionUID	= 6217926957357225677L;
	private JPanel						contentPane;
	private boolean					initilized			= false;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	  * 
	  */
	public Presenter(MainPanel mainPanel, StatusBar statusBarL, StatusBar statusBarR) {
		logger.debug("Initializing...");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("data/icons/useacc_logo.png"));
		this.setTitle("T10 On-Screen Keyboard");
		this.setAlwaysOnTop(true);
		// Window can't be focussed, so you can type at your current position with the On-Screen Keyboard
		this.setFocusableWindowState(false);
		this.setVisible(true);
		this.addWindowListener(Controller.getInstance());

		// get a reference to the content pane
		contentPane = (JPanel) getContentPane();
		contentPane.add(mainPanel);
		JPanel statusPane = new JPanel();
		statusPane.setLayout(new FlowLayout());
		// statusPane.add(statusBarL, java.awt.BorderLayout.LINE_START);
		// statusPane.add(statusBarR, java.awt.BorderLayout.LINE_END);
		statusPane.add(statusBarL, java.awt.BorderLayout.LINE_START);
		statusPane.add(statusBarR, java.awt.BorderLayout.LINE_END);
		statusPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(statusPane, java.awt.BorderLayout.SOUTH);
		this.setJMenuBar(new MenuBar());
		
		// build GUI
		pack();

		initilized = true;
		logger.debug("Initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public boolean isInitilized() {
		return initilized;
	}
	
	
	public void setInitilized(boolean initilized) {
		this.initilized = initilized;
	}
}
