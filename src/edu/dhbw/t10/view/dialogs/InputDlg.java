/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 11.11.2011
 * Author(s): felix
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.view.menus.EMenuItem;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix
 * 
 */
public class InputDlg extends JDialog {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long	serialVersionUID	= -7212140066266985858L;
	private JLabel					textLbl;
	private JTextField			textField;
	private JButton				okBtn;
	private JButton				cancelBtn;
	private EMenuItem				menuItem;
	private InputDlg				mhhh;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * TODO FelixP, add comment!
	 * 
	 * @param menuItem
	 * @param title
	 * @param text
	 * @author FelixP
	 */
	public InputDlg(final EMenuItem menuItem, String title, String text) {
		this.setTitle(title);
		this.setModalityType(null);
		this.mhhh = this;
		
		this.menuItem = menuItem;
		
		textLbl = new JLabel(text);
		textField = new JTextField();
		okBtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().eIsInputDlg(menuItem, mhhh);
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		this.add(textLbl, BorderLayout.NORTH);
		this.add(textField, BorderLayout.CENTER);
		
		JPanel p = new JPanel();
		p.add(okBtn, BorderLayout.WEST);
		p.add(cancelBtn, BorderLayout.EAST);
		this.add(p, BorderLayout.SOUTH);
		
		this.pack();
		this.setVisible(true);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public void addActionListener(ActionListener al) {
		
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public String getProfileName() {
		return textField.getText();
	}
	
	
	public void setLblText(String text) {
		textLbl.setText(text);
	}
}
