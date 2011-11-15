/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 21, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.dhbw.t10.type.keyboard.key.Button;
import edu.dhbw.t10.type.keyboard.key.ModeButton;
import edu.dhbw.t10.type.keyboard.key.ModeKey;
import edu.dhbw.t10.type.keyboard.key.MuteButton;
import edu.dhbw.t10.type.keyboard.key.PhysicalButton;


/**
 * Representation of the keyboard layout with sizes and scale, as well as with all components (Buttons and
 * Drop-Down-Lists)
 * 
 * @author NicolaiO
 * 
 */
public class KeyboardLayout {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger		logger		= Logger.getLogger(KeyboardLayout.class);
	private ArrayList<Button>			buttons		= new ArrayList<Button>();
	private ArrayList<ModeButton>		modeButtons	= new ArrayList<ModeButton>();
	private ArrayList<MuteButton>		muteButtons	= new ArrayList<MuteButton>();
	private ArrayList<ModeKey>			modeKeys		= new ArrayList<ModeKey>();
	private ArrayList<DropDownList>	ddls			= new ArrayList<DropDownList>();
	private ArrayList<Image>			images		= new ArrayList<Image>();
	private int								size_x		= 0;
	private int								size_y		= 0;
	private float							scale_x		= 1;
	private float							scale_y		= 1;
	private float							scale_font	= 1;
	private Font							font			= new Font("Dialog", Font.PLAIN, 12);
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Create new KeyboardLayout with given size and scale factors
	 * 
	 * @param size_x
	 * @param size_y
	 * @param scalex
	 * @param scaley
	 * @param scale_font
	 * @author NicolaiO
	 */
	public KeyboardLayout(int size_x, int size_y, float scalex, float scaley, float scale_font) {
		this.size_x = size_x;
		this.size_y = size_y;
		this.scale_x = scalex;
		this.scale_y = scaley;
		this.scale_font = scale_font;
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Add a drop down list
	 * 
	 * @param ddl
	 * @author NicolaiO
	 */
	@Deprecated
	public void addDdl(DropDownList ddl) {
		ddls.add(ddl);
	}
	
	
	/**
	 * call rescale, if scale factor was changed to resize all buttons to an occording size
	 * 
	 * @author NicolaiO
	 */
	public void rescale() {
		for (PhysicalButton pb : getAllPhysicalButtons()) {
			Rectangle rect = pb.getBounds();
			rect.setBounds((int) (pb.getPos_x() * scale_x), (int) (pb.getPos_y() * scale_y),
					(int) (pb.getOrigSize().width * scale_x), (int) (pb.getOrigSize().height * scale_y));
			pb.setBounds(rect);
			pb.setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scale_font)));
		}
		for (DropDownList ddl : ddls) {
			Rectangle rect = ddl.getBounds();
			rect.setBounds((int) (ddl.getPos_x() * scale_x), (int) (ddl.getPos_y() * scale_y),
					(int) (ddl.getOrigSize().width * scale_x), (int) (ddl.getOrigSize().height * scale_y));
			ddl.setBounds(rect);
		}
		for (Image img : images) {
			Rectangle rect = img.getBounds();
			rect.setBounds((int) (img.getPos_x() * scale_x), (int) (img.getPos_y() * scale_y),
					(int) (img.getOrigSize().width * scale_x), (int) (img.getOrigSize().height * scale_y));
			img.setBounds(rect);
		}
		logger.debug("Layout rescaled.");
	}
	
	
	/**
	 * Unset/release all currently active ModeButtons
	 * 
	 * @author NicolaiO
	 */
	public void unsetPressedModes() {
		ArrayList<ModeKey> tactiveModes = new ArrayList<ModeKey>();
		for (ModeKey b : modeKeys) {
			if (b.getState() == ModeKey.PRESSED) {
				tactiveModes.add(b);
			}
		}
		for (ModeKey b : tactiveModes) {
			b.release();
		}
	}
	
	
	public ArrayList<ModeKey> getPressedModeKeys() {
		ArrayList<ModeKey> pmk = new ArrayList<ModeKey>();
		for (ModeKey mk : modeKeys) {
			if (mk.getState() == ModeKey.PRESSED || mk.getState() == ModeKey.HOLD) {
				pmk.add(mk);
			}
		}
		return pmk;
	}


	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Returns a list of all Buttons (normal, mute, mode) as PhysicalButtons
	 * 
	 * @return
	 * @author NicolaiO
	 */
	public ArrayList<PhysicalButton> getAllPhysicalButtons() {
		ArrayList<PhysicalButton> tempb = new ArrayList<PhysicalButton>();
		tempb.addAll(buttons);
		tempb.addAll(modeButtons);
		tempb.addAll(muteButtons);
		return tempb;
	}
	
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	
	public void setButtons(ArrayList<Button> buttons) {
		this.buttons = buttons;
	}
	
	
	public Dimension getSize() {
		return new Dimension(getSize_x(), getSize_y());
	}
	
	
	public int getSize_x() {
		return (int) (size_x * scale_x);
	}
	
	
	public void setSize_x(int size_x) {
		this.size_x = size_x;
	}
	
	
	public int getSize_y() {
		return (int) (size_y * scale_y);
	}
	
	
	public int getOrigSize_y() {
		return size_y;
	}
	
	
	public int getOrigSize_x() {
		return size_x;
	}
	
	
	public void setSize_y(int size_y) {
		this.size_y = size_y;
	}
	
	
	/**
	 * Set all scales (x,y,font) to given scale
	 * 
	 * @param scale
	 * @author NicolaiO
	 */
	public void setScale(float scale) {
		this.scale_x = scale;
		this.scale_y = scale;
		this.scale_font = scale;
	}
	
	
	public void setSize(Dimension size) {
		size_x = size.width;
		size_y = size.height;
	}
	
	
	public float getScale_x() {
		return scale_x;
	}
	
	
	public void setScale_x(float scale_x) {
		this.scale_x = scale_x;
	}
	
	
	public float getScale_y() {
		return scale_y;
	}
	
	
	public void setScale_y(float scale_y) {
		this.scale_y = scale_y;
	}
	
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	
	public void setFontSize(int size) {
		font = new Font(font.getName(), font.getStyle(), size);
	}
	
	
	public int getFontSize() {
		return font.getSize();
	}
	
	
	public Font getFont() {
		return font;
	}
	
	
	public float getScale_font() {
		return scale_font;
	}
	
	
	public void setScale_font(float scale_font) {
		this.scale_font = scale_font;
	}
	
	
	public ArrayList<DropDownList> getDdls() {
		return ddls;
	}
	
	
	public ArrayList<ModeButton> getModeButtons() {
		return modeButtons;
	}
	
	
	public void setModeButtons(ArrayList<ModeButton> modeButtons) {
		this.modeButtons = modeButtons;
	}
	
	
	public ArrayList<MuteButton> getMuteButtons() {
		return muteButtons;
	}
	
	
	public void setMuteButtons(ArrayList<MuteButton> muteButtons) {
		this.muteButtons = muteButtons;
	}
	
	
	public void setDdls(ArrayList<DropDownList> ddls) {
		this.ddls = ddls;
	}
	
	
	public ArrayList<ModeKey> getModeKeys() {
		return modeKeys;
	}
	
	
	public void setModeKeys(ArrayList<ModeKey> modekeys) {
		this.modeKeys = modekeys;
	}
	
	
	public ArrayList<Image> getImages() {
		return images;
	}
	
	
	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}
	
	
}
