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
 * Statusbar, where different things could be displayed for 2 Seconds...
 * 
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
	private int							delay;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Creates a StatusBar as a JLabel, sets the Border, the align and the delay of the messages.
	 * 
	 * @param align
	 * @author DanielAl
	 */
	public StatusBar(int align, int delay) {
		super();
		messageQueue = new LinkedList<String>();
		this.setHorizontalAlignment(align);
		this.delay = delay;
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Adds a message to the messageQueue and calls the processQueue().<br>
	 * 
	 * @param message
	 * @author DanielAl
	 */
	public void enqueueMessage(String message) {
		switch (delay) {
			case 0:
				setMessage(message);
				break;
			default:
				messageQueue.add(message);
				processQueue();
		}
		
	}
	
	
	/**
	 * Starts a new thread for processing the Queue (run()), if no Thread exist. Otherwise it do nothing.<br>
	 * 
	 * @author DanielAl
	 */
	private void processQueue() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	
	/**
	 * Process the messageQueue and set text of the Statusbar.<br>
	 * Each message is displayed 2 seconds.<br>
	 * If the Queue is empty the thread is yield and the thread variable is set to null, so that processQueue() generates
	 * a new Thread if necessary.<br>
	 * 
	 * @author DanielAl
	 * @Override
	 */
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
