/**
 * Copyright (C) 2007 Andrea Bonomi - <andrea.bonomi@gmail.com>
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

package com.github.andreax79.diffusion.space;

import java.lang.reflect.Constructor;

import com.github.andreax79.diffusion.Settings;

/**
 * Space interface
 * 
 * @since 07/01/2007
 * @author Andrea Bonomi - andrea.bonomi@gmail.com
 */
public class GridSpace {

	/** Width */
	protected int width;
	/** Height */
	protected int height;
	/** Sites */
	protected Site[] sites;
    /** Settings */
    private Settings settings;
    /** SoluteCells */
    private int soluteCells;
    /** Initial area size */
    private int areaSize;
    
    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
	public GridSpace(Settings settings, int width, int height, Class siteClass) {
		this.settings = settings;

		if (width < 1)
			throw new IllegalArgumentException("width must be > 0");
		if (height < 1)
			throw new IllegalArgumentException("height must be > 0");
		if (siteClass == null)
			siteClass = Site.class;

		this.width=width;
		this.height=height;

		sites = new Site[height*width];

		Constructor siteConstructor;
		try {
			siteConstructor = siteClass.getConstructor(Integer.class, GridSpace.class);
		} catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException(ex);
		}

		try {
			// Creates nodes and adds to the HashMap
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int id = (i * width) + j;
					sites[id] = (Site)siteConstructor.newInstance(id, this);
				}
			}
		} catch (Throwable ex) {
			throw new IllegalArgumentException(ex);
		}
	}

    /**
     * @return Returns the number of sites
     */
    public int size() {
        return width * height;
    }

    /**
     * @return Returns the settings
     */
    public Settings getSettings() {
		return settings;
	}

	/**
	 * Return grid width
	 */

	public int getWidth() {
		return width;
	}

	/**
	 * Return grid height
	 */

	public int getHeight() {
		return height;
	}

	/**
	 * Gets sites
	 */

	public Site[] getSites() {
		return sites;
	}

	/**
	 * Get a site by x and y
	 */

	public Site getSite(int x, int y){
		if ((x < 0) || (y < 0) || (x >= width) || (y >= height))
			throw new IllegalArgumentException("invalid site x="+x+",y="+y);
		return sites[(y * width) + x];
	}

	/**
	 * @return Retuns an adj site by a specific tag
	 */
	public Site getAdjByTag(Site site, SiteTag tag) {
		int x = site.x;
		int y = site.y;
		switch (tag) {
		case NORTH: y--; break;
		case EAST: x++; break;
		case SOUTH: y++; break;
		case WEST: x--; break;
		}
		if (this instanceof TorusSpace) {
			x = x % width;
			y = y % height;
			return getSite(x, y);
		} else {
			if ((x < 0) || (x >= width) || (y < 0) || (y >= height))
				return null;
			else
				return getSite(x, y);
		}
	}

	public void reset() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int id = (i * width) + j;
				sites[id].state = (Math.random() <= settings.getPWater()) ? State.WATER : State.EMPTY;
			}
		}
	}
	
	public void setupSubstance(int initialAreaSize) {
/*		this.areaSize = initialAreaSize;
		int l = initialAreaSize/2;
		for (int y=height/2-l;y<height/2+l; y++)
			for (int x=width/2-l;x<width/2+l; x++)
				getSite(x, y).state = State.SUBSTANCE;
		soluteCells = initialAreaSize*initialAreaSize;*/
		this.areaSize = initialAreaSize;
		int l = initialAreaSize/2;
		for (int y=height/2-l;y<height/2+l; y++)
			for (int x=width/2-l;x<width/2+l; x++) {
				double distance = Math.sqrt(Math.pow(y-height/2, 2) + Math.pow(x-width/2, 2));
				if (distance <= l)
					getSite(x, y).state = (Math.random() <= settings.getPSubstance()) ? State.SUBSTANCE : State.EMPTY;
			}
		soluteCells = initialAreaSize*initialAreaSize;
	}
	
	public double getAverageInitialArea() {
		int l = areaSize/2;
		int c = 0;
		for (int y=height/2-l;y<height/2+l; y++)
			for (int x=width/2-l;x<width/2+l; x++)
				if (sites[(y * width) + x].state == State.SUBSTANCE)
					c++;
		return 100.0*c/(1.0*areaSize*areaSize);
	}

	public double getAverage() {
		return 100.0*soluteCells/(1.0*width*height);
	}
	
	public int getSoluteCells() {
		return soluteCells;
	}

	public void setSoluteCells(int soluteCells) {
		this.soluteCells = soluteCells;
	}

}
