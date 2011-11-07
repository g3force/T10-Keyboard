/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 24, 2011
 * Author(s): FelixP
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
import edu.dhbw.t10.type.keyboard.KeyboardLayout;
import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.Key;
import edu.dhbw.t10.type.keyboard.key.ModeButton;
import edu.dhbw.t10.type.keyboard.key.MuteButton;
import edu.dhbw.t10.view.Presenter;
import edu.dhbw.t10.view.panels.MainPanel;


/**
 * TODO FelixP, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author FelixP, DanielAl
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
		mainPanel.addComponentListener(mainPanel);
		resizeWindow(profileMan.getActive().getKbdLayout().getSize());
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
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Button) {
			Button button = (Button) e.getSource();
			// TODO useful hint: e.getModifiers()
			
			if (e.getModifiers() == ActionEvent.SHIFT_MASK) {
				// TODO reference to Shift Mode Button??
				logger.debug("shift modifier is pressed.");
			}

			if (button.getSingleKey().size() == 1) {
				Key key = (Key) button.getSingleKey().get(0);
				logger.debug("Key pressed: " + key.toString());
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
			} else if (button.getSingleKey().size() > 1) {
				// TODO
			} else
				logger.error("No Key List");
			button.unsetPressedModes();
		}
		

		if (e.getSource() instanceof ModeButton) {
			ModeButton modeB = (ModeButton) e.getSource();
			modeB.push();
		} // end if instance of ModeButton
		
		if (e.getSource() instanceof MuteButton) {
			MuteButton muteB = (MuteButton) e.getSource();
			muteB.push();
			if (muteB.getType() == MuteButton.AUTO_COMPLETING) {
				profileMan.toggleAutoCompleting();
			} else if (muteB.getType() == MuteButton.AUTO_PROFILE_CHANGE) {
				profileMan.toggleAutoProfileChange();
			} else if (muteB.getType() == MuteButton.TREE_EXPANDING) {
				profileMan.toggleTreeExpanding();
			}
			logger.debug("MuteButton pressed");
		} // end if instance of MuteButton
	}
	
	
	private void keyIsAccept(Key key) {
		if (suggest.length() > typedWord.length())
			outputMan.unMark();
		
		outputMan.printChar(key);
		suggest = StringHelper.removePunctuation(suggest);
		profileMan.acceptWord(suggest);
		typedWord = "";
		suggest = "";
	}
	
	
	private void keyIsCHAR(Key key) {
		outputMan.printChar(key);
		typedWord = typedWord + key.getName();
		suggest = profileMan.getWordSuggest(typedWord);
		outputMan.printSuggest(suggest, typedWord);
	}
	
	
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
	

	private void keyIsSpaceOrEnter(Key key) {
		logger.debug("Keycode" + key.getKeycode() + " " + key.getType());

		outputMan.printChar(key);
		typedWord = "";
		suggest = "";
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
	

	private void closeSuperFelix() {
		try {
		logger.debug("closing - saving the tree");
		profileMan.getActive().save();
		logger.debug("closing - saving the config");
		profileMan.saveConfig();
		logger.debug("closing - serializing the profiles");
		profileMan.serializeProfiles();
		logger.debug("closed - good buy");
		} catch (Exception e) {
			logger.error("closing route produced an error: " + e.toString());
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
