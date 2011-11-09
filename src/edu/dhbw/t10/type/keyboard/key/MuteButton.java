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
	// private Color onColor;
	// private Color offColor;
	// private String onName = "";
	// private String offName = "";
	private Mode						on;
	private Mode						off;
	private boolean					activated				= false;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public MuteButton(int size_x, int size_y, int pos_x, int pos_y) {
		super(size_x, size_y, pos_x, pos_y);
		on = new Mode(this.getBackground());
		off = new Mode(this.getBackground());
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

	/**
	 * 
	 * TODO NicolaiO synchronisation with actual mute state... either here or in profile(manager)
	 * 
	 * @author NicolaiO
	 */
	public void push() {
		setActivated(!activated);
	}
	
	
	public void release() {
		activated = false;
		setText(off.getName());
		setBackground(off.getColor());
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
	
	

	// public Color getOnColor() {
	// return onColor;
	// }
	//
	//
	// public void setOnColor(String onColor) {
	// Color c = getColorFromString(onColor);
	// if (c != null)
	// this.onColor = c;
	// }
	//
	//
	// public Color getOffColor() {
	// return offColor;
	// }
	//
	//
	// public void setOffColor(String offColor) {
	// Color c = getColorFromString(offColor);
	// if (c != null)
	// this.offColor = c;
	// }
	//
	//
	// public String getOnName() {
	// return onName;
	// }
	//
	//
	// public void setOnName(String onName) {
	// this.onName = onName;
	// }
	//
	//
	// public String getOffName() {
	// return offName;
	// }
	//
	//
	// public void setOffName(String offName) {
	// this.offName = offName;
	// }
	
	
	public Mode getModeOn() {
		return on;
	}
	
	
	public void setModeOn(Mode on) {
		this.on = on;
	}
	
	
	public Mode getModeOff() {
		return off;
	}
	
	
	public void setModeOff(Mode off) {
		this.off = off;
	}
	
	
	public boolean isActivated() {
		return activated;
	}
	
	
	public void setActivated(boolean activated) {
		this.activated = activated;
		if (!activated) {
			setText(off.getName());
			setBackground(off.getColor());
			setToolTipText(off.getTooltip());
			logger.debug("MuteButton deactivated");
		} else {
			setText(on.getName());
			setBackground(on.getColor());
			setToolTipText(on.getTooltip());
			logger.debug("MuteButton activated");
		}
	}


	public class Mode {
		private Color	color;
		private String	colorS;
		private String	name;
		private String	tooltip;
		
		
		private Mode(Color c) {
			colorS = "white";
			color = c;
		}
		
		
		public Color getColor() {
			return color;
		}
		
		
		public String getColorString() {
			return colorS;
		}
		
		
		public void setColor(String color) {
			colorS = color;
			Color c = getColorFromString(color);
			if (c != null)
				this.color = c;
		}
		
		
		public String getName() {
			return name;
		}
		
		
		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}
		
		
		@SuppressWarnings("unused")
		public String getTooltip() {
			return tooltip;
		}
		
		
		@SuppressWarnings("unused")
		public void setTooltip(String tooltip) {
			this.tooltip = tooltip;
		}


	}
	
}
