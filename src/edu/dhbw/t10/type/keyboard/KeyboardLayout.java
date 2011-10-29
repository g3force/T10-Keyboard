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
import edu.dhbw.t10.type.keyboard.key.MuteButton;
import edu.dhbw.t10.type.keyboard.key.PhysicalButton;


/**
 * TODO NicolaiO, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
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
	private ArrayList<DropDownList>	ddls			= new ArrayList<DropDownList>();
	private int								size_x		= 0;
	private int								size_y		= 0;
	private float							scale_x		= 1;
	private float							scale_y		= 1;
	private float							scale_font	= 1;
	private Font							font			= new Font("Dialog", Font.PLAIN, 12);
	

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
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
	
	
	public void addDdl(DropDownList ddl) {
		ddls.add(ddl);
	}
	

	public void rescale() {
		for (PhysicalButton k : getAllPhysicalButtons()) {
			Rectangle rect = k.getBounds();
			rect.setBounds((int) (k.getPos_x() * scale_x), (int) (k.getPos_y() * scale_y),
					(int) (k.getOrigSize().width * scale_x), (int) (k.getOrigSize().height * scale_y));
			k.setBounds(rect);
			k.setFont(new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scale_font)));
		}
		for (DropDownList ddl : ddls) {
			Rectangle rect = ddl.getBounds();
			rect.setBounds((int) (ddl.getPos_x() * scale_x), (int) (ddl.getPos_y() * scale_y),
					(int) (ddl.getOrigSize().width * scale_x), (int) (ddl.getOrigSize().height * scale_y));
			ddl.setBounds(rect);
		}
		logger.debug("Layout rescaled.");
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
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


}
