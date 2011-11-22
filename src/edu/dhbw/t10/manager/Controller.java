/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 24, 2011
 * Author(s): FelixP, DanielAl, NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.ZipException;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import edu.dhbw.t10.helper.StringHelper;
import edu.dhbw.t10.manager.output.Output;
import edu.dhbw.t10.manager.output.OutputManager;
import edu.dhbw.t10.manager.profile.ImportExportManager;
import edu.dhbw.t10.manager.profile.ProfileManager;
import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;
import edu.dhbw.t10.type.keyboard.key.ModeButton;
import edu.dhbw.t10.type.keyboard.key.MuteButton;
import edu.dhbw.t10.type.profile.Profile;
import edu.dhbw.t10.type.tree.PriorityTree;
import edu.dhbw.t10.view.Presenter;
import edu.dhbw.t10.view.dialogs.InputDlg;
import edu.dhbw.t10.view.dialogs.ProfileChooser;
import edu.dhbw.t10.view.dialogs.ProfileCleanerDlg;
import edu.dhbw.t10.view.menus.EMenuItem;
import edu.dhbw.t10.view.menus.StatusPane;
import edu.dhbw.t10.view.panels.MainPanel;


/**
 * The Controller Class provides the central interface to combine the functionality of the program. the data flows
 * through it. <br>
 * Here all Managers and the view is initialized...<br>
 * It provides overwritten methods to handles actionEvents...<br>
 * 
 * @author NicolaiO, DirkK, FelixP, SebastianN, DanielAl
 */
