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

package com.github.andreax79.diffusion.space;

import java.awt.Color;

import com.github.andreax79.diffusion.Settings;

/**
 * Site of a space 4 nov 2005 - generics
 * 
 * @author Andrea Bonomi - andrea.boboni@gmail.com
 */
public class Site {

    /** Space of this site */
    private GridSpace space;

    /** Site ID */
    public final int id;
    public final int x;
    public final int y;
    public State state;

    /** Constructor */
    public Site(Integer id, GridSpace space) {
        this.id = id;
        this.space = space;
        int spaceWidth = ((GridSpace) space).getWidth();
        this.y = id / spaceWidth;
		this.x = id - (y * spaceWidth);
        state = (Math.random() <= 0.65) ? State.WATER : State.EMPTY;
    }
    
    /** Returns the site id */
    public int getId() {
        return id;
    }

	public void action(Settings settings) {
		if (state == State.EMPTY)
			return;
		else if ((state == State.WATER) || (state == State.SUBSTANCE)){
			Site n[] = new Site[4];
			n[0] = getAdjByTag(SiteTag.NORTH);
			n[1] = getAdjByTag(SiteTag.EAST);
			n[2] = getAdjByTag(SiteTag.SOUTH);
			n[3] = getAdjByTag(SiteTag.WEST);

			int e = 0; // number of empty sites
			int w = 0; // number of water sites
			int s = 0;
			for (Site site : n) {
				if (site != null) {
					if (site.state == State.EMPTY)
						e++;
					else if (site.state == State.WATER)
						w++;
					else if (site.state == State.SUBSTANCE)
						s++;
				}
			}
			double pm = 0;
			if (state == State.WATER)
				pm = Math.pow(settings.getPbWW(), w) * Math.pow(settings.getPbWS(), s);
			if (state == State.SUBSTANCE)
				pm = Math.pow(settings.getPbSS(), s) * Math.pow(settings.getPbWS(), w);
			if ((e > 0) && (Math.random() < pm)) {
				int p = (int)Math.floor(Math.random() * e);
				int c = 0;
				for (int i=0; i<4; i++) {
					if ((n[i] != null) && (n[i].state == State.EMPTY)) {
						if (p == c) {
							n[i].state = state;
							state = State.EMPTY;
						}
						c++;
					}
				}
			}
			
/*			int c = (int)(Math.floor(Math.random() * 4));
			Site n = null;
			switch (c) {
				case 0: n = getAdjByTag(SiteTag.NORTH);
					break;
				case 1: n = getAdjByTag(SiteTag.EAST);
					break;
				case 2: n = getAdjByTag(SiteTag.SOUTH);
					break;
				case 3: n = getAdjByTag(SiteTag.WEST);
					break;
			}
			if (n != null && n.state == State.EMPTY) {
				n.state = State.WATER;
				state = State.EMPTY;
			}*/
		}
	}
	
    /**
     * @return Returns the space
     */
    public GridSpace getSpace() {
        return space;
    }
    
    /**
     * Get an adj Site by its
     */
    public Site getAdjByTag(SiteTag tag) {
    	return space.getAdjByTag(this, tag);
    }

	public Color getColor() {
		return state.color;
	}

}
