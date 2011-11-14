/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 14.11.2011
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
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

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
public class ProfileCleanerDlg extends JDialog {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private JSpinner	spinField;
	private JButton	okBtn;
	private JButton	cancelBtn;
	private final ProfileCleanerDlg	mhh;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ProfileCleanerDlg() {
		SpinnerNumberModel spinModel = new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1);
		spinField = new JSpinner(spinModel);

		okBtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");
		
		mhh = this;
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller.getInstance().eIsDlg(EMenuItem.iClean, mhh);
				setVisible(false);
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		JPanel p = new JPanel();
		p.add(okBtn, BorderLayout.WEST);
		p.add(cancelBtn, BorderLayout.EAST);

		this.add(spinField, BorderLayout.CENTER);
		this.add(p, BorderLayout.SOUTH);
		
		this.setTitle("Clean Dictionary");

		this.pack();
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	public Integer getFrequency() {
		Integer i = (Integer) spinField.getValue();
		return i;
	}
}
