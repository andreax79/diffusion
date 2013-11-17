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

import com.github.andreax79.diffusion.Settings;

/**
 * Toroidal grid with Von Neumann neighborhood
 * @author Andrea Bonomi - andrea.bonomi@gmail.com
 */
public class VonNeumannTorusSpace extends VonNeumannGridSpace implements TorusSpace {

	/**
	 * Constructor
	 * @param name name
	 * @param width grid width
	 * @param height grid height
	 */

	public VonNeumannTorusSpace(Settings settings, int width, int height) {
		this(settings, width, height, Site.class);
	}

	/**
	 * Constructor
	 * @param width grid width
	 * @param height grid height
	 * @param siteClass site(node) class, subclass of Site
	 */

	@SuppressWarnings("unchecked")
	public VonNeumannTorusSpace(Settings settings, int width, int height, Class siteClass) {
		super(settings, width, height, siteClass);
	}

}
