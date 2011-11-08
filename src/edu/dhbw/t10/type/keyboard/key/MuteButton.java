/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Oct 28, 2011
 * Author(s): DirkK
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard.key;

import java.awt.Color;

import org.apache.log4j.Logger;


/**
 * button for the mute options
 * @author DirkK
 * 
 */
public class MuteButton extends PhysicalButton {
	/**  */
	private static final long		serialVersionUID		= -4124533718708150504L;
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger	logger					= Logger.getLogger(MuteButton.class);
	public static final int			UNKNOWN					= 0;
	public static final int			AUTO_PROFILE_CHANGE	= 1;
	public static final int			AUTO_COMPLETING		= 2;
	public static final int			TREE_EXPANDING			= 3;
	private int							type						= UNKNOWN;
	private Color						onColor;
	private Color						offColor;
	private String						onName					= "";
	private String						offName					= "";
	private boolean					activated				= false;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public MuteButton(int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		onColor = this.getBackground();
		offColor = this.getBackground();
		createToolTip();
		setToolTipText("Blubb");
		// JToolTip t;
		// t.
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	// public String getTypeAsString() {
	// MuteButton.class.getDeclaredField("THIS_IS_MY_CONST").get(String.class)
	// }

	
	public void push() {
		if (activated) {
			activated = false;
			setText(offName);
			setBackground(offColor);
			setToolTipText("Blubb");
			logger.debug("MuteButton deactivated");
		} else {
			activated = true;
			setText(onName);
			setBackground(onColor);
			setToolTipText("Blubb");
			logger.debug("MuteButton activated");
		}
	}
	
	
	public void release() {
		activated = false;
		setText(offName);
		setBackground(offColor);
	}
	

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public int getType() {
		return type;
	}
	
	
	public void setType(int type) {
		this.type = type;
	}
	
	
	public Color getOnColor() {
		return onColor;
	}
	
	
	public void setOnColor(String onColor) {
		Color c = getColorFromString(onColor);
		if (c != null)
			this.onColor = c;
	}
	
	
	public Color getOffColor() {
		return offColor;
	}
	
	
	public void setOffColor(String offColor) {
		Color c = getColorFromString(offColor);
		if (c != null)
			this.offColor = c;
	}
	
	
	public String getOnName() {
		return onName;
	}
	
	
	public void setOnName(String onName) {
		this.onName = onName;
	}
	
	
	public String getOffName() {
		return offName;
	}
	
	
	public void setOffName(String offName) {
		this.offName = offName;
	}
	
}
