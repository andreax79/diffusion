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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.github.andreax79.diffusion.Settings;
import com.github.andreax79.diffusion.space.GridSpace;
import com.github.andreax79.diffusion.space.Site;
import com.github.andreax79.diffusion.space.VonNeumannGridSpace;
import com.github.andreax79.diffusion.ide.Monitor;
import com.github.andreax79.diffusion.ide.GridView;
import com.github.andreax79.diffusion.ide.ContrastTheme;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class App extends JApplet implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 200;
	public static final int HEIGHT = 200;
	
	protected int width = WIDTH;
	protected int height = HEIGHT;

	protected Settings settings = new Settings();
	protected int idle = 100;
	protected GridSpace space;
	protected GridView gridView;
	protected Thread thread;  //  @jve:decl-index=0:
	protected boolean nowDie;
	protected boolean updateCells;
	
	private static Random random = new Random();
	
	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	private JPanel jLeftPanel = null;
		
	// Temperature
	private JLabel temperatureLabel = null;
	private JSlider temperatureSlider = null;

	// WW
	private JLabel pbWWLabel = null;
	private JSlider pbWWSlider = null;
	private JLabel jWWLabel = null;
	private JSlider jWWSlider = null;

	// SS
	private JLabel pbSSLabel = null;
	private JSlider pbSSSlider = null;
	private JLabel jSSLabel = null;
	private JSlider jSSSlider = null;

	// WS
	private JLabel pbWSLabel = null;
	private JSlider pbWSSlider = null;
	private JLabel jWSLabel = null;
	private JSlider jWSSlider = null;

	private Monitor monitor = null;

	private JPanel jButtonsPanel = null;

	private JButton jButtonStart = null;
	private JButton jButtonStop = null;
	private JButton jButtonReset = null;
	
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerSize(10);
			jSplitPane.setLeftComponent(getJLeftPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jLeftPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJLeftPanel() {
		if (jLeftPanel == null) {
			jWSLabel = new JLabel();
			jWSLabel.setText("JWS:");
			jSSLabel = new JLabel();
			jSSLabel.setText("JSS:");
			pbWSLabel = new JLabel();
			pbWSLabel.setText("PbWS:");
			pbSSLabel = new JLabel();
			pbSSLabel.setText("PbSS:");
			jWWLabel = new JLabel();
			jWWLabel.setText("JWW");
			pbWWLabel = new JLabel();
			pbWWLabel.setText("PbWW");
			temperatureLabel = new JLabel();
			temperatureLabel.setText("temperature");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;

			jLeftPanel = new JPanel();
			jLeftPanel.setLayout(new BoxLayout(getJLeftPanel(), BoxLayout.Y_AXIS));
			jLeftPanel.setPreferredSize(new Dimension(100, 0));

			jLeftPanel.add(new JLabel(" "));
			jLeftPanel.add(temperatureLabel, null);
			jLeftPanel.add(temperatureSlider(), null);
			
			jLeftPanel.add(new JLabel(" "));
			jLeftPanel.add(new JLabel("Solvent"));
			jLeftPanel.add(new JLabel(" "));

			jLeftPanel.add(pbWWLabel, null);
			jLeftPanel.add(getpbWWSlider(), null);
			jLeftPanel.add(jWWLabel, null);
			jLeftPanel.add(getJWWSlider(), null);

			jLeftPanel.add(new JLabel(" "));
			jLeftPanel.add(new JLabel("Solute"));
			jLeftPanel.add(new JLabel(" "));

			jLeftPanel.add(pbSSLabel, null);
			jLeftPanel.add(getJSliderPbSS());
			jLeftPanel.add(jSSLabel, null);
			jLeftPanel.add(getJSliderJSS());
			
			jLeftPanel.add(new JLabel(" "));
			jLeftPanel.add(new JLabel("Solvent-Solute"));
			jLeftPanel.add(new JLabel(" "));

			jLeftPanel.add(pbWSLabel, null);
			jLeftPanel.add(getJSliderPbWS(), getJSliderPbWS().getName());
			jLeftPanel.add(jWSLabel, null);
			jLeftPanel.add(getJSliderJWS());

			jLeftPanel.add(getJButtonsPanel(), null);
			jLeftPanel.add(new JLabel(" "));		
			jLeftPanel.add(new JPanel());
			
			jLeftPanel.add(getMonitor(), null);
			updateLabels();
		}
		return jLeftPanel;
	}

	/**
	 * This method initializes temperatureSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider temperatureSlider() {
		if (temperatureSlider == null) {
			temperatureSlider = new JSlider();
			temperatureSlider.setValue((int)(settings.getTemperature()*100));
			temperatureSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					settings.setTemperature(temperatureSlider.getValue() / 100.0);
					pbWWSlider.setValue((int)(settings.getPbWW()*100));
					jWWSlider.setValue((int)(settings.getJWW()*25));
					pbWSSlider.setValue((int)(settings.getPbWS()*100));
					jWSSlider.setValue((int)(settings.getJWS()*25));
					pbSSSlider.setValue((int)(settings.getPbSS()*100));
					jSSSlider.setValue((int)(settings.getJSS()*25));
					updateLabels();
				}
			});
		}
		return temperatureSlider;
	}

	/**
	 * This method initializes pbWWSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getpbWWSlider() {
		if (pbWWSlider == null) {
			pbWWSlider = new JSlider();
			pbWWSlider.setValue((int)(settings.getPbWW()*100));
			pbWWSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					settings.setPbWW(pbWWSlider.getValue() / 100.0);
					updateLabels();
				}
			});
		}
		return pbWWSlider;
	}

	/**
	 * This method initializes sensibilitySlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJWWSlider() {
		if (jWWSlider == null) {
			jWWSlider = new JSlider();
			jWWSlider.setValue((int)(settings.getJWW()*25));
			jWWSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					settings.setJWW(jWWSlider.getValue() / 25.0);
					updateLabels();
				}
			});
		}
		return jWWSlider;
	}


	/**
	 * This method initializes jSliderPbSS	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderPbSS() {
		if (pbWSSlider == null) {
			pbWSSlider = new JSlider();
			pbWSSlider.setValue((int)(settings.getPbSS() * 100));
			pbWSSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					settings.setPbSS(pbWSSlider.getValue() / 100.0);
					updateLabels();
				}
			});
		}
		return pbWSSlider;
	}

	/**
	 * This method initializes jSliderPbWS	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderPbWS() {
		if (pbSSSlider == null) {
			pbSSSlider = new JSlider();
			pbSSSlider.setValue((int)(settings.getPbWS() * 100));
			pbSSSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					settings.setPbWS(pbSSSlider.getValue() / 100.0);
					updateLabels();
				}
			});

		}
		return pbSSSlider;
	}

	/**
	 * This method initializes jSliderJSS	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderJSS() {
		if (jSSSlider == null) {
			jSSSlider = new JSlider();
			jSSSlider.setValue((int)(settings.getJSS() * 25));
			jSSSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					settings.setJSS(jSSSlider.getValue() / 25.0);
					updateLabels();
				}
			});

		}
		return jSSSlider;
	}

	/**
	 * This method initializes jSliderJWS	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getJSliderJWS() {
		if (jWSSlider == null) {
			jWSSlider = new JSlider();
			jWSSlider.setValue((int)(settings.getPbSS() * 25));
			jWSSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					settings.setJWS(jWSSlider.getValue() / 25.0);
					updateLabels();
				}
			});

		}
		return jWSSlider;
	}

	public GridSpace getSpace() {
		if (space == null) {
			space = new VonNeumannGridSpace(settings, width, height, Site.class);
			space.setupSubstance(40);
		}
		return space;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {
		// Set look & feel
        try { 
            if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1) {
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Diffusion");
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            }
        	MetalLookAndFeel.setCurrentTheme(new ContrastTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }		
		
		try {
			int newHeight = Integer.parseInt(getParameter("spaceHeight"));
			if (newHeight > 0)
				height = newHeight;
		} catch (NumberFormatException ex) {
        } catch (NullPointerException ex) {
        } 
		
		try {
			int newWidth = Integer.parseInt(getParameter("spaceWidth"));
			if (newWidth > 0)
				width = newWidth;
		} catch (NumberFormatException ex) {
        } catch (NullPointerException ex) {
        } 
		
		this.setSize(1100, 820);
		this.setContentPane(getJContentPane());
				
		// Graphic window
		int d = GridView.defaultNodeDistance;
		gridView = new GridView(getSpace(), width*d, height*d);
        gridView.setAutoRegenerateBackImage(true);
        gridView.generateBackImage();
        
        jSplitPane.setRightComponent(gridView);

		thread = new Thread(this);
	}
	
	/**
	 * This method initializes monitor	
	 * 	
	 * @return Monitor	
	 */
	private Monitor getMonitor() {
		if (monitor == null) {
			monitor = new Monitor(getSpace());
		}
		return monitor;
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private void updateLabels() {
		temperatureLabel.setText(String.format("temperature: %.0f¡", settings.getTemperature()*100));
		pbWWLabel.setText(String.format("Pb(WW): %.2f", settings.getPbWW()));
		jWWLabel.setText(String.format("J(WW): %.2f", settings.getJWW()));
		pbSSLabel.setText(String.format("Pb(SS): %.2f", settings.getPbSS()));
		jSSLabel.setText(String.format("J(SS): %.2f", settings.getJSS()));
		pbWSLabel.setText(String.format("Pb(WS): %.2f", settings.getPbWS()));
		jWSLabel.setText(String.format("J(WS): %.2f", settings.getJWS()));
	}
	
	public void start() {
        thread.start();
	}

	public void stop() {
		nowDie = true;
	}

	public void destroy() {
		thread = null;
		space = null;
		gridView = null;
	}
	
	public void update() {
		int size = space.size();
		Site arr[] = new Site[size];
		System.arraycopy(space.getSites(), 0, arr, 0, size);
		// Shuffle array
		for (int i = size; i > 1; i--) {
			int j = random.nextInt(i);
			Site tmp = arr[i-1];
			arr[i-1] = arr[j];
			arr[j] = tmp;
		}
		
		// Action
		for (Site site : arr)
			site.action(settings);
	}
	
	public void run() {
		// Cycle
		try {
			while (!nowDie) {
				try {
					if (updateCells)
						for (int i=0; i<5; i++)
							update();
					gridView.repaint();
					Thread.sleep(idle);
				} catch (InterruptedException e) {
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String getAppletInfo() {
		return "Digital Footprints Applet - andrea.bonomi@gmail.com";
	}

	/**
	 * This method initializes jButtonsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonsPanel() {
		if (jButtonsPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jButtonsPanel = new JPanel();
			jButtonsPanel.setLayout(new GridBagLayout());
			jButtonsPanel.add(getJButtonStart(), gridBagConstraints1);
			jButtonsPanel.add(getJButtonStop(), gridBagConstraints2);
			jButtonsPanel.add(getJButtonReset(), gridBagConstraints3);
		}
		return jButtonsPanel;
	}

	/**
	 * This method initializes jButtonStart	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStart() {
		if (jButtonStart == null) {
			jButtonStart = new JButton();
			jButtonStart.setText("Start");
			jButtonStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateCells = true;
                    monitor.setUpdate(true);
				}
			});
		}
		return jButtonStart;
	}

	/**
	 * This method initializes jButtonStop	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStop() {
		if (jButtonStop == null) {
			jButtonStop = new JButton();
			jButtonStop.setText("Stop");
			jButtonStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateCells = false;
                    monitor.setUpdate(false);
				}
			});
		}
		return jButtonStop;
	}

	/**
	 * This method initializes jButtonReset	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonReset() {
		if (jButtonReset == null) {
			jButtonReset = new JButton();
			jButtonReset.setText("Reset");
			jButtonReset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateCells = false;
					space.reset();
					space.setupSubstance(40);
				}
			});
		}
		return jButtonReset;
	}

    public static void main( String[] args )
    {
      JApplet applet = new App();
      applet.init();

      final JFrame frame = new JFrame("FrameTitle");
      frame.setContentPane(applet.getContentPane());
      frame.setJMenuBar(applet.getJMenuBar());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Set the size of the frame.
      // To pack the frame as tightly as possible
      // replace the setSize() message with the following.
      frame.pack();
      // frame.setSize(FrameWidth, FrameHeight);

      // Set the location of the frame.
      // frame.setLocation(FrameX, FrameY);

      frame.setVisible(true);
      applet.start();

    }
}
