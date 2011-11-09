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

import java.util.LinkedList;

import javax.swing.JLabel;

import org.apache.log4j.Logger;


/**
 * Statusbar, where different things could be displayed for 5 Seconds...
 * TODO Thread to be able to run the overwrite method all over the time...
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


	LinkedList<String>				messageQueue;
	Thread								thread				= null;
	int									delay					= 2000;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public StatusBar() {
		super();
		// super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		messageQueue = new LinkedList<String>();
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
		setMessage("");	
		Thread.yield();
		thread = null;
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
