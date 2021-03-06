/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 21, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.view.menus;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import edu.dhbw.t10.manager.Controller;
import edu.dhbw.t10.view.dialogs.AboutDlg;
import edu.dhbw.t10.view.dialogs.DialogContainer;
import edu.dhbw.t10.view.dialogs.InputDlg;
import edu.dhbw.t10.view.dialogs.ProfileCleanerDlg;


/**
 * As class name says: This class represents the MenuBar and all the included menus
 * The ActionListeners are also directly implemented here...
 * 
 * T O D O FelixP optional menu item "Modify"
 * 
 * @author NicolaiO
 */
public class MenuBar extends JMenuBar {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long			serialVersionUID	= -2903181098465204289L;
	protected static final Object[]	eventCache			= null;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Create a MenuBar with all its items and define action events
	 * 
	 * @author NicolaiO
	 */
	public MenuBar() {
		// File Menu
		JMenu mFile = new JMenu("Keyboard");
		JMenuItem iNewProfile = new JMenuItem("New Profile");
		JMenuItem iImport = new JMenuItem("Import Profile");
		JMenuItem iClose = new JMenuItem("Close");
		
		// ProfileMenu
		JMenu mProfile = new JMenu("Profile");
		// JMenuItem iChange = new JMenuItem("Modify");
		JMenuItem iExport = new JMenuItem("Export Profile");
		JMenuItem iT2D = new JMenuItem("Extend Dictionary By Text");
		JMenuItem iF2D = new JMenuItem("Extend Dictionary From File");
		JMenuItem iD2F = new JMenuItem("Export Dictionary To File");
		JMenuItem iClean = new JMenuItem("Clean Dictionary");
		JMenuItem iDelete = new JMenuItem("Delete");
		
		// Help Menu
		JMenu mHelp = new JMenu("Help");
		JMenuItem iAbout = new JMenuItem("About");

		// add menus to GUI
		add(mFile);
		add(mProfile);
		add(mHelp);
		mFile.add(iNewProfile);
		mFile.add(iImport);
		mFile.add(iClose);
		// mProfile.add(iChange);
		mProfile.add(iExport);
		mProfile.add(iT2D);
		mProfile.add(iF2D);
		mProfile.add(iD2F);
		mProfile.add(iClean);
		mProfile.add(iDelete);
		mHelp.add(iAbout);
		
		
		// Action Listener for menu items
		// iChange.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		//				
		// }
		// });
		
		iNewProfile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new InputDlg(EMenuItem.iNewProfile, "New Profile", "Name of profile:");
			}
		});
		
		iImport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogContainer(EMenuItem.iImport);
			}
		});
		
		iExport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogContainer(EMenuItem.iExport);
			}
		});
		
		iClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().closeSuperFelix();
				System.exit(0);
			}
		});
		
		// iChange.addActionListener(new ActionListener() {
		//			
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// // TODO FelixP Menu bearbeiten(eingabe: Name und Pfad, vor ausgefuellt)
		// }
		// });
		
		iT2D.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogContainer(EMenuItem.iT2D);
			}
		});

		iF2D.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogContainer(EMenuItem.iF2D);
			}
		});

		iD2F.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new DialogContainer(EMenuItem.iD2F);
			}
		});

		iClean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ProfileCleanerDlg();
			}
		});
		
		iDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().deleteActiveProfile();
			}
		});
		
		iAbout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AboutDlg();
			}
		});
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
