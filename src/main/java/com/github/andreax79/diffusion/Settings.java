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

package com.github.andreax79.diffusion;

import java.io.Serializable;

public class Settings implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected double temperature;
	protected double pbWW;
	protected double jWW;
	
	protected double pbSS;
	protected double jSS;
	protected double pbWS;
	protected double jWS;

	protected double pWater = 0.65;
	protected double pSubstance = 1; 
	
	public Settings() {
		reset();
	}
	
	public void reset() {
		temperature = 0.38;
		recalculate();
	}
	
	public void recalculate() {
		pbWW = temperature;
		pbWS = pbWW * 1.68;
		if (pbWS > 1)
			pbWS = 1;
		pbSS = pbWW * 1.68;
		if (pbSS > 1)
			pbSS = 1;
		calculateJWW();
		calculateJWS();
		calculateJSS();
	}
	
	public void calculateJWW() {
		jWW = Math.pow(10, -1.5 * pbWW + 0.6);		
	}
	
	public void calculateJWS() {
		jWS = Math.pow(10, -1.5 * pbWS + 0.6);		
	}
	
	public void calculateJSS() {
		jSS = Math.pow(10, -1.5 * pbSS + 0.6);		
	}
	
	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
		recalculate();
	}

	public double getPbWW() {
		return pbWW;
	}

	public void setPbWW(double pbWW) {
		this.pbWW = pbWW;
	}

	public double getJWW() {
		return jWW;
	}

	public void setJWW(double jWW) {
		this.jWW = jWW;
	}

	public double getPbSS() {
		return pbSS;
	}

	public void setPbSS(double pbSS) {
		this.pbSS = pbSS;
	}

	public double getJSS() {
		return jSS;
	}

	public void setJSS(double jSS) {
		this.jSS = jSS;
	}

	public double getPbWS() {
		return pbWS;
	}

	public void setPbWS(double pbWS) {
		this.pbWS = pbWS;
	}

	public double getJWS() {
		return jWS;
	}

	public void setJWS(double jWS) {
		this.jWS = jWS;
	}

	public double getPWater() {
		return pWater;
	}

	public void setPWater(double pWater) {
		this.pWater = pWater;
	}

	public double getPSubstance() {
		return pSubstance;
	}

	public void setPSusbtance(double pSubstance) {
		this.pSubstance = pSubstance;
	}

}
