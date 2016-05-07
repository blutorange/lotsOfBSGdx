package de.homelab.madgaksha.desktop;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.IGameParameters;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.logging.LoggerFactory;

public class DesktopLauncher extends JFrame {
	
	/**
	 * Version 1.
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(DesktopLauncher.class);

	private static Locale locale = Locale.getDefault();
	private static int fps = 30;
	private static boolean fullscreen = false;
	private static int width = 640;
	private static int height = 480;
	
	private static DesktopLauncher instance = null;
	private static LwjglApplication lwjglApplication = null;
	private static Game game = null;
	
	/**
	 * Exits and shows a message with the error.
	 * @param level Severity.
	 * @param log Log message.
	 * @param msg GUI message.
	 */
	public static void exit(Level level, String log, String msg) {
		LOG.log(level,log);
		if (game != null) {
			game.pause();
			game.dispose();
		}
		if (instance != null) {
			instance.setDefaultCloseOperation(EXIT_ON_CLOSE);
			instance.setVisible(false);
			instance.dispose();
		}
		JOptionPane.showMessageDialog(null, msg);
		
	}
	
	/**
	 * Prints the help for the command line arguments to stdout.
	 */
	private static void showHelp() {
		System.out.println("The following command line options are available:");
		System.out.println("");
		System.out.println("-fs [--fullscreen]   Activates fullscreen mode.");
		System.out.println("-dw [--width]        Sets the width of the window.");
		System.out.println("-dh [--height]        Sets the height of the window.");
		System.out.println("-f  [--fps]          Sets the framerate in frames per second.");
		System.out.println("-h  [--help]         Shows this help.");
		System.out.println("-l  [--language]     Sets the language for the program.");
	}
	
	/**
	 * Adds all available command line options and parses the command line. 
	 * @param args Command line args.
	 * @return The parsed command line.
	 */
	private static CommandLine parseCommandLine(String[] args) {
		// Parse and process the given command line options.
		final Options o = new Options();
		final CommandLineParser parser = new DefaultParser();
		CommandLine line = null;

		o.addOption("l", "language", true, "Sets the language for the program.");
		o.addOption("h", "help", false, "Show help.");
		o.addOption("fs", "fullscreen", false, "Activates fullscreen mode.");
		o.addOption("f", "fps", false, "Framerate in frames per second.");
		o.addOption("dw", "width", true, "Window width.");
		o.addOption("dh", "height", true, "Window height");
		
		try {
			line = parser.parse(o, args, false);
		} catch (ParseException e) {
			LOG.log(Level.SEVERE, "Invalid command line options. Try --help", e);
			System.exit(-1);
		}
		
		return line;
	}
	
	
	/**
	 * Parses the command line options and initializes the main program. Type
	 * <code>--help</code> for a list of available options.
	 * 
	 * @param args
	 *            Command line options.
	 */
	public static void main(String[] args) {
		final CommandLine line = parseCommandLine(args);

		// Print help if asked to and exit.
		if (line.hasOption("help")) {
			showHelp();
			System.exit(0);
		}
				
		// Set the framerate
		fps = 30;
		if (line.hasOption("fps")) {
			final Scanner s = new Scanner(line.getOptionValue("fps"));
			if (s.hasNextInt(10)) fps = s.nextInt(10);
			s.close();
		}
		
		// Set the language.
		final Locale l;
		if (line.hasOption("language")) {
			l = new Locale(line.getOptionValue("language"));
		} else {
			l = Locale.getDefault();
		}
		locale = l;
		LOG.config("language set to " + l.getDisplayLanguage());
		
		// Set window dimensions.
		width = 640;
		height = 480;
		fullscreen = line.hasOption("fullscreen");
		if (line.hasOption("width")) {
			final String w = line.getOptionValue("width", "640");
			final Scanner s = new Scanner(w);
			if (s.hasNextInt(10)) width = s.nextInt(10);
			s.close();
		}
		if (line.hasOption("height")) {
			final String h = line.getOptionValue("height", "480");
			final Scanner s = new Scanner(h);
			if (s.hasNextInt(10)) height = s.nextInt(10);
			s.close();
		}
		
		// Try to enable hardware acceleration, if available
		try {
			System.setProperty("sun.java2d.opengl", "true");
		}
		catch (Exception e) {
			LOG.log(Level.INFO, "could not enable hardware acceleration", e);
		}
		
		instance = new DesktopLauncher();
	}

	
	private DesktopLauncher() {
		// Apply locale.
		this.setLocale(locale);
		JOptionPane.setDefaultLocale(locale);		
		
		// Load localization.
		i18n.init(locale);
		
		// Handle closing etc.
		setupWindowEvents();
		
		// Set our title.
		this.setTitle(i18n.main("desktop.launcher.title"));
		
		// Render the main launcher screen.
		renderLauncher();
	}

	private void setupWindowEvents() {
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//TODO
	// just a simple button for testing now.
	private void renderLauncher() {
		final JButton button = new JButton("start");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
				launchGame();
			}
		});
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(button);
		this.setSize(320, 240);
		this.setVisible(true);
	}
	
	// Now we actually get to the meat.
	public void launchGame() {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = fps;
		config.fullscreen = fullscreen;
		LOG.info(String.valueOf(fullscreen));
		config.width = width;
		config.height = height;
		config.title = i18n.main("desktop.game.title");
		config.resizable = true;
		config.x = -1;
		config.y = -1;
		config.forceExit = false;
		
		final IGameParameters params = new IGameParameters() {
			
			@Override
			public int getRequestedWidth() {
				return width;
			}
			
			@Override
			public Locale getRequestedLocale() {
				return locale;
			}
			
			@Override
			public int getRequestedHeight() {
				return height;
			}
			
			@Override
			public boolean getRequestedFullscreen() {
				return fullscreen;
			}
			
			@Override
			public int getRequestedFps() {
				return fps;
			}
		};
		
		LOG.fine("launching game");
		game = new Game(params);
		lwjglApplication = new LwjglApplication(game,config);
		lwjglApplication.addLifecycleListener(new LifecycleListener() {
			
			@Override
			public void resume() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void pause() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// Let the user choose another level.
				setEnabled(true);
				lwjglApplication = null;
				game = null;
			}
		});
	}

}