public class Controller implements ActionListener, WindowListener, MouseListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger					= Logger.getLogger(Controller.class);
	private static Controller		instance;
	

	
	private ProfileManager			profileMan;
	private OutputManager			outputMan;
	private MainPanel					mainPanel;
	private Presenter					presenter;
	private StatusPane				statusPane;
	
	private boolean					readyForActionEvents	= false;
	private boolean					changeProfileBlocked	= false;
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * This Constructor instantiate all other objects... The Application is loaded...<br>
	 * The Controller is implemented as a Singleton<br>
	 * 
	 * @author NicolaiO, DirkK, FelixP, SebastianN, DanielAl
	 */
	private Controller() {
		instance = this;
		logger.debug("initializing...");

		// load GUI
		mainPanel = new MainPanel();
		statusPane = new StatusPane();
		presenter = new Presenter(mainPanel, statusPane);

		outputMan = new OutputManager();

		// This message is important! Otherwise, The StatusPane has a wrong height and the layout will be decreased
		// meaning, it gets smaller with each start...
		showStatusMessage("Keyboard initializing...");
		profileMan = new ProfileManager();
		
		changeProfile(profileMan.getActive());
		
		readyForActionEvents = true;
		showStatusMessage("Keyboard initialized.");
		logger.debug("initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Save the actual Profile and dictionary to be able to close the application.
	 * 
	 * @author DirkK
	 */
	private void closeSuperFelix() {
		try {
			logger.debug("closing - saving the tree");
			profileMan.getActive().save();
			logger.debug("closing - saving the config");
			profileMan.saveConfig();
			logger.debug("closed - good BUY");
			logger.info("(c) FIT 42");
		} catch (Exception e) {
			logger.error("closing routine produced an error: " + e.toString());
		}
	}
	
	
	/**
	 * Resizes the Window and rescale the buttons to fit in there...
	 * 
	 * @param size
	 * @author NicolaiO
	 */
	public void resizeWindow(Dimension size) {
		KeyboardLayout kbdLayout = profileMan.getActive().getKbdLayout();
		if (kbdLayout != null) {
			float xscale = (float) size.width / (float) kbdLayout.getOrigSize_x();
			float yscale = (float) size.height / (float) kbdLayout.getOrigSize_y();
			float fontScale = xscale + yscale / 2;
			kbdLayout.setScale_x(xscale);
			kbdLayout.setScale_y(yscale);
			kbdLayout.setScale_font(fontScale);
			kbdLayout.rescale();
			mainPanel.setPreferredSize(new Dimension(kbdLayout.getSize_x(), kbdLayout.getSize_y()));
			presenter.pack();
			logger.debug("Window rescaled");
		}
	}


	// --------------------------------------------------------------------------
	// --- Profile --------------------------------------------------------------
	// --------------------------------------------------------------------------

	/**
	 * Creates a profile by name.
	 * 
	 * @param String name
	 * @author SebastianN
	 */
	public void addNewProfile(String name) {
		Profile profile = profileMan.createProfile(name);
		if (profile != null) {
			changeProfile(profile);
		} else {
			logger.error("can not add new profile, it could not be created!");
		}
	}
	
	
	/**
	 * Deletes the active profile
	 * 
	 * @author NicolaiO
	 */
	public void deleteActiveProfile() {
		// get active profile to be delete
		Profile todelete = profileMan.getActive();
		// get potential new profile
		Profile newProfile = profileMan.getProfiles().get(0);

		// after deleting profile, first or second profile should be made active
		if (todelete == newProfile) {
			if (profileMan.getProfiles().size() > 1) {
				newProfile = profileMan.getProfiles().get(1);
			} else {
				logger.debug("Only one or zero profiles left. Can't delete.");
				return;
			}
		}
		changeProfile(newProfile);
		profileMan.deleteProfile(todelete);
	}
	
	
	/**
	 * Change profile. This does not only <b>set</b> the active profile, but also reloads the GUI!
	 * Do not use setActive of ProfileManager alone...
	 * 
	 * TODO NicolaiO move the content of this method to Profile Manager (DirkK)
	 * 
	 * @param profile
	 * @author NicolaiO
	 */
	public void changeProfile(Profile profile) {
		if (!changeProfileBlocked) {
			changeProfileBlocked = true;
			profileMan.setActive(profile);
			mainPanel.setKbdLayout(profileMan.getActive().getKbdLayout());
			Dimension size = profileMan.getActive().getKbdLayout().getSize();
			resizeWindow(size);
			changeProfileBlocked = false;
		} else {
			logger.debug("changeProfile blocked");
		}
	}
	
	
	/**
	 * Check, if a given profile already exists.
	 * 
	 * @param name of the profile to check
	 * @return true if it exists, false else
	 * @author NicolaiO
	 */
	public boolean existProfile(String name) {
		return profileMan.existProfile(name);
	}
	
	
	// --------------------------------------------------------------------------
	// --- Statusbar interface --------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * Display a tooltip in the statusbar (It will be there, until another tooltip is set or it is manually hidden
	 * 
	 * @param message
	 * @author NicolaiO
	 */
	public void showTooltip(String message) {
		statusPane.enqueueMessage(message, StatusPane.RIGHT);
	}
	
	
	/**
	 * Hide current tooltip
	 * 
	 * @author NicolaiO
	 */
	public void hideTooltip() {
		statusPane.enqueueMessage("", StatusPane.RIGHT);
	}
	

	/**
	 * Show a status message in the statusbar. It will be enqueued and displayed, after all other message were displayed.
	 * Each message has a fixed display time.
	 * 
	 * @param message
	 * @author NicolaiO
	 */
	public void showStatusMessage(String message) {
		statusPane.enqueueMessage(message, StatusPane.LEFT);
	}


	// --------------------------------------------------------------------------
	// --- Action handler -------------------------------------------------------
	// --------------------------------------------------------------------------

	/**
	 * Starts the right activities for a specific event...
	 * The events come from Buttons, ModeButtons, MuteButtons, DDLs and other elements
	 * 
	 * @param e
	 * @author NicolaiO, DirkK, FelixP, SebastianN, DanielAl
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!readyForActionEvents) {
			logger.debug("An ActionEvent was blocked, because controller is not ready yet.");
			return;
		}

		if (e.getSource() instanceof Button) {
			logger.debug("Normal Button pressed.");
			outputMan.buttonPressed((Button) e.getSource(), profileMan.getActive());
		}
		
		if (e.getSource() instanceof ModeButton) {
			logger.debug("ModeButton pressed.");
			ModeButton modeB = (ModeButton) e.getSource();
			// currently we do not support some buttons for linux...
			if (Output.getOs() == Output.LINUX
					&& (modeB.getModeKey().getKeycode().equals("\\WINDOWS\\") || modeB.getModeKey().getKeycode()
							.equals("\\CONTEXT_MENU\\"))) {
				showStatusMessage("Button not supported by your OS");
			} else {
				if (modeB.isModesDisabled()) {
					// The helpB is created, because a Modebuttoon should with a right click treated as a normal button. So a
					// new Button with the Key of the ModeButton is created.
					Button helpB = new Button(1, 1, 1, 1);
					Key helpKey = ((ModeButton) e.getSource()).getModeKey().clone();
					helpB.setKey(helpKey);
					outputMan.buttonPressed(helpB, profileMan.getActive());
					// eIsButton(helpB);
				} else {
					modeB.push();
				}
			}
		}
		
		if (e.getSource() instanceof MuteButton) {
			logger.debug("MuteButton pressed.");
			outputMan.muteButtonPressed((MuteButton) e.getSource(), profileMan.getActive());
		}
		
		if (e.getSource() instanceof DropDownList) {
			logger.debug("DropDownList pressed.");
			eIsDropDownList((DropDownList) e.getSource());
		}
		
		if (e.getSource() instanceof ProfileChooser) {
			if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
				eIsProfileChooser((ProfileChooser) e.getSource());
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- eIs Actions ----------------------------------------------------------
	// --------------------------------------------------------------------------


	/**
	 * Do something, if ProfileChooser was activated. o_O
	 * 
	 * @param pc
	 * @author FelixP
	 */
	private void eIsProfileChooser(ProfileChooser pc) {
		File path = pc.getSelectedFile();
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		pc.setVisible(false);
		
		switch (pc.getMenuType()) {
		// import profile
			case iImport:
				try {
					profileMan.importProfiles(path);
				} catch (ZipException err1) {
					logger.error("unable to extract file " + path.toString());
				} catch (IOException err1) {
					logger.error("Error by importing Profile from " + path.toString());
				}
				break;
			
			// export profile
			case iExport:
				String pathToFile = StringHelper.addEnding(path.toString(), ".zip");
				try {
					profileMan.getActive().save();
					ImportExportManager.exportProfiles(profileMan.getActive(), new File(pathToFile));
					logger.debug("Profile exported");
					showStatusMessage("Profile exported");
				} catch (IOException err1) {
					logger.error("Unable to export profile " + pathToFile);
				}
				break;
			
			// Extend Dictionary By Text
			case iT2D:
				profileMan.getActive().save();
				try {
					words = ImportExportManager.importFromText(path.toString());
				} catch (IOException err) {
					showStatusMessage("Could not load the text file. Please choose another one.");
				}
				profileMan.getActive().getTree().importFromHashMap(words);
				showStatusMessage("Text file included.");
				break;
			
			// Extend Dictionary From File
			case iF2D:
				try {
					words = ImportExportManager.importFromFile(path.toString(), true);
				} catch (IOException err) {
					showStatusMessage("Could not load the text file. Please choose another one.");
				}
				profileMan.getActive().getTree().importFromHashMap(words);
				showStatusMessage("Dictionary file included.");

				break;
			
			// Export Dictionary To File
			case iD2F:
				words = profileMan.getActive().getTree().exportToHashMap();
				try {
					pathToFile = StringHelper.addEnding(path.toString(), ".tree");
					ImportExportManager.exportToFile(words, pathToFile);
					showStatusMessage("Dictionary file exported to " + pathToFile + ".");
				} catch (IOException err) {
					showStatusMessage("Could not create the dictionary file. Please choose another path.");
				}
				break;
		}
	}
	
	
	/**
	 * 
	 * FIXME FelixP, add comment!
	 * 
	 * @param menuItem
	 * @param o
	 * @author FelixP
	 */
	public void eIsDlg(EMenuItem menuItem, Object o) {
		switch (menuItem) {
		// new profile
			case iNewProfile:
				InputDlg iDlg = (InputDlg) o;
				String newProfile = iDlg.getProfileName();
				if (!existProfile(newProfile)) {
					this.addNewProfile(newProfile);
					iDlg.setVisible(false);
				} else {
					iDlg.setLblText("Profile already exists :-(");
				}
				break;
			
			// Clean Dictionary
			case iClean:
				ProfileCleanerDlg iCleanDlg = (ProfileCleanerDlg) o;
				Integer freq = iCleanDlg.getFrequency();
				Date date = iCleanDlg.getDate();
				int deleted = profileMan.getActive().getTree()
						.autoCleaning(freq, date.getTime(), PriorityTree.BOTTOM_OR_OLDER);
				showStatusMessage("Dictionary cleaned (" + deleted + " deleted).");
				break;
		}
	}
	
	
	/**
	 * Switches the profiles based on a Dropdownlist... <br>
	 * 
	 * @param currentDdl
	 * @author DanielAl, NicolaiO
	 */
	private void eIsDropDownList(DropDownList currentDdl) {
		if (currentDdl.getType() == DropDownList.PROFILE) {
			Profile selectedProfile = profileMan.getProfileByName(currentDdl.getSelectedItem().toString());
			if (selectedProfile != null) {
				logger.debug("selected Profilename: " + selectedProfile.getName());
				changeProfile(selectedProfile);
			} else {
				logger.warn("Selected Item refers to a non valid profile: \"" + currentDdl.getSelectedItem() + "\"");
			}
		}
	}
	
	
	// --------------------------------------------------------------------------
	// --- Window Events --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}
	
	
	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}
	
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		closeSuperFelix();
	}
	
	
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}
	
	
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}
	
	
	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}
	
	
	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
	
	
	// --------------------------------------------------------------------------
	// --- Mouse Events ---------------------------------------------------------
	// --------------------------------------------------------------------------
	

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof MuteButton) {
			// show tooltip in statusbar
			MuteButton pb = (MuteButton) e.getSource();
			showTooltip(pb.getMode().getTooltip());
		}
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
		// delete tooltip in statusbar
		hideTooltip();
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Calls the constructor if no instance exist. Singleton Design Pattern...
	 * 
	 * @return Controller
	 * @author NicolaiO
	 */
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	
	public boolean isReadyForActionEvents() {
		return readyForActionEvents;
	}
}
