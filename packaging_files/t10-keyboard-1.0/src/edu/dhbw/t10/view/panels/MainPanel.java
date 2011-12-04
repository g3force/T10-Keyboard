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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.Image;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.keyboard.key.PhysicalButton;


/**
 * This is the main Panel for the GUI where the keyboard layout is placed on.
 * It adds all PhysicalButtons and DropDownLists to itself and reacts on window resizing
 * 
 * @author NicolaiO
 * 
 */
public class MainPanel extends JPanel implements ComponentListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final long		serialVersionUID	= -52892520461804389L;
	private static final Logger	logger				= Logger.getLogger(MainPanel.class);
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Create a new empty MainPanel
	 * 
	 * @author NicolaiO
	 */
	public MainPanel() {
		logger.debug("Initializing...");
		this.setLayout(null);
		this.addComponentListener(this);
		logger.debug("Initializied.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * set KeyboardLayout for this Panel and add everything in the layout to itself
	 * 
	 * @param kbd
	 * @author NicolaiO
	 */
	@Deprecated
	public void setKbdLayout(KeyboardLayout kbd) {
		logger.debug("adding Layout...");
		this.setPreferredSize(new Dimension(kbd.getSize_x(), kbd.getSize_y()));
		this.removeAll();
		for (PhysicalButton button : kbd.getAllPhysicalButtons()) {
			this.add(button);
		}
		for (DropDownList ddl : kbd.getDdls()) {
			this.add(ddl);
		}
		for (Image img : kbd.getImages()) {
			this.add(img);
		}
		logger.debug("Layout added.");
	}
	

	@Override
	public void componentHidden(ComponentEvent e) {
		
	}
	
	
	@Override
	public void componentMoved(ComponentEvent e) {
		
	}
	
	
	@Override
	public void componentResized(ComponentEvent e) {
		// do not resize if (0,0)
		if (Controller.getInstance().isReadyForActionEvents() && e.getComponent().getSize().height != 0
				&& e.getComponent().getSize().width != 0) {
			logger.debug("Window resized handler called");
			Controller.getInstance().resizeWindow(e.getComponent().getSize());
		}
	}
	
	
	@Override
	public void componentShown(ComponentEvent e) {
		
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
