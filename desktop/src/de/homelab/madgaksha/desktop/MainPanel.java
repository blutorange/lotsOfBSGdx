package de.homelab.madgaksha.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import de.homelab.madgaksha.i18n.I18n;
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
		
	/**
	 * Create the frame.
	 */
	public MainPanel(Integer width, Integer height, Integer fps, Boolean fullscreen, Float textboxSpeed) {

		launchConfig = new LaunchConfig(width, height, fps, fullscreen, textboxSpeed);
		
		contentPane = this;
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(20, 20));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JLabel txtpnScore = new JLabel();
		txtpnScore.setText(I18n.main("desktop.launcher.score"));
		panel.add(txtpnScore);

		panel.add(Box.createHorizontalGlue());

		btnStart = new JButton(I18n.main("desktop.game.start"));
		panel.add(btnStart);

		btnDebug = new JButton("debug button");
		panel.add(btnDebug);
		
		panel.add(Box.createHorizontalGlue());

		final OptionsFrame frmOptions = new OptionsFrame(launchConfig);
		btnOptions = new JButton(I18n.main("desktop.options"));
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

	public Float getTextboxSpeed() {
		return launchConfig.textboxSpeed;
	}
}
