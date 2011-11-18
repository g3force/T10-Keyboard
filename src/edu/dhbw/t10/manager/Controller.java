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
import java.util.ArrayList;
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
import edu.dhbw.t10.type.keyboard.key.ModeKey;
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
	
	private String						typedWord;
	private String						suggest;
	private String						datapath;
	
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

		// works for Windows and Linux... so the data is stored in the systems userdata folder...
		datapath = System.getProperty("user.home") + "/.t10keyboard";
		File tf = new File(datapath);
		if (!tf.exists()) {
			tf.mkdirs();
		}

		outputMan = new OutputManager();
		typedWord = "";
		suggest = "";

		// This message is important! Otherwise, The StatusPane has a wrong height and the layout will be decreased
		// meaning, it gets smaller with each start...
		statusPane.enqueueMessage("Keyboard initializing...", StatusPane.LEFT);
		profileMan = new ProfileManager();
		
		changeProfile(profileMan.getActive());
		
		readyForActionEvents = true;
		statusPane.enqueueMessage("Keyboard initialized.", StatusPane.LEFT);
		logger.debug("initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 * is called whenever a word shall be accepted
	 * 
	 * @param word
	 * @author DirkK
	 */
	private void acceptWord(String word) {
		boolean success = profileMan.acceptWord(word);
		if (success) {
			statusPane.enqueueMessage("Word inserted: " + word, StatusPane.LEFT);
		}
		typedWord = "";
		suggest = "";
	}
	
	
	/**
	 * Save the actual Profile and dictionary to be able to clse the application.
	 * 
	 * @author DirkK
	 */
	private void closeSuperFelix() {
		try {
			logger.debug("closing - saving the tree");
			profileMan.getActive().save();
			logger.debug("closing - saving the config");
			profileMan.saveConfig();
			logger.debug("closing - serializing the profiles");
			profileMan.serializeProfiles();
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
	 * Deletes a profile by name.
	 * 
	 * @param String name
	 * @author SebastianN
	 */
	public void deleteActiveProfile() {
		// get active profile to be delete
		Profile todelete = profileMan.getActive();
		// get potential new profile
		Profile newProfile = profileMan.getProfiles().get(0);

		if (todelete == newProfile) {
			if (profileMan.getProfiles().size() > 1) {
				newProfile = profileMan.getProfiles().get(0);
			} else {
				logger.debug("Only one or zero profiles left. Can't delete.");
				return;
			}
		}
		changeProfile(newProfile);
		profileMan.deleteProfile(todelete);
		profileMan.loadDDLs();
	}
	
	
	/**
	 * Change profile. This does not only <b>set</b> the active profile, but also reloads the GUI!
	 * Do not use setActive of ProfileManager alone...
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
			Button b = (Button) e.getSource();
			// currently we do not support some buttons for linux...
			if (Output.getOs() == Output.LINUX
					&& (b.getKey().getKeycode().equals("\\WINDOWS\\") || b.getKey().getKeycode().equals("\\CONTEXT_MENU\\"))) {
				statusPane.enqueueMessage("Button not supported by your OS", StatusPane.LEFT);
			} else {
				eIsButton(b);
			}
		}
		
		if (e.getSource() instanceof ModeButton) {
			logger.debug("ModeButton pressed.");
			ModeButton modeB = (ModeButton) e.getSource();
			// currently we do not support some buttons for linux...
			if (Output.getOs() == Output.LINUX
					&& (modeB.getModeKey().getKeycode().equals("\\WINDOWS\\") || modeB.getModeKey().getKeycode()
							.equals("\\CONTEXT_MENU\\"))) {
				statusPane.enqueueMessage("Button not supported by your OS", StatusPane.LEFT);
			} else {
				if (modeB.isModesDisabled()) {
					Button helpB = new Button(1, 1, 1, 1);
					Key helpKey = ((ModeButton) e.getSource()).getModeKey().clone();
					helpB.setKey(helpKey);
					eIsButton(helpB);
				} else {
					modeB.push();
				}
			}
		}
		
		if (e.getSource() instanceof MuteButton) {
			logger.debug("MuteButton pressed.");
			eIsMuteButton((MuteButton) e.getSource());
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
	// --- Output ---------------------------------------------------------------
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
					ImportExportManager.importProfiles(path);
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
					statusPane.enqueueMessage("Profile exported", StatusPane.LEFT);
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
					statusPane.enqueueMessage("Could not load the text file. Please choose another one.", StatusPane.LEFT);
				}
				profileMan.getActive().getTree().importFromHashMap(words);
				statusPane.enqueueMessage("Text file included.", StatusPane.LEFT);
				break;
			
			// Extend Dictionary From File
			case iF2D:
				try {
					words = ImportExportManager.importFromFile(path.toString(), true);
				} catch (IOException err) {
					statusPane.enqueueMessage("Could not load the text file. Please choose another one.", StatusPane.LEFT);
				}
				profileMan.getActive().getTree().importFromHashMap(words);
				statusPane.enqueueMessage("Dictionary file included.", StatusPane.LEFT);

				break;
			
			// Export Dictionary To File
			case iD2F:
				words = profileMan.getActive().getTree().exportToHashMap();
				try {
					pathToFile = StringHelper.addEnding(path.toString(), ".tree");
					ImportExportManager.exportToFile(words, pathToFile);
					statusPane.enqueueMessage("Dictionary file exported to " + pathToFile + ".", StatusPane.LEFT);
				} catch (IOException err) {
					statusPane.enqueueMessage("Could not create the dictionary file. Please choose another path.",
							StatusPane.LEFT);
				}
				break;
		}
	}
	
	
	public void eIsDlg(EMenuItem menuItem, Object o) {
		switch (menuItem) {
		// new profile
			case iNewProfile:
				InputDlg iDlg = (InputDlg) o;
				String newProfile = iDlg.getProfileName();
				if (!profileMan.existProfile(newProfile)) {
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
				statusPane.enqueueMessage("Dictionary cleaned (" + deleted + " deleted).", StatusPane.LEFT);
				break;
		}
	}
	
	
	/**
	 * Do the logic for a button event. Switch between different types, specific Keys and a Key Combination...
	 * 
	 * @param button
	 * @author DanielAl
	 */
	private void eIsButton(Button button) {
		Key key = (Key) button.getPressedKey();

		// get all currently pressed Modekeys
		ArrayList<ModeKey> pressedModeKeys = profileMan.getActive().getKbdLayout().getPressedModeKeys();
		
		if (key.getKeycode().equals("\\CAPS_LOCK\\")) {
			this.keyIsCapsLock();
		} else
		// Print the key iff zero or one ModeKeys is pressed
		if (pressedModeKeys.size() - button.getActiveModes().size() < 1) {
			if (key.isAccept())
				this.keyIsAccept(key);
			else if (key.getType() == Key.CHAR)
				this.keyIsCHAR(key);
			else if (key.getType() == Key.UNICODE)
				this.keyIsUnicode(key);
			else if (key.getKeycode().equals("\\BACK_SPACE\\"))
				this.keyIsBackspace();
			else if ((key.getKeycode().equals("\\SPACE\\") || key.getKeycode().equals("\\ENTER\\")))
				this.keyIsSpaceOrEnter(key);
			else if (key.getKeycode().equals("\\DELETE\\")) {
				outputMan.printKey(key);
				suggest = typedWord;
			} else if (key.getType() == Key.CONTROL)
				this.keyIsControl(key);
			logger.debug("Key pressed: " + key.toString());
		} else {
			// print the key combi else
			logger.debug("Keycombi will be executed. Hint: " + pressedModeKeys.size() + "-"
					+ button.getActiveModes().size() + "<1");
			logger.trace(pressedModeKeys);
			outputMan.printCombi(pressedModeKeys, button.getKey());
		}

		// unset all ModeButtons, that are in PRESSED state
		profileMan.getActive().getKbdLayout().unsetPressedModes();
	}
	
	
	/**
	 * Run this with a caps_lock key to trigger all shift buttons.
	 * If Shift state is DEFAULT, it will be changed to HOLD, else to DEFAULT
	 * 
	 * @param key
	 * @author NicolaiO
	 */
	private void keyIsCapsLock() {
		logger.trace("CapsLock");
		for (ModeKey mk : profileMan.getActive().getKbdLayout().getModeKeys()) {
			if (mk.getKeycode().equals("\\SHIFT\\")) {
				if (mk.getState() == ModeKey.DEFAULT) {
					mk.setState(ModeKey.HOLD);
				} else {
					mk.setState(ModeKey.DEFAULT);
				}
				break;
			}
		}
		presenter.pack();
	}
	
	
	/**
	 * Switchs between the three different Mute modes...<br>
	 * Modes are:<br>
	 * - AUTO_COMPLETING - If activated, this prints a suggested Word behind the typed chars and mark them...
	 * - AUTO_PROFILE_CHANGE - If activated, this changes the profiles based on the sourrounded context.
	 * - TREE_EXPANDING - If activated, accepted words are saved in the dicitionary...
	 * @param muteB
	 * @author DanielAl
	 */
	private void eIsMuteButton(MuteButton muteB) {
		muteB.push();
		int type = muteB.getType();
		switch (type) {
			case MuteButton.AUTO_COMPLETING:
				if (muteB.isActivated()) {
					typedWord = "";
					suggest = "";
				}
				profileMan.getActive().setAutoCompleting(muteB.isActivated());
				break;
			case MuteButton.AUTO_PROFILE_CHANGE:
				profileMan.getActive().setAutoProfileChange(muteB.isActivated());
				break;
			case MuteButton.TREE_EXPANDING:
				if (muteB.isActivated()) {
					typedWord = "";
					suggest = "";
				}
				profileMan.getActive().setTreeExpanding(muteB.isActivated());
				break;
		}
		logger.debug("MuteButton pressed");
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
	
	
	/**
	 * Accept a suggested word, unmarks it and prints the given key.
	 * 
	 * @param key
	 * @author DanielAl
	 */
	private void keyIsAccept(Key key) {
		if (suggest.length() > typedWord.length())
			// outputMan.unMark();
			outputMan.printSuggest(suggest, typedWord, 1);
		outputMan.printKey(key);
		acceptWord(suggest);
		logger.trace("Word accepted");
	}
	
	
	/**
	 * Prints a Control Key, <br>
	 * if no SPACE, ENTER, DELETE or BACK_SPACE, these are special Keys and handled with extra methods...
	 * 
	 * @param key
	 * @author DanielAl
	 */
	private void keyIsControl(Key key) {
		if (suggest.length() > typedWord.length())
			outputMan.printKey(new Key(0, "Delete", "\\DELETE\\", Key.CONTROL, false, "", ""));
		outputMan.printKey(key);
		typedWord = "";
		suggest = "";
	}
	
	
	/**
	 * Prints the given key, added it to the typed String and get a new suggest and prtints it...
	 * @param key
	 * @author DanielAl
	 */
	private void keyIsCHAR(Key key) {
		outputMan.printKey(key);
		typedWord = typedWord + key.getName();
		suggest = profileMan.getWordSuggest(typedWord);
		outputMan.printSuggest(suggest, typedWord);
	}
	
	
	/**
	 * If the input is a Unicode (it is a Symbol character, special chars are type char) and this will be printed. <br>
	 * The typed Word and Suggest Word will be forgotten.<br>
	 * 
	 * @param key
	 * @author DanielAl
	 */
	private void keyIsUnicode(Key key) {
		outputMan.printKey(key);
		acceptWord(typedWord);
	}
	
	
	/**
	 * Handles a typed BackSpace.<br>
	 * There are 3 options:<br>
	 * - typedWord and suggest are equal, so no suggest is printed and one Backspace deletes the last typed char.<br>
	 * - a suggest is printed to complete the typedWord. Then you need to delete the marked chars and the last typed
	 * char, so yo need two Back_Spaces.<br>
	 * - all other options, you'll only need to send one Back_Space<br>
	 * A new suggest will also be generated.<br>
	 * 
	 * @author DanielAl
	 */
	private void keyIsBackspace() {
		if (typedWord.length() > 0 && typedWord.equals(suggest)) {
			typedWord = typedWord.substring(0, typedWord.length() - 1);
			// Delete 1, because nothing is marked and you want to delete one char
			outputMan.deleteChar(1);
			suggest = profileMan.getWordSuggest(typedWord);
			outputMan.printSuggest(suggest, typedWord);
		} else if (typedWord.length() > 0) {
			typedWord = typedWord.substring(0, typedWord.length() - 1);
			// Delete 1, because there are suggested chars marked and you want to delete them and one char
			outputMan.deleteChar(2);
			suggest = profileMan.getWordSuggest(typedWord);
			outputMan.printSuggest(suggest, typedWord);
		} else {
			outputMan.deleteChar(1);
		}
	}
	
	
	/**
	 * When Space or Enter is pressed accept the typed Word and print Space or Enter...<br>
	 * The suggest will be declined and forgotten.<br>
	 * 
	 * @param key
	 * @author DanielAl
	 */
	private void keyIsSpaceOrEnter(Key key) {
		logger.debug("Keycode " + key.getKeycode() + " " + key.getType());
		if (typedWord.length() < suggest.length())
			outputMan.delMark();
		outputMan.printKey(key);
		acceptWord(typedWord);
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
			statusPane.enqueueMessage(pb.getMode().getTooltip(), StatusPane.RIGHT);
		}
	}
	
	
	@Override
	public void mouseExited(MouseEvent e) {
		// delete tooltip in statusbar
		statusPane.enqueueMessage("", StatusPane.RIGHT);
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
	
	
	public String getDatapath() {
		return datapath;
	}
	
	
	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}
	
	
	public boolean isReadyForActionEvents() {
		return readyForActionEvents;
	}
}
