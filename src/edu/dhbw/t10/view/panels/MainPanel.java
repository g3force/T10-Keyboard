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

import edu.dhbw.t10.manager.profile.ProfileManager;
import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.keyboard.key.PhysicalButton;
import edu.dhbw.t10.view.Presenter;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author NicolaiO
 * 
 */
public class MainPanel extends JPanel implements ComponentListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final long		serialVersionUID	= -52892520461804389L;
	private static MainPanel		instance;
	private static final Logger	logger				= Logger.getLogger(MainPanel.class);
	private boolean					initilized			= false;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	private MainPanel() {
		logger.debug("Initializing...");
		this.setLayout(null);
		this.addComponentListener(this);
		logger.debug("Initializied.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public static MainPanel getInstance() {
		if (instance == null) {
			instance = new MainPanel();
		}
		return instance;
	}
	
	
	public void setKbdLayout(KeyboardLayout kbd) {
		logger.debug("adding Layout...");
		this.setPreferredSize(new Dimension(kbd.getSize_x(), kbd.getSize_y()));
		for (PhysicalButton button : kbd.getAllPhysicalButtons()) {
			this.add(button);
		}
		for (DropDownList ddl : kbd.getDdls()) {
			this.add(ddl);
		}
		initilized = true;
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
		if (initilized && Presenter.getInstance().isInitilized() && e.getComponent().getSize().height != 0
				&& e.getComponent().getSize().width != 0) {// do not delete Presenter is initialized, rescale
																						// before GUI has a size
			logger.debug("Window resized handler called");
			ProfileManager.getInstance().resizeWindow(e.getComponent().getSize());
		}
	}
	
	
	@Override
	public void componentShown(ComponentEvent e) {
		
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
