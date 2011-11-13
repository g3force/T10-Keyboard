/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: 06.11.2011
 * Author(s): FelixP
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.view.menus.EMenuItem;


/**
 * 
 * Modified JFileChosser ....non-modal :-)
 * 
 * @author FelixP
 * 
 */
public class ProfileChooser extends JFileChooser {
	/**  */
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long	serialVersionUID	= 1033076958567424395L;
	private EMenuItem				type;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * 
	 * Constructor of ProfileChosser. Need to know the Container
	 * 
	 * @param menuType
	 * @param container
	 * @author FelixP
	 */
	public ProfileChooser(EMenuItem menuType, final JFrame container) {
		type = menuType;
		switch (menuType) {
			case iT2D: // Extend Dictionary By Text
			
				this.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.getName().toLowerCase().endsWith(".zip");
					}
					
					@Override
					public String getDescription() {
						return "zip Files";
					}
				});
				
				setDialogType(JFileChooser.OPEN_DIALOG);
				break;

			case iImport: // import
				setDialogType(JFileChooser.OPEN_DIALOG);
				break;
			case iExport: // export
			
				this.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.getName().toLowerCase().endsWith(".zip");
					}
					

					@Override
					public String getDescription() {
						return "zip Files";
					}
				});
				setDialogType(JFileChooser.SAVE_DIALOG);
				break;

			case iF2D:
				this.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.getName().toLowerCase().endsWith(".tree");
					}
					

					@Override
					public String getDescription() {
						return "tree Files";
					}
				});
				
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
	public EMenuItem getMenuType() {
		return type;
	}
}
