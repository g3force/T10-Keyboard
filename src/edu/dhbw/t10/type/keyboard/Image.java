/*
 * *********************************************************
 * Copyright (c) 2011 - 2011, DHBW Mannheim
 * Project: T10 On-Screen Keyboard
 * Date: Nov 15, 2011
 * Author(s): NicolaiO
 * 
 * *********************************************************
 */
package edu.dhbw.t10.type.keyboard;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.apache.log4j.Logger;


/**
 * The image class contains an arbitrary image that should be displayed within the keyboardLayout.
 * It stores the size and position as well as the image to display.
 * Furthermore, if this object will be resized with setBounds(Rectangle), it will resize the image too.
 * 
 * @author NicolaiO
 * 
 */
public class Image extends JLabel implements ILayoutElement {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	
	private static final long		serialVersionUID	= -2937257574937296192L;
	private static final Logger	logger				= Logger.getLogger(Image.class);
	private Dimension					origSize				= new Dimension(10, 10);
	private int							pos_x					= 0;
	private int							pos_y					= 0;
	private String						src					= "";
	private BufferedImage			img;
	private int							imgType				= 0;
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public Image(String src, int size_x, int size_y, int pos_x, int pos_y) {
		init(src, size_x, size_y, pos_x, pos_y);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Do all the initializing steps, like setting the attributes and receive the image from the given src.
	 * 
	 * @param src a resource URL as String (should start with "/res"!)
	 * @param size_x
	 * @param size_y
	 * @param pos_x
	 * @param pos_y
	 * @author NicolaiO
	 */
	private void init(String src, int size_x, int size_y, int pos_x, int pos_y) {
		this.origSize = new Dimension(size_x, size_y);
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.src = src;

		URL srcUrl = getClass().getResource(src);
		if (srcUrl != null) {
			try {
				img = ImageIO.read(srcUrl);
				imgType = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
				setIcon(new ImageIcon(img));
			} catch (IOException e) {
				logger.warn("An image could not be loaded!");
			}
		} else {
			logger.warn("Image URL \"" + src + "\" is not valid!");
		}
	}
	

	@Override
	public void setBounds(Rectangle r) {
		super.setBounds(r);
		/**
		 * resize the image together with its parent label
		 */
		setIcon(new ImageIcon(createResizedCopy(img, r.width, r.height, true)));
	}


	/**
	 * Resizes the given image to the given size and returns it.
	 * The code is taken from: http://stackoverflow.com/questions/244164/resize-an-image-in-java-any-open-source-library
	 * 
	 * @param originalImage
	 * @param scaledWidth
	 * @param scaledHeight
	 * @param preserveAlpha
	 * @return
	 * @author NicolaiO
	 */
	public BufferedImage createResizedCopy(java.awt.Image originalImage, int scaledWidth, int scaledHeight,
			boolean preserveAlpha) {
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	
	public Dimension getOrigSize() {
		return origSize;
	}
	
	
	public void setOrigSize(Dimension origSize) {
		this.origSize = origSize;
	}
	
	
	public int getPos_x() {
		return pos_x;
	}
	
	
	public void setPos_x(int pos_x) {
		this.pos_x = pos_x;
	}
	
	
	public int getPos_y() {
		return pos_y;
	}
	
	
	public void setPos_y(int pos_y) {
		this.pos_y = pos_y;
	}
	
	
	public String getSrc() {
		return src;
	}
	
	
	public void setSrc(String src) {
		this.src = src;
	}
	
	
	public BufferedImage getImg() {
		return img;
	}
	
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	
	public int getImgType() {
		return imgType;
	}
	
	
	public void setImgType(int imgType) {
		this.imgType = imgType;
	}
}
