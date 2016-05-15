package de.homelab.madgaksha.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.Gdx;

import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.logging.LoggerFactory;
import de.homelab.madgaksha.resources.Resource.EIcon;
import de.homelab.madgaksha.resources.ResourceLoader;

public class MainPanel extends JPanel implements ComponentListener {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnlCenter;
	private JLabel lblLevelImage;
	private JButton btnStart;
	private JButton btnDebug;
	private JButton btnOptions; 
	private JLabel lblTitle;
	private JLabel lblDescription;
	private LaunchConfig launchConfig;
	private int levelIconWidth, levelIconHeight;
	
	private static class LaunchConfig {
		private final static Logger LOG = LoggerFactory.getLogger(LaunchConfig.class);
		public Integer fps;
		public Integer width, height;
		public Boolean fullscreen;
		private final static String configFile = "config.properties";
		private final static int DEFAULT_WIDTH = 720;
		private final static int DEFAULT_HEIGHT = 480;
		private final static int DEFAULT_FPS = 30;
		private final static boolean DEFAULT_FULLSCREEN = false;
		public LaunchConfig(Integer w, Integer h, Integer f, Boolean fs) {
			Properties props = new Properties();		
			InputStream is = null;
			try {
				is = new FileInputStream(new File(configFile));
				props.load(is);
			}
			catch (Exception e) { // keep default configuration
			}
			finally {
				if (is != null) IOUtils.closeQuietly(is);
			}
			
			// Read from config file and merge with cli params.
			fullscreen = fs;
			fps = f;
			width = w;
			height = h;		
			if (fps == null) fps = parseInt(props.getProperty("fps"), DEFAULT_FPS);
			if (width == null) width = parseInt(props.getProperty("width"), DEFAULT_WIDTH);
			if (height == null) height = parseInt(props.getProperty("height"), DEFAULT_HEIGHT);
			if (fullscreen == null) fullscreen = parseBoolean(props.getProperty("fullscreen"), DEFAULT_FULLSCREEN);
			writeConfig();
		}
		private Integer parseInt(String number, Integer defaultNumber) {
			if (number == null) return defaultNumber;
			final Scanner s = new Scanner(number);
			Integer x;
			if (s.hasNextInt()) {
				x = s.nextInt();
			}
			else x = defaultNumber;
			s.close();
			return x;
		}
		private Boolean parseBoolean(String bool, Boolean defaultBoolean) {
			if (bool == null) return defaultBoolean;
			final Scanner s = new Scanner(bool);
			Boolean b;
			if (s.hasNextBoolean()) {
				b = s.nextBoolean();
			}
			else b = defaultBoolean;
			s.close();
			return b;
		}		
		public void writeConfig() {
			Properties props = new Properties();
			props.setProperty("width", String.valueOf(width));
			props.setProperty("height", String.valueOf(height));
			props.setProperty("fps", String.valueOf(fps));
			props.setProperty("fullscreen", String.valueOf(fullscreen));
			OutputStream os = null;
			try {
				os = new FileOutputStream(new File(configFile));
				LOG.log(Level.INFO, "writing configuration to " + new File(configFile).getAbsolutePath());
				props.store(os, "main configuration");
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "could not write configuration file", e);
			}
			finally {
				if (os != null) IOUtils.closeQuietly(os);
			}
		}
		public void setDefaults() {
			fps = DEFAULT_FPS;
			width = DEFAULT_WIDTH;
			height = DEFAULT_HEIGHT;
			fullscreen = DEFAULT_FULLSCREEN;
		}
	}
	
	/**
	 * Create the frame.
	 */
	public MainPanel(Integer width, Integer height, Integer fps, Boolean fullscreen) {

		launchConfig = new LaunchConfig(width, height, fps, fullscreen);
		
		contentPane = this;
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(20, 20));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JLabel txtpnScore = new JLabel();
		txtpnScore.setText("SCORE");
		panel.add(txtpnScore);

		panel.add(Box.createHorizontalGlue());

		btnStart = new JButton(i18n.main("desktop.game.start"));
		panel.add(btnStart);

		btnDebug = new JButton("debug button");
		panel.add(btnDebug);
		
		panel.add(Box.createHorizontalGlue());

		final OptionsFrame frmOptions = new OptionsFrame(launchConfig);
		btnOptions = new JButton(i18n.main("desktop.options"));
		btnOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				frmOptions.centerFrame(getWidth(), getHeight());
				frmOptions.applyConfig();
				frmOptions.setVisible(true);				
			}
		});
		panel.add(btnOptions);

		JButton button = new JButton(">>");
		contentPane.add(button, BorderLayout.EAST);

		JButton button_1 = new JButton("<<");
		contentPane.add(button_1, BorderLayout.WEST);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		panel_1.add(Box.createHorizontalGlue());

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		panel_1.add(panel_2);

		lblTitle = new JLabel();
		lblTitle.setText("Title");
		lblTitle.setFont(lblTitle.getFont().deriveFont(25.0f).deriveFont(Font.BOLD));
		panel_2.add(lblTitle);

		lblDescription = new JLabel();
		lblDescription.setText("Description");
		panel_2.add(lblDescription);

		panel_1.add(Box.createHorizontalGlue());

		pnlCenter = new JPanel();
		contentPane.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout(new GridBagLayout());

		Icon icnLevel = ResourceLoader.icon(EIcon.LEVEL_FOO);
		lblLevelImage = new JImageLabel(icnLevel, SwingConstants.CENTER);
		pnlCenter.add(lblLevelImage);
		lblLevelImage.setAlignmentY(CENTER_ALIGNMENT);

		addComponentListener(this);
		setPreferredSize(new Dimension(1280, 720));
	}

	@Override
	public void componentHidden(ComponentEvent paramComponentEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent paramComponentEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent paramComponentEvent) {
		performSizeAdjustment();
	}

	@Override
	public void componentShown(ComponentEvent paramComponentEvent) {
		performSizeAdjustment();
	}

	private void performSizeAdjustment() {
		// Resize level icon
		int width = pnlCenter.getWidth();
		int height = pnlCenter.getHeight();
		if (width * 9 > height * 16) {
			width = height * 16 / 9;
		} else {
			height = width * 9 / 16;
		}
		width = Math.max(0, width);
		height = Math.max(0, height);
		levelIconWidth = width;
		levelIconHeight = height;
		lblLevelImage.setPreferredSize(new Dimension(width, height));
		pnlCenter.revalidate();
	}

	public JButton getStartButton() {
		return btnStart;
	}
	
	public JButton getOptionsButton() {
		return btnOptions;
	}
	
	public JButton getDebugButton() {
		return btnDebug;
	}

	// http://stackoverflow.com/a/28226779/3925216
	private class JImageLabel extends JLabel {
		private static final long serialVersionUID = 1L;
		private Image image;

		public JImageLabel(Icon icon, int alignment) {
			super(icon, alignment);
			setIcon(icon);
		}

		public void setIcon(Icon icon) {
			super.setIcon(icon);
			if (icon instanceof ImageIcon) {
				image = ((ImageIcon) icon).getImage();
			}
		}

		@Override
		public void paint(Graphics g) {
			g.drawImage(image, 0, 0, levelIconWidth, levelIconHeight, null);
		}
	}
	
	public void setLevel(ImageIcon icon, String title, String description) {
		lblLevelImage.setIcon(icon);
		lblTitle.setText(title);
		lblDescription.setText(description);
	}

	private class OptionsFrame extends JFrame implements WindowListener {
		private static final long serialVersionUID = 1L;
		private JSpinner spnWidth;
		private JSpinner spnHeight;
		private JSpinner spnFps;
		private JCheckBox cbFullscreen;
		private LaunchConfig launchConfig;
		
		public OptionsFrame(LaunchConfig c) {
			JFrame frmOptions = this;
			frmOptions.setTitle(i18n.main("desktop.options.title"));

			JPanel pnlContent = new JPanel();
			pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
			pnlContent.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			// Options
			JPanel pnlKeyVal = new JPanel(new SpringLayout());

			// Fps
			JLabel lblFps = new JLabel(i18n.main("desktop.options.fps"));
			spnFps = new JSpinner(new SpinnerNumberModel(30, 10, 120, 5));
			spnFps.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ev) {
					try {
						int fps = (Integer)(((JSpinner)ev.getSource()).getValue());
						launchConfig.fps = fps;
					}
					catch (Exception ex) {
					}
				}
			});
			pnlKeyVal.add(lblFps);
			pnlKeyVal.add(spnFps);
			
			// Width
			JLabel lblWidth = new JLabel(i18n.main("desktop.options.width"));
			spnWidth = new JSpinner(new SpinnerNumberModel(100, 100, 9999, 10));
			spnWidth.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ev) {
					try {
						int w = (Integer)(((JSpinner)ev.getSource()).getValue());
						launchConfig.width = w;
					}
					catch (Exception ex) {
					}
				}
			});
			pnlKeyVal.add(lblWidth);
			pnlKeyVal.add(spnWidth);
			

			// Height
			JLabel lblHeight = new JLabel(i18n.main("desktop.options.height"));
			spnHeight = new JSpinner(new SpinnerNumberModel(100, 100, 9999, 10));
			spnHeight.addKeyListener(new KeyAdapter(){
				@Override
				public void keyReleased(KeyEvent ev) {
					try {
						int h = (Integer)(((JSpinner)ev.getSource()).getValue());
						launchConfig.height = h;
					}
					catch (Exception ex) {
					}					
				}
			});
			spnHeight.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ev) {
					try {
						int h = (Integer)(((JSpinner)ev.getSource()).getValue());
						launchConfig.height = h;
					}
					catch (Exception ex) {
					}
				}
			});
			pnlKeyVal.add(lblHeight);
			pnlKeyVal.add(spnHeight);

			// Fullscreen?
			JLabel lblFullscreen = new JLabel(i18n.main("desktop.options.fullscreen"));
			cbFullscreen = new JCheckBox();
			cbFullscreen.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent ev) {
					launchConfig.fullscreen = ((JCheckBox)ev.getSource()).isSelected();
				}
			});
			pnlKeyVal.add(lblFullscreen);
			pnlKeyVal.add(cbFullscreen);			
			
			// Set key-value input fields.
			SpringUtilities.makeCompactGrid(pnlKeyVal, 4, 2, 3, 3, 3, 3);
			pnlContent.add(pnlKeyVal);
			
			// Close button
			JPanel pnlButtons= new JPanel();
			pnlButtons.setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.gridx = 0;
			cons.weightx = 1;
			cons.gridwidth = 1;
			cons.gridheight = 2;
			JButton btnClose = new JButton(i18n.main("desktop.options.close"));
			btnClose.setAlignmentX(CENTER_ALIGNMENT);
			btnClose.setContentAreaFilled(true);
			btnClose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent paramActionEvent) {
					setVisible(false);
					launchConfig.writeConfig();
				}
			});
			// Default button
			JButton btnDefault = new JButton(i18n.main("desktop.options.default"));
			btnDefault.setAlignmentX(CENTER_ALIGNMENT);
			btnDefault.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent paramActionEvent) {
					launchConfig.setDefaults();
					launchConfig.writeConfig();
					applyConfig();					
				}
			});

			pnlButtons.add(btnDefault,cons);
			pnlButtons.add(btnClose,cons);
			pnlContent.add(pnlButtons);
			
			frmOptions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frmOptions.setContentPane(pnlContent);
			frmOptions.pack();
			
			addWindowListener(this);
			
			launchConfig = c;
			applyConfig();
		}
		
		public void centerFrame(int parentWidth, int parentHeight) {
			setBounds(parentWidth/2 - getWidth()/2, parentHeight/2 - getHeight()/2, getWidth(), getHeight());
		}
		
		public void applyConfig() {
			spnFps.setValue(launchConfig.fps);
			spnWidth.setValue(launchConfig.width);
			spnHeight.setValue(launchConfig.height);
			cbFullscreen.setSelected(launchConfig.fullscreen);
		}

		@Override
		public void windowActivated(WindowEvent paramWindowEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent paramWindowEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent paramWindowEvent) {
			launchConfig.writeConfig();
		}

		@Override
		public void windowDeactivated(WindowEvent paramWindowEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent paramWindowEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent paramWindowEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent paramWindowEvent) {
			// TODO Auto-generated method stub
			
		}
	}

	public void writeConfig() {
		launchConfig.writeConfig();		
	}

	public int getConfigWidth() {
		return launchConfig.width;
	}

	public int getConfigHeight() {
		return launchConfig.height;
	}

	public int getConfigFps() {
		return launchConfig.fps;
	}

	public boolean getConfigFullscreen() {
		return launchConfig.fullscreen;
	}
}
