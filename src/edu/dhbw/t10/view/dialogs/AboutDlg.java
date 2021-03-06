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
import javax.swing.JPanel;

import edu.dhbw.t10.SuperFelix;


/**
 * 
 * Dialog for About menu...
 * 
 * @author felix
 * 
 */
public class AboutDlg extends JDialog {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private JButton	likeBtn;
	private JButton				codeBtn;
	private ImageIcon	icon;
	private static final long	serialVersionUID	= -3739014603528510969L;
	

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
				openBrowser("https://plus.google.com/100091571390634776061/posts");
				openBrowser("http://www.facebook.com/UseAcc");
			}
		});
		
		codeBtn = new JButton("Code");
		codeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				openBrowser("http://code.google.com/p/t10-onscreen-keyboard/");
			}
		});

		JLabel iconLbl = new JLabel();
		icon = new ImageIcon(getClass().getResource("/res/icons/logo_mittel.png"));
		if (icon != null) {
			iconLbl = new JLabel(icon);
		}


		JLabel titleLbl = new JLabel("<html>On-Screen keyboard with intelligent word completition<br>Version: "
				+ SuperFelix.VERSION + "<br>Git Revision: " + SuperFelix.REV + "</html>");
		titleLbl.setAlignmentX(CENTER_ALIGNMENT);
		titleLbl.setAlignmentY(CENTER_ALIGNMENT);
		JLabel descriptionLbl = new JLabel("<html>A student project in <br>" + "the lecture Software Engineering, <br>"
				+ "3. semester, DHBW Mannheim.</html>");
		JLabel authorLbl = new JLabel(
				"<html>Daniel Andres Lopez, Nicolai Ommer,<br>Dirk Klostermann, Sebastian Nickel,<br>Felix Pistorius<html>");
		
		
		JPanel centerPnl = new JPanel();
		centerPnl.add(iconLbl, BorderLayout.WEST);
		centerPnl.add(descriptionLbl, BorderLayout.EAST);
		
		JPanel southPnl = new JPanel();
		southPnl.add(authorLbl, BorderLayout.WEST);
		southPnl.add(codeBtn, BorderLayout.CENTER);
		southPnl.add(likeBtn, BorderLayout.EAST);

		this.add(titleLbl, BorderLayout.NORTH);
		this.add(centerPnl, BorderLayout.CENTER);
		// this.add(iconLbl, BorderLayout.WEST);
		// this.add(eastPnl, BorderLayout.EAST);
		// this.add(likeBtn, BorderLayout.SOUTH);
		this.add(southPnl, BorderLayout.SOUTH);

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
