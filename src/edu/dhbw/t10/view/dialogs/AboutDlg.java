/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 15.11.2011
 * Author(s): felix
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import edu.dhbw.t10.SuperFelix;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix
 * 
 */
public class AboutDlg extends JDialog {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private JButton	likeBtn;
	private ImageIcon	icon;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public AboutDlg() {
		this.setTitle("About");

		likeBtn = new JButton("like");
		likeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				openBrowser("http://www.facebook.com/UseAcc");
				openBrowser("https://plus.google.com/100091571390634776061/posts");
			}
		});

		icon = new ImageIcon(SuperFelix.class.getResource("../../../res/icons/logo_mittel.png"));
		JLabel iconLbl = new JLabel(icon);

		this.add(iconLbl, BorderLayout.CENTER);
		this.add(likeBtn, BorderLayout.SOUTH);

		this.pack();
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	private void openBrowser(String path) {
		final String os = System.getProperty("os.name").toLowerCase();
		try {
			if (os.indexOf("mac") >= 0) {
				Runtime.getRuntime().exec("open " + path);
			}

			if (os.indexOf("windows") >= 0) {
				if (os.equals("windows nt")) {
					Runtime.getRuntime().exec("cmd.exe /C " + path);
				} else if (os.equals("windows 95")) {
					Runtime.getRuntime().exec("command.com /C " + path);
				} else {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + path);
				}
			}
			
			if (os.indexOf("linux") >= 0) {
				// x-www-browser -newwindow -fullscreen "http://www.facebook.com/UseAcc"
				Runtime.getRuntime().exec("x-www-browser -newwindow -fullscreen " + path);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
