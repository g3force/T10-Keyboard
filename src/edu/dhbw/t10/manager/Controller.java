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
		presenter = new Presenter(mainPanel);
		typedWord = "";
		suggest = "";
		profileMan = new ProfileManager();

		mainPanel.setKbdLayout(profileMan.getActive().getKbdLayout());
		profileMan.addAllProfilesToDDL();

		mainPanel.addComponentListener(mainPanel);
		resizeWindow(profileMan.getActive().getKbdLayout().getSize());
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
	 * @author //FIXME
	 */
	public void createProfile(String name) {
		profileMan.createProfile(name);
	}
	

	/**
	 * Deletes a profile by name.
	 * 
	 * @param String name
	 * @author //FIXME
	 */
	public void deleteProfile(String name) {
		profileMan.deleteProfile(name);
	}


	@Override
	/**
	 * Starts the right activities for a specific event...
	 * 
	 * @param e
	 * @author //FIXME
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Button) {
			eIsButton((Button) e.getSource());
		} // end if instance of Button

		if (e.getSource() instanceof ModeButton) {
			ModeButton modeB = (ModeButton) e.getSource();
			modeB.push();
		} // end if instance of ModeButton

		if (e.getSource() instanceof MuteButton) {
			eIsMuteButton((MuteButton) e.getSource());
		} // end if instance of MuteButton
		
		if (e.getSource() instanceof DropDownList) {
			eIsDropDownList((DropDownList) e.getSource());
		} // end if instance of DropDownList
	}
	

	/**
	 * Do the logic for a button event. Switch between different types, specific Keys and a Key Combination...
	 * 
	 * @param button
	 * @author DanielAl, //FIXME
	 */
	private void eIsButton(Button button) {
		// TODO useful hint: e.getModifiers()
		
		// TODO reference to Shift Mode Button??
		// if (e.getModifiers() == ActionEvent.SHIFT_MASK) {
		// logger.debug("shift modifier is pressed.");
		// }
		
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
			// FIXME not working...
			outputMan.printCombi(button);
		} else
			logger.error("No Key List");

		button.unsetPressedModes();
	}
	

	/**
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
	 * @author DanielAl
	 */

	private void eIsMuteButton(MuteButton muteB) {
		muteB.push();
		int type = muteB.getType();
		switch (type) {
			case MuteButton.AUTO_COMPLETING:
				profileMan.toggleAutoCompleting();
				break;
			case MuteButton.AUTO_PROFILE_CHANGE:
				profileMan.toggleAutoProfileChange();
				break;
			case MuteButton.TREE_EXPANDING:
				profileMan.toggleTreeExpanding();
				break;
		}
		logger.debug("MuteButton pressed");
	}
	

	/**
	 * 
	 * TODO DanielAl, add comment!
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
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
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
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
	 * @author DanielAl
	 */
	private void keyIsCHAR(Key key) {
		outputMan.printChar(key);
		typedWord = typedWord + key.getName();
		suggest = profileMan.getWordSuggest(typedWord);
		outputMan.printSuggest(suggest, typedWord);
	}
	

	/**
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
	 * @author DanielAl
	 */
	private void keyIsUnicode(Key key) {
		outputMan.printChar(key);
		// FIXME Wieso sind Umlaute als Unicode Zeichen im Keyboard gespeichert?? Wie soll die Unterscheidung zwischen
		// Satzzeichen und Buchstaben stattfinden?
		// typedWord = typedWord + key.getName();
		// suggest = profileMan.getWordSuggest(typedWord);
		// outputMan.printSuggest(suggest, typedWord);
		typedWord = "";
		suggest = "";
	}
	

	/**
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
	 * @author DanielAl
	 */
	private void keyIsBackspace() {
		if (typedWord.length() > 0 && typedWord.equals(suggest)) {
			typedWord = typedWord.substring(0, typedWord.length() - 1);
			outputMan.deleteChar(1); // Eins, weil es keinen Vorschlag gibt...
			suggest = profileMan.getWordSuggest(typedWord);
			outputMan.printSuggest(suggest, typedWord);
		} else if (typedWord.length() > 0) {
			typedWord = typedWord.substring(0, typedWord.length() - 1);
			outputMan.deleteChar(2); // Zwei, weil einmal muss die aktuelle Markierung gelöscht werden und
			// dann ein Zeichen.
			suggest = profileMan.getWordSuggest(typedWord);
			outputMan.printSuggest(suggest, typedWord);
		} else {
			outputMan.deleteChar(1);
		}
	}
	

	/**
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
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
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
	 * @author DanielAl
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
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
	 * @author DanielAl
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
			logger.trace("(c) FIT 42");
		} catch (Exception e) {
			logger.error("closing routine produced an error: " + e.toString());
		}
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * 
	 * TODO DanielAl, add comment!
	 * 
	 * @param currentList
	 * @author DanielAl
	 */
	
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}
}
