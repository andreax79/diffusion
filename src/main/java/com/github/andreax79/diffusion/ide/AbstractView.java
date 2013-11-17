/**
 * Copyright (C) 2005 Andrea Bonomi - <andrea.bonomi@gmail.com>
 *
 * https://github.com/andreax79/diffusion
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.)
 *
 */

package com.github.andreax79.diffusion.ide;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.github.andreax79.diffusion.space.GridSpace;

/**
 * Abstract view
 * @author Andrea Bonomi - andrea.bonomi@gmail.com
 */
public abstract class AbstractView extends JPanel implements View, MouseListener {

	private static final long serialVersionUID = 1L;

	/** Space */
	protected GridSpace space;

	/** Foreground image */
	protected Image offScreen;

	/** Foreground image Graphics */
	protected Graphics gOff;

	/** Background image */
	protected Image backOffScreen;

	/** Background image Graphics */
	protected Graphics backGOff;

	/** Background external image */
	protected Image backgroundImage;

	/** Width */
	protected int width;

	/** Height */
	protected int height;

	/** Font */
	protected Font labelFont;

	/** Default delay */
	public static final int defaultDelay = 500;

	/** Background color */
	protected Color backgroundColor = defaultBackgroundColor;

	/** Arc color */
	protected Color arcColor = defaultArcColor;

	/** Node color */
	protected Color nodeColor = defaultNodeColor;

	/** Empty node color */
	protected Color emptyNodeColor = Color.black;

	/** Label color */
	protected Color siteIdColor = defaultSiteIdColor;

	/** Selected node color */
	protected Color selectedNodeColor = defaultSelectedNodeColor;

	/** Show label id */
	protected boolean showLabel = false;

	/** Show label id */
	protected boolean showInterface = false;

	/** If true show nodes */
	protected boolean showNodes = true;

	/** Regenerate the back image */
	protected boolean autoRegenerateBackImage = false;

	/**
	 * Costructor
	 */
	public AbstractView(GridSpace space, int width, int height) {
		this.space = space;
		this.width = width;
		this.height = height;

		// Set font
		labelFont = new Font("Serif", Font.PLAIN, 8);

		// Add mouse listener
		addMouseListener(this);
	}

	/**
	 * Paint this component
	 * 
	 * @param graphics
	 *            Graphics object
	 */
	public void paintComponent(Graphics graphics) {
		// try {
		super.paintComponent(graphics);
		generateImage();
		if (offScreen != null)
			graphics.drawImage(offScreen, 0, 0, null);
		// } catch (Exception e) {
		// System.err.println(e.getMessage());
		// }
	}

	/**
	 * Update the view - this method is called from the space
	 */
	public void updateView() {
		repaint();
	}

	/**
	 * Gets the preferred size of this component
	 */

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	/**
	 * Gets the maximum size of this component
	 */

	public Dimension getMaximumSize() {
		return new Dimension(width, height);
	}

	/**
	 * Set height/low rendering quality (antialias on/off)
	 */

	public void setHighRenderingQuality(boolean quality) {
		if (gOff == null)
			return;
		RenderingHints hints = new RenderingHints(null);
		hints.put(RenderingHints.KEY_ANTIALIASING, quality ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		((Graphics2D) gOff).setRenderingHints(hints);
	}

	/**
	 * Set height/low rendering quality for back image (antialias on/off)
	 */

	public void setBackHighRenderingQuality(boolean quality) {
		if (backGOff == null)
			return;
		RenderingHints hints = new RenderingHints(null);
		hints.put(RenderingHints.KEY_ANTIALIASING, quality ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, quality ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		((Graphics2D) backGOff).setRenderingHints(hints);
	}

	/**
	 * Load a background image
	 */

	public void loadBackgroundImage(String filename) {
		ImageIcon icon = new ImageIcon(filename);
		backgroundImage = icon.getImage();
		generateBackImage();
	}

	// Interface methods required

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public GridSpace getSpace() {
		return space;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getArcColor() {
		return arcColor;
	}

	public void setArcColor(Color arcColor) {
		this.arcColor = arcColor;
	}

	public Color getNodeColor() {
		return nodeColor;
	}

	public void setNodeColor(Color nodeColor) {
		this.nodeColor = nodeColor;
	}

	public Color getEmptyNodeColor() {
		return emptyNodeColor;
	}

	public void setEmptyNodeColor(Color emptyNodeColor) {
		this.emptyNodeColor = emptyNodeColor;
	}

	public Color getSiteIdColor() {
		return siteIdColor;
	}

	public void setSiteIdColor(Color siteIdColor) {
		this.siteIdColor = siteIdColor;
	}

	public boolean getShowNodes() {
		return showNodes;
	}

	public void setShowNodes(boolean showNodes) {
		this.showNodes = showNodes;
	}

	public boolean isShowLabel() {
		return showLabel;
	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public boolean isShowInterface() {
		return showInterface;
	}

	public void setShowInterface(boolean showInterface) {
		this.showInterface = showInterface;
	}

	public Font getLabelFont() {
		return labelFont;
	}

	public void setLabelFont(Font labelFont) {
		this.labelFont = labelFont;
	}

	public Color getSelectedNodeColor() {
		return selectedNodeColor;
	}

	public void setSelectedNodeColor(Color selectedNodeColor) {
		this.selectedNodeColor = selectedNodeColor;
	}

	public boolean isAutoRegenerateBackImage() {
		return autoRegenerateBackImage;
	}

	public void setAutoRegenerateBackImage(boolean autoRegenerateBackImage) {
		this.autoRegenerateBackImage = autoRegenerateBackImage;
	}

}
