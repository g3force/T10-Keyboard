/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 28, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import org.apache.log4j.Logger;


/**
 * button for the different modes like shift, strg pressed
 * 
 * @author DirkK, NicolaiO
 * 
 */
public class ModeButton extends PhysicalButton implements MouseListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private static final Logger	logger				= Logger.getLogger(ModeButton.class);
	private static final long		serialVersionUID	= 5356736981172867044L;
	private ModeKey					modeKey;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Create a new ModeButton with given ModeKey and bounds
	 * 
	 * @param modeKey
	 * @param size_x
	 * @param size_y
	 * @param pos_x
	 * @param pos_y
	 * @author NicolaiO
	 */
	public ModeButton(ModeKey modeKey, int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		setModeKey(modeKey);
		modeKey.addModeButton(this);
		addMouseListener(this);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Push the button. The event will be transmitted to the occording ModeKey and thus to all other ModeButtons with
	 * same ModeKey (e.g. to all Shift Buttons)
	 * 
	 * @author NicolaiO
	 */
	public void push() {
		modeKey.push();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		/**
		 * visualize pressing button for right mouse click
		 */
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.getSource() instanceof JButton) {
				JButton b = (JButton) e.getSource();
				b.getModel().setPressed(true);
			}
		}
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		/**
		 * visualize pressing button for right mouse click
		 */
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.getSource() instanceof ModeButton) {
				// if the mouse is still within button
				if (e.getPoint().x >= 0 && e.getPoint().y >= 0 && e.getPoint().x < ((ModeButton) e.getSource()).getWidth()
						&& e.getPoint().y < ((ModeButton) e.getSource()).getHeight()) {
					// press key button
					// TODO NicolaiO do something that works...
					// Idee: dem ActionEvent ein Button übergeben und kein ModeButton, sodass der COntroller dies auch so
					// behandelt
					// leider wird trotzdem ein ModeBUtton übergeben... bitte drüberschauen und korrigieren
					Button helpB = new Button(1, 1, 1, 1);
					Key helpKey = ((ModeButton) e.getSource()).getModeKey();
					helpB.setKey(helpKey);
					logger.warn("test1");
					ActionEvent f = new ActionEvent(helpB, ActionEvent.ACTION_PERFORMED, helpB.getActionCommand());
					this.actionListener.actionPerformed(f);
					logger.warn("test2");
					// this.actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this
					// .getActionCommand()));
				}
				
				JButton b = (JButton) e.getSource();
				b.getModel().setPressed(false);
			}
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public String getModeName() {
		return modeKey.getName();
	}
	
	
	public Key getModeKey() {
		return modeKey;
	}
	
	
	public void setModeKey(ModeKey modeKey) {
		this.modeKey = modeKey;
		if (!modeKey.getDefaultIconSrc().equals("")) {
			setIcon(modeKey.getDefaultIcon());
		} else
			setText(modeKey.getName());
	}
	
	
	public int getState() {
		return modeKey.getState();
	}
}
