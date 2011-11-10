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


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.dhbw.t10.view.dialogs.DialogContainer;


/**
 * As class name says: This class represents the MenuBar and all the included menus
 * The ActionListeners are also directly implemented here...
 * 
 * @author NicolaiO
 */
public class MenuBar extends JMenuBar {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long			serialVersionUID	= -2903181098465204289L;
	protected static final Object[]	eventCache			= null;
	private static int					inc					= 1;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public MenuBar() {
		// File Menu
		JMenu mFile = new JMenu("File");
		JMenuItem iNewProfile = new JMenuItem("New Profile");
		JMenuItem iImport = new JMenuItem("Import Profile");
		JMenuItem iExport = new JMenuItem("Export Profile");
		JMenuItem iClose = new JMenuItem("Close");
		
		// ProfileMenu
		JMenu mProfile = new JMenu("Profile");
		JMenuItem iChange = new JMenuItem("Modify");
		JMenuItem iT2D = new JMenuItem("Extend Dictionary By Text");
		JMenuItem iF2D = new JMenuItem("Extend Dictionary From File");
		JMenuItem iD2F = new JMenuItem("Export Dictionary To File");
		JMenuItem iClean = new JMenuItem("Clean Dictionary");
		JMenuItem iDelete = new JMenuItem("Delete");
		
		// add menus to GUI
		add(mFile);
		add(mProfile);
		mFile.add(iNewProfile);
		mFile.add(iImport);
		mFile.add(iExport);
		mFile.add(iClose);
		mProfile.add(iChange);
		mProfile.add(iT2D);
		mProfile.add(iF2D);
		mProfile.add(iD2F);
		mProfile.add(iClean);
		mProfile.add(iDelete);

		
		// Action Listener for menu items
		iChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		iNewProfile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object response = JOptionPane.showInputDialog((Component) e.getSource(),
						"Geben Sie den Namen des neuen Profils an.", "Neues Profil anlegen", JOptionPane.PLAIN_MESSAGE, null,
						eventCache, "");
			}
		});
		
		iImport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// ProfileChooser pc = new ProfileChooser(0);
				DialogContainer pc = new DialogContainer(0);
			}
		});
		
		iExport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// ProfileChooser pc = new ProfileChooser(1);
				DialogContainer pc = new DialogContainer(1);
			}
		});

		iClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		iChange.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FelixP Menu bearbeiten(eingabe: Name und Pfad, vor ausgefüllt)
			}
		});
		
		iT2D.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FelixP Menu
				// ProfileChooser pc = new ProfileChooser(2);
				DialogContainer pc = new DialogContainer(2);
			}
		});
		
		iF2D.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FelixP Menu
			}
		});
		
		iD2F.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FelixP Menu
			}
		});
		
		iClean.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO FelixP Menu autoLöschen (profilliste, Datum, Wortanzahl
			}
		});
		
		iDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Controller.getInstance().deleteProfile("Profile3");
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
