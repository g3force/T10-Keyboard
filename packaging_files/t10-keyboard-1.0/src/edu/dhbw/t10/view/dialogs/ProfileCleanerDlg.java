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
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.view.menus.EMenuItem;


/**
 * Non-Modal dialog for menu item: "Clean Dictionary".
 * 
 * @author FelixP
 * 
 */
public class ProfileCleanerDlg extends JDialog {
	private static final long			serialVersionUID	= -8794372924172644219L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private JSpinner						dateField;
	private JSpinner						spinField;
	private JButton						okBtn;
	private JButton						cancelBtn;
	private final ProfileCleanerDlg	mhh;
	private Calendar						calendar;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ProfileCleanerDlg() {
		calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date initDate = calendar.getTime();
		calendar.add(Calendar.YEAR, -100);
		Date earliestDate = calendar.getTime();
		SpinnerDateModel dateModel = new SpinnerDateModel(initDate, earliestDate, initDate, Calendar.YEAR);
		dateField = new JSpinner(dateModel);
		dateField.setEditor(new JSpinner.DateEditor(dateField, "d.MM.yyyy"));

		SpinnerNumberModel numModel = new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1);
		spinField = new JSpinner(numModel);


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

		JPanel p1 = new JPanel();
		p1.add(okBtn, BorderLayout.WEST);
		p1.add(cancelBtn, BorderLayout.EAST);

		this.add(dateField, BorderLayout.NORTH);
		this.add(spinField, BorderLayout.CENTER);
		this.add(p1, BorderLayout.SOUTH);
		
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
	

	public Date getDate() {
		return (Date) dateField.getValue();
	}
}
