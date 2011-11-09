/* 
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 06.11.2011
 * Author(s): felix
 *
 * *********************************************************
 */
package edu.dhbw.t10.view.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix
 * 
 */
public class ProfileChooser extends JFrame {
	/**  */
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1033076958567424395L;
	private JFileChooser			fc;
	private boolean				approve				= false;
	File								selectedFile;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ProfileChooser(int dialogType) {
		fc = new JFileChooser();
		fc.setDialogType(dialogType);

		getContentPane().add(fc, "Center");
		pack();
		
		// ActionListener for OK/Cancel buttons
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				 JFileChooser fileChooser = (JFileChooser) actionEvent.getSource();
				String command = actionEvent.getActionCommand();
				
				// OK button
				if (command.equals(JFileChooser.APPROVE_SELECTION)) {
					// set flag and selected file
					selectedFile = fileChooser.getSelectedFile();
					approve = true;

					setVisible(false);
					
					// Cancel button
				} else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
					// do nothing...
					setVisible(false);
				}
			}
		};

		fc.addActionListener(al);
		setVisible(true);
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * @return type of dialog
	 * @author felix
	 */
	public int getDialogType() {
		return fc.getDialogType();
	}
	

	/**
	 * 
	 * @return status of dialog: true for approve and false for cancel
	 * @author felix
	 */
	public boolean isApproved() {
		return approve;
	}
	

	/**
	 * 
	 * @return name of profile
	 * @author felix
	 */
	public String getProfileName() {
		if (approve) {
			return selectedFile.getName();
		}
		
		return "";
	}
	

	/**
	 * 
	 * @return path to profile
	 * @author felix
	 */
	public String getPathToProfile() {
		if (approve) {
			return selectedFile.getParent();
		}
		
		return "";
	}
}
