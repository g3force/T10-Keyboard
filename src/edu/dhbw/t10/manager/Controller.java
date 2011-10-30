/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 24, 2011
 * Author(s): felix
 * 
 * *********************************************************
 */
package edu.dhbw.t10.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import edu.dhbw.t10.manager.output.OutputManager;
import edu.dhbw.t10.manager.profile.ProfileManager;
import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;
import edu.dhbw.t10.type.keyboard.key.ModeButton;
import edu.dhbw.t10.type.keyboard.key.MuteButton;


/**
 * TODO felix, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author felix, Andres
 * 
 */
public class Controller implements ActionListener {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger	= Logger.getLogger(Controller.class);
	private static Controller		instance;
	
	private String						typedWord;
	private String						suggest;
	
	private ProfileManager			profileMan;
	private OutputManager			outputMan;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	private Controller() {
		instance = this;
		logger.debug("initializing...");
		typedWord = "";
		suggest = "";
		profileMan = ProfileManager.getInstance();
		outputMan = OutputManager.getInstance();
		logger.debug("initialized.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Button) {
			Button button = (Button) e.getSource();
			
			if (button.getSingleKey().size() == 1) {
				Key key = (Key) button.getSingleKey().get(0);
				logger.debug("Key pressed (Name:" + key.getName() + ", Type: " + key.getType() + ", Keycode: "
						+ key.getKeycode() + ")");
				if (key.isAccept())
					this.keyIsAccept(key);
				else if (key.getType() == Key.CHAR)
					this.KeyIsCHAR(key);
				else if (key.getType() == Key.UNICODE)
					this.KeyIsUnicode(key);
				else if (key.getName() == "\\BACK_SPACE\\")
					this.KeyIsBackspace();
				else if ((key.getName() == "\\SPACE\\" || key.getName() == "\\ENTER\\"))
					this.KeyIsSpaceOrEnter(key);
				else if (key.getName() == "\\DELETE\\") {
					outputMan.printChar(key);
					suggest = typedWord;
				}
			}
			button.unsetPressedModes();
		} // end if instanceof Bbutton
		
		if (e.getSource() instanceof ModeButton) {
			ModeButton modeB = (ModeButton) e.getSource();
			modeB.push();
		}
		
		if (e.getSource() instanceof MuteButton) {
			MuteButton muteB = (MuteButton) e.getSource();
			if (muteB.getType() == MuteButton.AUTO_COMPLETING) {
				profileMan.toggleAutoCompleting();
			} else if (muteB.getType() == MuteButton.AUTO_PROFILE_CHANGE) {
				profileMan.toggleAutoProfileChange();
			} else if (muteB.getType() == MuteButton.TREE_EXPANDING) {
				profileMan.toggleTreeExpanding();
			}
		}
		
		// if (key.isAccept()) {
		// if (suggest.length() > typedWord.length())
		// outputMan.unMark();
		// outputMan.printChar(key);
		// profileMan.acceptWord(suggest);
		// typedWord = "";
		// suggest = "";
		// } else
		// if (key.getType() == Key.CHAR) {
		// outputMan.printChar(key);
		// typedWord = typedWord + key.getText();
		// suggest = profileMan.getWordSuggest(typedWord);
		// outputMan.printSuggest(suggest, typedWord);
		// } else
		// if (key.getType() == Key.UNICODE) {
		// outputMan.printChar(key);
		// } else
		// if (key.getName() == "\\BACK_SPACE\\") {
		// if (typedWord.length() > 0) {
		// typedWord = typedWord.substring(0, typedWord.length() - 2);
		// outputMan.deleteChar(2); // Zwei, weil einmal muss die aktuelle Markierung gelöscht werden und
		// // dann ein Zeichen.
		// suggest = profileMan.getWordSuggest(typedWord);
		// outputMan.printSuggest(suggest, typedWord);
		// } else {
		// outputMan.deleteChar(1);
		// }
		// } else
		// if ((key.getName() == "\\SPACE\\" || key.getName() == "\\ENTER\\")) {
		// outputMan.printChar(key);
		// typedWord = "";
		// suggest = "";
		// } else
		// if (key.getType() == Key.CONTROL) {
		// outputMan.printChar(key);
		// if (key.getName() == "\\DELETE\\")
		// suggest = typedWord;
		// }
		// else if (key.getType() == Key.MUTE) {
		// // TODO Do something for mute
		// }
	}
	
	
	private void keyIsAccept(Key key) {
		if (suggest.length() > typedWord.length())
			outputMan.unMark();
		
		outputMan.printChar(key);
		profileMan.acceptWord(suggest);
		typedWord = "";
		suggest = "";
	}
	
	
	private void KeyIsCHAR(Key key) {
		outputMan.printChar(key);
		typedWord = typedWord + key.getName();
		suggest = profileMan.getWordSuggest(typedWord);
		outputMan.printSuggest(suggest, typedWord);
	}
	
	
	private void KeyIsUnicode(Key key) {
		outputMan.printChar(key);
		typedWord = typedWord + key.getName();
		suggest = profileMan.getWordSuggest(typedWord);
		outputMan.printSuggest(suggest, typedWord);
	}
	
	
	private void KeyIsBackspace() {
		if (typedWord.length() > 0) {
			typedWord = typedWord.substring(0, typedWord.length() - 2);
			outputMan.deleteChar(2); // Zwei, weil einmal muss die aktuelle Markierung gelöscht werden und
			// dann ein Zeichen.
			suggest = profileMan.getWordSuggest(typedWord);
			outputMan.printSuggest(suggest, typedWord);
		} else {
			outputMan.deleteChar(1);
		}
	}
	
	
	private void KeyIsSpaceOrEnter(Key key) {
		outputMan.printChar(key);
		typedWord = "";
		suggest = "";
	}
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
