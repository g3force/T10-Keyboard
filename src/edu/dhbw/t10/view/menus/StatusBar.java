/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 09.11.2011
 * Author(s): DanielAl
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view.menus;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.apache.log4j.Logger;


/**
 * Statusbar, where different btf things could be displayed for 5 Seconds...
 * TODO DanielAl Comments
 * @author DanielAl
 * 
 */
public class StatusBar extends JLabel implements Runnable {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	/**  */
	private static final long		serialVersionUID	= 5692213469714617751L;
	private static final Logger	logger				= Logger.getLogger(StatusBar.class);


	private LinkedList<String>		messageQueue;
	private Thread						thread				= null;
	private int							delay					= 2000;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public StatusBar(int align) {
		super();
		super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		messageQueue = new LinkedList<String>();
		this.setHorizontalAlignment(align);
	}


	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public void enqueueMessage(String message) {
		messageQueue.add(message);
		processQueue();
	}


	private void processQueue() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}


	@Override
	public void run() {
		while (!messageQueue.isEmpty()) {
			setMessage(messageQueue.getFirst());
			messageQueue.removeFirst();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException err) {
				logger.warn("Can't wait for Statusbar...");
			}
		}
		thread = null;
		setMessage("");
		Thread.yield();
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------

	/**
	 * Set the text of the StatusBar.
	 * 
	 * @param message
	 * @author DanielAl
	 */
	private void setMessage(String message) {
		setText(" " + message);
	}
}
