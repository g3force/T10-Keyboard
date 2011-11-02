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

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.view.menus.MenuBar;
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
	private static Presenter		instance;
	private JPanel						contentPane;
	private boolean					initilized			= false;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	  * 
	  */
	private Presenter() {
		instance = this; // VERRRRRY IMPORTANT, IF YOU DONT WANT TO HAVE MULTIPLE KEYBOARDS
		logger.debug("Initializing...");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("data/icons/useacc_logo.png"));
		this.setTitle("T10 On-Screen Keyboard");
		this.setAlwaysOnTop(true);
		this.setFocusableWindowState(false); // Window can't be focussed, so you can type at your current position with
															// the On-Screen Keyboard
		this.setVisible(true);
		this.addWindowListener(Controller.getInstance());

		// get a reference to the content pane
		contentPane = (JPanel) getContentPane();
		contentPane.add(MainPanel.getInstance());
		this.setJMenuBar(new MenuBar());
		
		// build GUI
		pack();

		initilized = true;
		logger.debug("Initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public static Presenter getInstance() {
		if (instance == null) {
			instance = new Presenter();
		}
		return instance;
	}
	
	
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
