/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 20, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;

import org.apache.log4j.Logger;


/**
 * This class is the body of one Button on the keyboard. It contains information
 * about the names and keycodes for the several modes like default, shift and alt_gr
 * 
 * @author NicolaiO
 * 
 */
public class Button extends PhysicalButton implements MouseListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger				= Logger.getLogger(Button.class);
	private static final long		serialVersionUID	= 6949715976373962684L;
	private HashMap<ModeKey, Key>	modes					= new HashMap<ModeKey, Key>();
	private Key							key;
	private ArrayList<ModeKey>		activeModes			= new ArrayList<ModeKey>();
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Create a new Button with given size and position
	 * 
	 * @param size_x
	 * @param size_y
	 * @param pos_x
	 * @param pos_y
	 * @author NicolaiO
	 */
	public Button(int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		addMouseListener(this);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Add a ModeButton to this Button. This saves the ModeButton in a mode-list and registers as an observer to change
	 * its mode when ModeButton is pressed.
	 * 
	 * @param mode ModeButton that this Button should be bind to
	 * @param accordingKey Key, that should be pressed, when ModeButton is active and Button is pressed
	 * @author NicolaiO
	 */
	public void addMode(ModeKey mode, Key accordingKey) {
		modes.put(mode, accordingKey);
		mode.register(this);
	}
	
	
	/**
	 * activate a mode. If the button is pressed after this, it will react according to mode preferences.
	 * If more than one mode is active afterwards, currently the Button text is set to default!
	 * 
	 * @param mode ModeButton, that should be activated
	 * @author NicolaiO
	 */
	public void addCurrentMode(ModeKey mode) {
		if (!activeModes.contains(mode)) {
			activeModes.add(mode);
		}
		if (activeModes.size() == 1) {
			if (modes.get(mode) != null) {
				setText(modes.get(mode).getName());
			} else {
				logger.warn("addCurrentMode called with invalid mode!");
			}
		} else {
			// If no mode or more than one mode is active, just set ButtonText to default...
			// TODO OPTIONAL NicolaiO support multi-modes
			setText(key.getName());
			logger.warn("Multi-ModeButtons not implemented yet! Show default key name as Button Text.");
		}
	}
	
	
	/**
	 * Remove a Mode again, if Mode is not longer active.
	 * 
	 * @param mode ModeButton, that should be deactivated
	 * @author NicolaiO
	 */
	public void rmCurrentMode(ModeKey mode) {
		activeModes.remove(mode);
		if (activeModes.size() == 1) {
			setText(modes.get(activeModes.get(0)).getName());
		} else {
			setText(key.getName());
		}
	}
	
	
	/**
	 * Return all keys, that are currently pressed, including mode keys!
	 * 
	 * @return
	 * @author DirkK
	 */
	public ArrayList<Key> getSingleKey() {
		ArrayList<Key> output = new ArrayList<Key>();
		if (activeModes.size() == 0) {
			output.add(key);
		} else if (activeModes.size() == 1 && modes.containsKey(activeModes.get(0))) {
			output.add(modes.get(activeModes.get(0)));
		} else {
			for (ModeKey modeKey : activeModes) {
				output.add(modeKey);
			}
			output.add(modes.get("default"));
		}
		return output;
	}
	
	
	/**
	 * Unset/release all currently active ModeButtons
	 * 
	 * @author NicolaiO
	 */
	public void unsetPressedModes() {
		ArrayList<ModeKey> tactiveModes = new ArrayList<ModeKey>();
		for (ModeKey b : activeModes) {
			if (b.getState() == ModeKey.PRESSED) {
				tactiveModes.add(b);
			}
		}
		for (ModeKey b : tactiveModes) {
			b.release();
		}
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
			if (e.getSource() instanceof JButton) {
				// press shift
				for (ModeKey mb : modes.keySet()) {
					if (mb.getName().toLowerCase().equals("shift")) {
						this.addCurrentMode(mb);
						break;
					}
				}
				// press key button (SHIFT_MASK not really used)
				this.actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this
						.getActionCommand(), ActionEvent.SHIFT_MASK));
				// release shift
				for (ModeKey mb : modes.keySet()) {
					if (mb.getName().toLowerCase().equals("shift")) {
						this.rmCurrentMode(mb);
						break;
					}
				}
				JButton b = (JButton) e.getSource();
				b.getModel().setPressed(false);
			}
		}
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
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	public HashMap<ModeKey, Key> getModes() {
		return modes;
	}
	
	
	public void setModes(HashMap<ModeKey, Key> modes) {
		this.modes = modes;
	}
	
	
	public ArrayList<ModeKey> getActiveModes() {
		return activeModes;
	}
	
	
	public Key getKey() {
		return key;
	}
	
	
	public void setKey(Key key) {
		this.key = key;
		setText(key.getName());
	}
}
