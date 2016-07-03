package de.homelab.madgaksha.desktop;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import de.homelab.madgaksha.i18n.I18n;

public class OptionsFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	private JSpinner spnWidth;
	private JSpinner spnHeight;
	private JSpinner spnFps;
	private JCheckBox cbFullscreen;
	private JSpinner spnTextboxSpeed;
	private LaunchConfig launchConfig;

	final InputChangeListener listenerFps = new InputChangeListener() {
		@Override
		public void inputChanged(int fps) {
			launchConfig.fps = Math.max(1, fps);
		}
	};
	
	final InputChangeListener listenerWidth = new InputChangeListener() {
		@Override
		public void inputChanged(int width) {
			launchConfig.width = Math.max(1, width);
		}
	};
	final InputChangeListener listenerHeight = new InputChangeListener() {
		@Override
		public void inputChanged(int height) {
			launchConfig.height = Math.max(1, height);
		}
	};
	final InputChangeListener listenerTextboxSpeed = new InputChangeListener() {
		@Override
		public void inputChanged(float textboxSpeed) {
			launchConfig.textboxSpeed = Math.max(0.1f, textboxSpeed);
		}
	};
	final InputChangeListener listenerFullscreen = new InputChangeListener() {
		@Override
		public void inputChanged(boolean fullscreen) {
			launchConfig.fullscreen = fullscreen;
		}
	};

	public OptionsFrame(LaunchConfig c) {
		JFrame frmOptions = this;
		frmOptions.setTitle(I18n.main("desktop.options.title"));

		JPanel pnlContent = new JPanel();
		pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
		pnlContent.setBorder(new EmptyBorder(5, 5, 5, 5));

		// Options
		JPanel pnlKeyVal = new JPanel(new SpringLayout());

		// Fps
		spnFps = FormComponentUtils.createNumberSpinner("desktop.options.fps", listenerFps, pnlKeyVal, 30, 10, 120, 5,
				true);

		// Width
		spnWidth = FormComponentUtils.createNumberSpinner("desktop.options.width", listenerWidth, pnlKeyVal, 640, 100,
				9999, 10, true);

		// Height
		spnHeight = FormComponentUtils.createNumberSpinner("desktop.options.height", listenerHeight, pnlKeyVal, 640,
				100, 9999, 10, true);

		// Fullscreen?
		cbFullscreen = FormComponentUtils.createBooleanCheckbox("desktop.options.fullscreen", listenerFullscreen,
				pnlKeyVal, false);

		// Textbox speed
		spnTextboxSpeed = FormComponentUtils.createNumberSpinner("desktop.options.textboxSpeed", listenerTextboxSpeed,
				pnlKeyVal, 10.0f, 2f, 100.0f, 0.5f, false);

		// =====================
		// INTPUT KEYS
		// =====================

		// Set key-value input fields.
		SpringUtilities.makeCompactGrid(pnlKeyVal, 5, 2, 3, 3, 3, 3);
		pnlContent.add(pnlKeyVal);

		// Close button
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.gridx = 0;
		cons.weightx = 1;
		cons.gridwidth = 1;
		cons.gridheight = 2;
		JButton btnClose = new JButton(I18n.main("desktop.options.close"));
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
		JButton btnDefault = new JButton(I18n.main("desktop.options.default"));
		btnDefault.setAlignmentX(CENTER_ALIGNMENT);
		btnDefault.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				launchConfig.setDefaults();
				launchConfig.writeConfig();
				applyConfig();
			}
		});

		pnlButtons.add(btnDefault, cons);
		pnlButtons.add(btnClose, cons);
		pnlContent.add(pnlButtons);

		frmOptions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmOptions.setContentPane(pnlContent);
		frmOptions.pack();

		addWindowListener(this);

		launchConfig = c;
		applyConfig();
	}

	public void centerFrame(int parentWidth, int parentHeight) {
		setBounds(parentWidth / 2 - getWidth() / 2, parentHeight / 2 - getHeight() / 2, getWidth(), getHeight());
	}

	public void applyConfig() {
		spnFps.setValue(launchConfig.fps);
		spnWidth.setValue(launchConfig.width);
		spnHeight.setValue(launchConfig.height);
		cbFullscreen.setSelected(launchConfig.fullscreen);
		spnTextboxSpeed.setValue(launchConfig.textboxSpeed);
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
