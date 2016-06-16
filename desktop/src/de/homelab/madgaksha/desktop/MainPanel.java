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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.IOUtils;

import de.homelab.madgaksha.i18n.I18n;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.level.Level01;
import de.homelab.madgaksha.logging.LoggerFactory;
import de.homelab.madgaksha.resources.Resource.EIcon;
import de.homelab.madgaksha.resources.ResourceLoader;
import de.homelab.madgaksha.util.Score;

public class MainPanel extends JPanel implements ComponentListener {
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = LoggerFactory.getLogger(DesktopLauncher.class);
	
	private final static String highScoreFile = "./toku.ten";
	
	private final ActionListener onlyOneLevel = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, I18n.main("only.one.level"));
		}
	};
	
	private JPanel contentPane;
	private JPanel pnlCenter;
	private JLabel lblLevelImage;
	private JButton btnStart;
	private JButton btnDebug;
	private JButton btnOptions; 
	private JLabel lblTitle;
	private JLabel lblDescription;
	private JLabel lblScore;
	private LaunchConfig launchConfig;
	private int levelIconWidth, levelIconHeight;
	
	private int currentLevelIndex = 0;
	private Map<Class<? extends ALevel>, Long> scoreMap;
	private final static List<ALevel> levelList = new ArrayList<ALevel>();
	static {
		levelList.add(new Level01());
	}
	
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

		// Score label
		JLabel txtpnScore = new JLabel();
		txtpnScore.setText(I18n.main("desktop.launcher.score"));
		panel.add(txtpnScore);
		
		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		// Score number
		lblScore = new JLabel();
		lblScore.setText("0");
		panel.add(lblScore);
		
		panel.add(Box.createHorizontalGlue());

		// Start button
		btnStart = new JButton(I18n.main("desktop.game.start"));
		panel.add(btnStart);

		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		// Quit game button
		btnDebug = new JButton(I18n.main("desktop.game.exit"));
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

		// Left and right button.
		JButton buttonNext = new JButton(">>");
		contentPane.add(buttonNext, BorderLayout.EAST);	

		JButton buttonLast = new JButton("<<");
		contentPane.add(buttonLast, BorderLayout.WEST);

		buttonNext.addActionListener(onlyOneLevel);
		buttonLast.addActionListener(onlyOneLevel);
		
		// Title and description.
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
		
		lblDescription = new JLabel();
		lblDescription.setText("Description");
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		panel_3.add(Box.createHorizontalGlue());
		panel_3.add(lblTitle);
		panel_3.add(Box.createHorizontalGlue());
		panel_2.add(panel_3);
				
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		panel_4.add(Box.createHorizontalGlue());
		panel_4.add(lblDescription);
		panel_4.add(Box.createHorizontalGlue());		
		panel_2.add(panel_4);
		
		panel_1.add(Box.createHorizontalGlue());

		pnlCenter = new JPanel();
		contentPane.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout(new GridBagLayout());

		Icon icnLevel = ResourceLoader.icon(EIcon.LEVEL_01);
		lblLevelImage = new JImageLabel(icnLevel, SwingConstants.CENTER);
		pnlCenter.add(lblLevelImage);
		lblLevelImage.setAlignmentY(CENTER_ALIGNMENT);

		addComponentListener(this);
		setPreferredSize(new Dimension(1280, 720));
		
		// Read high score file.
		updateScore();
	}

	private void loadScore() {
		BufferedInputStream bis = null;
		File hsf = new File(highScoreFile);
		if (hsf.exists()) {
			try {
				bis = new BufferedInputStream(new FileInputStream(highScoreFile));
			}
			catch (IOException e) {
				LOG.log(Level.WARNING, "could not read score file", e);
			}
		}
		scoreMap = Score.readScore(bis);
		if (bis != null) IOUtils.closeQuietly(bis);
	}
	
	public void updateScore() {
		loadScore();
		setLevel(currentLevelIndex);
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
	
	public void setLevel(int idx) {//ImageIcon icon, ALevel level, long score) {
		if (idx >= levelList.size()) idx = 0;
		ALevel level = levelList.get(idx);

		// Load level icon.
		Icon icon;
		try {
			String iconString = level.getLauncherIcon();
			EIcon iconEnum = EIcon.valueOf(iconString);
			icon = ResourceLoader.icon(iconEnum);
		}
		catch (IllegalArgumentException e) {
			LOG.log(Level.WARNING, "no such level icon", e);
			return;
		}
		
		// Get score, default to 0.
		long score;
		if (scoreMap.containsKey(level.getClass())) {
			score = scoreMap.get(level.getClass());
		}
		else score = 0L;
		
		// Set title, description, score.
		lblTitle.setText(level.getName());
		lblDescription.setText(level.getDescription());
		lblScore.setText(String.valueOf(score));
		
		// Set level icon.
		lblLevelImage.setIcon(icon);
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
