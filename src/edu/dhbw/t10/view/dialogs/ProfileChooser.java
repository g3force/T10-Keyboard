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
	JFileChooser	fc;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ProfileChooser(int dialogType) {
		fc = new JFileChooser();
		fc.setDialogType(dialogType);

		getContentPane().add(fc, "Center");

		pack();
		setVisible(true);
	}

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
