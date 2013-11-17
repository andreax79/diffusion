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

import java.awt.Color;
import java.awt.Font;

import com.github.andreax79.diffusion.space.GridSpace;

/**
 * Space visualization interface
 * 
 * @author Andrea Bonomi - andrea.bonomi@gmail.com
 * @since 27/12/2006
 */
public interface View {

    /** Background color */
    public static Color defaultBackgroundColor = Color.white;
    
    /** Arc color */
    public static Color defaultArcColor = Color.black;
    
    /** Node color */
    public static Color defaultNodeColor = Color.black;
    
    /** Empty node color */
    public static Color defaultEmptyNodeColor = Color.white;
    
    /** Label color */
    public static Color defaultSiteIdColor = Color.black;
    
    /** Selected node color */
    public static Color defaultSelectedNodeColor = Color.red;

    /**
     * @return Returns the space
     */
    public GridSpace getSpace();

    /**
     * @return Returns the background color
     */
    public Color getBackgroundColor();

    /**
     * Set the background color
     */
    public void setBackgroundColor(Color backgroundColor);

    /**
     * @return Returns the arc color
     */
    public Color getArcColor();

    /**
     * Set the arc color
     */
    public void setArcColor(Color arcColor);

    public Color getNodeColor();

    public void setNodeColor(Color nodeColor);

    public Color getEmptyNodeColor();

    public void setEmptyNodeColor(Color emptyNodeColor);

    public Color getSiteIdColor();

    public void setSiteIdColor(Color siteIdColor);

    public Color getSelectedNodeColor();

    public void setSelectedNodeColor(Color selectedNodeColor);

    public boolean getShowNodes();

    public void setShowNodes(boolean showNodes);

    /**
     * @return Returns "show labels" on/off
     */
    public boolean isShowLabel();

    /**
     * Toggle "show labels"
     */
    public void setShowLabel(boolean showLabel);

    /**
     * @return Returns "show interfaces" on/off
     */
    public boolean isShowInterface();

    /**
     * Toggle "show interfaces"
     */
    public void setShowInterface(boolean showInterface);

    /**
     * Re-generate back image
     */
    public abstract void generateBackImage();

    /**
     * Re-generate front image
     */
    public abstract void generateImage();

    /**
     * Set height/low rendering quality (antialias on/off)
     */
    public void setHighRenderingQuality(boolean quality);

    /**
     * Set height/low rendering quality for back image (antialias on/off)
     */
    public void setBackHighRenderingQuality(boolean quality);

    /**
     * @return Returns the label font
     */
    public Font getLabelFont();

    /**
     * Set label font
     */
    public void setLabelFont(Font labelFont);

    /**
     * Load a background image
     */
    public void loadBackgroundImage(String filename);

    /**
     * Repaint this component
     */
    public void repaint();
    
    /**
     * Update the view - this method is called from the space 
     */
    public void updateView();

}
