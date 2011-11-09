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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.apache.log4j.Logger;

import edu.dhbw.t10.helper.StringHelper;
import edu.dhbw.t10.manager.output.OutputManager;
import edu.dhbw.t10.manager.profile.ProfileManager;
import edu.dhbw.t10.type.keyboard.DropDownList;
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;
import edu.dhbw.t10.type.keyboard.key.ModeButton;
import edu.dhbw.t10.type.keyboard.key.MuteButton;
import edu.dhbw.t10.type.profile.Profile;
import edu.dhbw.t10.view.Presenter;
import edu.dhbw.t10.view.menus.StatusBar;
import edu.dhbw.t10.view.panels.MainPanel;


/**
 * The Controller Class provides the central interface to combine the functionallity of the porgramm. the data flows
 * through it. <br>
 * Here all Managers and the view is initialized...<br>
 * It provides overwritten methods to handles actionEvents...<br>
 * 
 * @author FelixP, DanielAl, NicolaiO
 * 
 */
public class Controller implements ActionListener, WindowListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Controller.class);
	private static Controller		instance;
	
	private String						typedWord;
	private String						suggest;
	
	private ProfileManager			profileMan;
	private OutputManager			outputMan;
	private MainPanel					mainPanel;
	private StatusBar					statusBar;
	private Presenter					presenter;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * This Constructor instanciate all other objects... The Application is loaded...<br>
	 * The Controller is implemented as a Singleton<br>
	 * 
	 * @author NicolaiO, DirkK, FelixP, SebastianN, DanielAl
	 */
	private Controller() {
		instance = this;
		logger.debug("initializing...");
		outputMan = new OutputManager();
		mainPanel = new MainPanel();
		statusBar = new StatusBar("");
		presenter = new Presenter(mainPanel, statusBar);
		typedWord = "";
		suggest = "";
		profileMan = new ProfileManager();

		mainPanel.setKbdLayout(profileMan.getActive().getKbdLayout());
		profileMan.addAllProfilesToDDL();


		mainPanel.addComponentListener(mainPanel);
		resizeWindow(profileMan.getActive().getKbdLayout().getSize());
		statusBar.message("Keyboard initialiezd.");
		logger.debug("initialized.");
	}
	

	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	// Data ----------------------------
	
	/**
	 * Creates a profile by name.
	 * 
	 * @param String name
	 * @author SebastianN
	 */
	public void createProfile(String name) {
		profileMan.addProfileToDDL(profileMan.createProfile(name));
	}
	

	/**
	 * Deletes a profile by name.
	 * 
	 * @param String name
	 * @author SebastianN
	 */
	public void deleteProfile(String name) {
		profileMan.deleteProfile(name);
	}


	@Override
	/**
	 * Starts the right activities for a specific event...
	 * 
	 * @param e
	 * @author ALL
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Button) {
			eIsButton((Button) e.getSource());
		}

		if (e.getSource() instanceof ModeButton) {
			ModeButton modeB = (ModeButton) e.getSource();
			modeB.push();
		}

		if (e.getSource() instanceof MuteButton) {
			eIsMuteButton((MuteButton) e.getSource());
		}
		
		if (e.getSource() instanceof DropDownList) {
			eIsDropDownList((DropDownList) e.getSource());
		}
		
		// FIXME NicolaiO reference to Shift Mode Button?? => problem, any idea??
		/**
		 * SHIFT_MASK modifier is set, when a button is clicked with right mouse button.
		 * How can I tell that shift should be pressed?! with ModeButton Shift or is there another way?
		 * TODO DanielAl send shift signal to Output
		 */
		if (e.getModifiers() == ActionEvent.SHIFT_MASK) {
			logger.debug("shift modifier is pressed. No action yet...");
		}
	}
	

	/**
	 * Do the logic for a button event. Switch between different types, specific Keys and a Key Combination...
	 * 
	 * @param button
	 * @author DanielAl
	 */
	private void eIsButton(Button button) {
		if (button.getSingleKey().size() == 1) {
			Key key = (Key) button.getSingleKey().get(0);

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
				outputMan.printChar(key);
				suggest = typedWord;
			}
			logger.debug("Key pressed: " + key.toString());
		} else if (button.getSingleKey().size() > 1) {
			// FIXME NicolaiO, DanielAl Combi auslesen und weitergeben...
			outputMan.printCombi(button);
		} else
			logger.error("No Key List");

		button.unsetPressedModes();
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
				profileMan.getActive().setAutoCompleting(muteB.isActivated());
				break;
			case MuteButton.AUTO_PROFILE_CHANGE:
				profileMan.getActive().setAutoProfileChange(muteB.isActivated());
				break;
			case MuteButton.TREE_EXPANDING:
				profileMan.getActive().setTreeExpanding(muteB.isActivated());
				break;
		}
		logger.debug("MuteButton pressed");
	}
	

	/**
	 * Switchs the profiles based on a Dropdownlist... <br>
	 * 
	 * @param currentList
	 * @author DanielAl
	 */
	private void eIsDropDownList(DropDownList currentList) {

		if (currentList.getType() == DropDownList.PROFILE) {
			Profile selectedProfile = profileMan.getProfiles().get(currentList.getSelectedIndex());
			logger.debug("Profilename: " + selectedProfile.getName());
			profileMan.setActive(selectedProfile);
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
			outputMan.unMark();
		
		outputMan.printChar(key);
		suggest = StringHelper.removePunctuation(suggest);
		profileMan.acceptWord(suggest);
		typedWord = "";
		suggest = "";
	}
	

	/**
	 * Prints the given key, added it to the typed String and get a new suggest and prtints it...
	 * @param key
	 * @author DanielAl
	 */
	private void keyIsCHAR(Key key) {
		outputMan.printChar(key);
		typedWord = typedWord + key.getName();
		suggest = profileMan.getWordSuggest(typedWord);
		outputMan.printSuggest(suggest, typedWord);
		statusBar.message("Suggest: " + suggest);
	}
	

	/**
	 * If the input is a Unicode (it is a Symbol character, special chars are type char) and this will be printed. <br>
	 * The typed Word and Suggest Word will be forgotten.<br>
	 * 
	 * @param key
	 * @author DanielAl
	 */
	private void keyIsUnicode(Key key) {
		outputMan.printChar(key);
		typedWord = "";
		suggest = "";
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
			statusBar.message("Suggest: " + suggest);
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
		profileMan.acceptWord(typedWord);
		outputMan.printChar(key);
		typedWord = "";
		suggest = "";
	}
	

	// Window ----------------------------
	
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
	

	/**
	 * Save the actual Profile and dictionary to be able to clse the application.
	 * 
	 * @author NicolaiO
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
}
