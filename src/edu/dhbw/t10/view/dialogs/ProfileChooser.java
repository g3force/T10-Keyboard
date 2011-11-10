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

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import edu.dhbw.t10.manager.Controller;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix
 * 
 */
public class ProfileChooser extends JFileChooser {
	/**  */
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1033076958567424395L;
	private int						type;


	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ProfileChooser(int menuType, final JFrame container) {
		type = menuType;
		switch (menuType) {
			case 0: // import
			case 2: // Extend Dictionary By Text
				setDialogType(JFileChooser.OPEN_DIALOG);
				break;
			case 1: // export
				setDialogType(JFileChooser.SAVE_DIALOG);
				break;
		}
		
		// ActionListener for OK/Cancel buttons
		 ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String command = actionEvent.getActionCommand();
				if (command.equals(JFileChooser.APPROVE_SELECTION) || command.equals(JFileChooser.CANCEL_SELECTION)) {
					container.setVisible(false);
				}
			}
		 };

		addActionListener(al);
		addActionListener(Controller.getInstance());
		container.setVisible(true);
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------

	/**
	 * 
	 */
	public int getMenuType() {
		return type;
	}
}
