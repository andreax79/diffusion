
package com.github.andreax79.diffusion.ide;

/*
 * Copyright (c) 2002 Sun Microsystems, Inc. All  Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowlarc that Software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

/*
 * @(#)MemoryMonitor.java	1.31 02/06/13
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.github.andreax79.diffusion.space.GridSpace;


/**
 * Tracks Memory allocated & used, displayed in graph form.
 */
public class Monitor extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private Thread thread;
	private long sleepAmount = 500;
	private int w, h;
	private BufferedImage bimg;
	private Graphics2D big;
	private Font font = new Font("Times New Roman", Font.PLAIN, 11);
	private int columnInc;
	private int pts[];
	private int ptsD[];
	private int ptNum;
	private int ascent, descent;
	private Rectangle graphOutlineRect = new Rectangle();
	private Rectangle2D mfRect = new Rectangle2D.Float();
	private Line2D graphLine = new Line2D.Float();
	private GridSpace space;
	private double maxCapacity = 100;
    private boolean update;
    private boolean first;
	
	public Monitor(GridSpace space) {
		this.space = space;
		setBackground(Color.white);
        first = true;
		start();
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public Dimension getPreferredSize() {
		return new Dimension(256,150);
	}

	public void paint(Graphics g) {

		if (big == null) {
			return;
		}

		big.setBackground(getBackground());
		big.clearRect(0,0,w,h);

        double average = space.getAverage();
		double averageInitialArea = space.getAverageInitialArea();
		if (maxCapacity < space.getAverageInitialArea())
			maxCapacity = space.getAverageInitialArea();

		// .. Draw allocated and used strings ..
		big.setColor(Color.black);
		big.drawString("average central area:" + averageInitialArea, 4.0f, (float) ascent+0.5f);
		big.drawString("average: " + average, 4, h-descent);

		// .. Draw History Graph ..
		big.setColor(Color.black);
		int graphX = 5;
		int graphY = (int) ascent + descent;
		int graphW = w - graphX - 5;
		int graphH = (int) (h - ((ascent + descent) * 2) - 0.5f);
		graphOutlineRect.setRect(graphX, graphY, graphW, graphH);
		big.draw(graphOutlineRect);

		int graphRow = graphH/10;

		// .. Draw row ..
		for (int j = graphY; j <= graphH+graphY; j += graphRow) {
			graphLine.setLine(graphX,j,graphX+graphW,j);
			big.draw(graphLine);
		}

		// .. Draw animated column movement ..
		int graphColumn = graphW/15;

		if (columnInc == 0) {
			columnInc = graphColumn;
		}

		for (int j = graphX+columnInc; j < graphW+graphX; j+=graphColumn) {
			graphLine.setLine(j,graphY,j,graphY+graphH);
			big.draw(graphLine);
		}

		--columnInc;

		if (pts == null) {
			pts = new int[graphW];
			ptsD = new int[graphW];
			ptNum = 0;
		} else if (pts.length != graphW) {
			int tmp[] = null;
			int tmpD[] = null;
			if (ptNum < graphW) {     
				tmp = new int[ptNum];
				System.arraycopy(pts, 0, tmp, 0, tmp.length);
				tmpD = new int[ptNum];
				System.arraycopy(ptsD, 0, tmpD, 0, tmpD.length);
			} else {        
				tmp = new int[graphW];
				System.arraycopy(pts, pts.length-tmp.length, tmp, 0, tmp.length);
				tmpD = new int[graphW];
				System.arraycopy(ptsD, ptsD.length-tmpD.length, tmpD, 0, tmpD.length);
				ptNum = tmp.length - 2;
			}
			pts = new int[graphW];
			System.arraycopy(tmp, 0, pts, 0, tmp.length);
			ptsD = new int[graphW];
			System.arraycopy(tmpD, 0, ptsD, 0, tmpD.length);
		} else {
			if (ptNum < pts.length) {
				pts[ptNum] = (int)averageInitialArea;
				ptsD[ptNum] = (int)average;
				for (int j=graphX+graphW-ptNum, k=0;k < ptNum; k++, j++) {
					if (k != 0) {
						big.setColor(Color.red);
						if (pts[k] != pts[k-1]) {
							int pk0 = (int)(graphY+graphH*((maxCapacity-pts[k-1])/maxCapacity));
							int pk1 = (int)(graphY+graphH*((maxCapacity-pts[k])/maxCapacity));
							big.drawLine(j-1, pk0, j, pk1);
						} else {
							int pk1 = (int)(graphY+graphH*((maxCapacity-pts[k])/maxCapacity));
							big.fillRect(j, pk1, 1, 1);
						}
						big.setColor(Color.green);
						if (ptsD[k] != ptsD[k-1]) {
							int pk0 = (int)(graphY+graphH*((maxCapacity-ptsD[k-1])/maxCapacity));
							int pk1 = (int)(graphY+graphH*((maxCapacity-ptsD[k])/maxCapacity));
							big.drawLine(j-1, pk0, j, pk1);
						} else {
							int pk1 = (int)(graphY+graphH*((maxCapacity-ptsD[k])/maxCapacity));
							big.fillRect(j, pk1, 1, 1);
						}
					}
				}
			}
			if (ptNum+2 == pts.length) {
				// throw out oldest point
				for (int j = 1;j < ptNum; j++) {
					pts[j-1] = pts[j];
					ptsD[j-1] = ptsD[j];
				}
				--ptNum;
			} else {
				ptNum++;
			}
		}
		g.drawImage(bimg, 0, 0, this);
	}

    public void setUpdate(boolean update) {
        this.update = update;
    }

	public void start() {
		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setName("Monitor");
		thread.start();
	}

	public synchronized void stop() {
		thread = null;
		notify();
	}

	public void run() {

		Thread me = Thread.currentThread();

		while (thread == me && !isShowing() || getSize().width == 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { return; }
		}

		while (thread == me && isShowing()) {
            if (update || first) {
                Dimension d = getSize();
                if (d.width != w || d.height != h) {
                    w = d.width;
                    h = d.height;
                    bimg = (BufferedImage) createImage(w, h);
                    big = bimg.createGraphics();
                    big.setFont(font);
                    FontMetrics fm = big.getFontMetrics(font);
                    ascent = (int) fm.getAscent();
                    descent = (int) fm.getDescent();
                    first = false;
                }
                repaint();
            }
			try {
				Thread.sleep(sleepAmount);
			} catch (InterruptedException e) { e.printStackTrace(); break;  }
		}
		thread = null;
	}
	
}
