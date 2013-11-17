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

import java.awt.Point;
import java.awt.event.MouseEvent;

import com.github.andreax79.diffusion.space.GridSpace;
import com.github.andreax79.diffusion.space.Site;
import com.github.andreax79.diffusion.space.State;

/**
 * Grid visualization
 * 
 * @author Andrea Bonomi - andrea.bonomi@gmail.com
 */
public class GridView extends AbstractView {
	
	private static final long serialVersionUID = 1L;

	/** Default node distance */
	public static final int defaultNodeDistance = 4;
	/** Node distace */
	public int nodeDistance = defaultNodeDistance;
	/** Mouse pressed */
	protected boolean pressed = false;
	/** Default node radius */
	public static final int defaultNodeRadius = 1;	
	/** Node radius */
	public int nodeRadius = defaultNodeRadius;

	/**
	 * Costructor
	 */
	public GridView(GridSpace space, int width, int height) {
		super(space, width, height);
	}

	/**
	 * Generate back image (nodes and arcs)
	 */
	public synchronized void generateBackImage() {
		if (backGOff == null) {
			// Create image buffer
			backOffScreen = createImage(width, height);
			if (backOffScreen == null)
				return;
			backGOff = backOffScreen.getGraphics();
			// Set antialias
			setBackHighRenderingQuality(true);
		}

		// Clear
		backGOff.setColor(backgroundColor);
		backGOff.fillRect(0, 0, width, height);
		backGOff.setFont(labelFont);

		// Draws nodes
		drawNodes();
	}

	/**
	 * Draws nodes
	 */
	protected void drawNodes() {
		for (int i = 0; i< space.getHeight(); i++)
			for (int j = 0; j< space.getWidth(); j++) {
				Site site = space.getSite(j, i);
				int x = (site.x + 1) * nodeDistance;
				int y = (site.y + 1) * nodeDistance;
				backGOff.setColor(site.getColor());
				backGOff.fillRect(x - nodeRadius, y - nodeRadius, 2*nodeRadius, 2*nodeRadius);
			}
	}

	public synchronized void generateImage() {
		if (pressed) {
			Point mousePosition = this.getMousePosition();
			if (mousePosition != null)
				excitate(mousePosition.x, mousePosition.y);
		}
		
		if (gOff == null) {
			offScreen = createImage(width, height);
			gOff = offScreen.getGraphics();
		}

		if ((backGOff == null) || (autoRegenerateBackImage))
			generateBackImage();

		// Clear
		gOff.setColor(backgroundColor);
		gOff.fillRect(0, 0, width, height);

		// Draw back image
		gOff.drawImage(backOffScreen, 0, 0, null);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
	}
	
	public void excitate(int mouseX, int mouseY) {
		try {
			int ax = (mouseX - nodeDistance / 2) / nodeDistance;
			int ay = (mouseY - nodeDistance / 2) / nodeDistance;
			Site tempSite = space.getSite(ax, ay);
			if (tempSite.state == State.WATER)
				tempSite.state = State.EMPTY;
			else if (tempSite.state == State.EMPTY)
				tempSite.state = State.WATER;
		} catch (IllegalArgumentException ex) {}
	}
	
	public int getNodeDistance() {
		return nodeDistance;
	}

	public void setNodeDistance(int nodeDistance) {
		this.nodeDistance = nodeDistance;
	}

}
