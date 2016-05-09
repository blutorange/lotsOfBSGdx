/*

http://stackoverflow.com/questions/31052775/adding-multiple-windows-in-libgdx
http://badlogicgames.com/forum/viewtopic.php?f=11&t=7635

Basically, you can run each window in a separate process (use the answer here to see how to 
implement JavaProcess which is used below):

public class Tiles {
   public static void main(String[] args) {
      LwjglApplicationConfiguration configForTiles = new LwjglApplicationConfiguration();
      TilePresets tilesWindow = new TilePresets();
      LwjglApplication tiles = new LwjglApplication(tilesWindow, configForTiles);
   }
}

Wrapper.java is the main entry point. It's where launching both windows occurs:

public class Wrapper {
   public static void main(String[] args) {
      // Launch mapWindow regularly 
      LwjglApplicationConfiguration configForMap = new LwjglApplicationConfiguration();
      MapMaker mapWindow = new MapMaker();
      LwjglApplication map = new LwjglApplication(mapWindow, configForMap);

      try {
         int res = JavaProcess.exec(Tiles.class); // Where the second window is shown
      } catch (IOException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}

http://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process/723914#723914

This is a synthesis of some of the other answers that have been provided. The Java system properties provide 
enough information to come up with the path to the java command and the classpath in what, I think, is a 
platform independent way.


public final class JavaProcess {

    private JavaProcess() {}        

    public static int exec(Class klass) throws IOException,
                                               InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = klass.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className);

        Process process = builder.start();
        process.waitFor();
        return process.exitValue();
    }

}

You would run this method like so:

int status = JavaProcess.exec(MyClass.class);


 */


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
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.badlogic.gdx.Application;

import de.homelab.madgaksha.GameParameters;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.level.FooLevel;
import de.homelab.madgaksha.logging.LoggerFactory;

public class DesktopLauncher extends JFrame {
	
	/**
	 * Version 1.
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = LoggerFactory.getLogger(DesktopLauncher.class);

	private Locale locale = Locale.getDefault();
	private int fps = 30;
	private boolean fullscreen = false;
	private int width = 640;
	private int height = 480;
	private int verbosity = Application.LOG_ERROR;
	private Process gameProcess = null;
	private static DesktopLauncher instance = null;
	
	/**
	 * Exits and shows a message with the error.
	 * @param level Severity.
	 * @param log Log message.
	 * @param msg GUI message.
	 */
	public static void exit(Level level, String log, String msg) {
		killProcesses();
		if (instance != null) {
			instance.setDefaultCloseOperation(EXIT_ON_CLOSE);
			instance.setVisible(false);
			instance.dispose();
		}
		alert(msg);
	}
	
	public static void killProcesses() {
		//TODO	
	}
	
	/**
	 * Prints the help for the command line arguments to stdout.
	 */
	private static void showHelp() {
		System.out.println("The following command line options are available:");
		System.out.println("");
		System.out.println("-dw [--width]        Sets the width of the window.");
		System.out.println("-dh [--height]       Sets the height of the window.");
		System.out.println("-f  [--fps]          Sets the framerate in frames per second.");
		System.out.println("-fs [--fullscreen]   Activates fullscreen mode.");
		System.out.println("-h  [--help]         Shows this help.");
		System.out.println("-l  [--language]     Sets the language for the program.");
		System.out.println("-v  [--verbosity]    Sets the logging level. 0=none, 1=info, 2=error, 3=debug.");
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

		o.addOption("v", "verbosity", true, "Sets the logging level 0-3.");
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
		int fps = 30;
		if (line.hasOption("fps")) {
			final Scanner s = new Scanner(line.getOptionValue("fps"));
			if (s.hasNextInt(10)) fps = s.nextInt(10);
			s.close();
		}
		
		// Set the language.
		Locale l;
		if (line.hasOption("language")) {
			l = new Locale(line.getOptionValue("language"));
		} else {
			l = Locale.getDefault();
		}
		Locale locale = l;
		LOG.config("language set to " + l.getDisplayLanguage());
		
		// Set window dimensions.
		int width = 640;
		int height = 480;
		boolean fullscreen = line.hasOption("fullscreen");
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
		
		// Set logging level.
		int verbosity = Application.LOG_ERROR;
		if (line.hasOption("verbosity")) {
			final String v = line.getOptionValue("verbosity", String.valueOf(verbosity));
			final Scanner s = new Scanner(v);
			if (s.hasNextInt(10)) verbosity = s.nextInt(10);
			s.close();
		}
		switch (verbosity) {
		case 0:
			verbosity = Application.LOG_NONE;
		case 1:
			verbosity = Application.LOG_INFO;
		case 2:
			verbosity = Application.LOG_ERROR;
		case 3:
			verbosity = Application.LOG_DEBUG;
			break;
		default:
			System.out.println("Unknown logging level " + verbosity + ". Try --help.");
			System.exit(-1);
		}
		
		// Try to enable hardware acceleration, if available
		try {
			System.setProperty("sun.java2d.opengl", "true");
		}
		catch (Exception e) {
			LOG.log(Level.INFO, "could not enable hardware acceleration", e);
		}
		
		DesktopLauncher.instance = new DesktopLauncher(fullscreen, width, height, fps, locale, verbosity);
	}

	
	private DesktopLauncher(boolean fullscreen, int width, int height, int fps, Locale locale, int verbosity) {
		// Save configuration.
		this.fullscreen = fullscreen;
		this.width = width;
		this.height = height;
		this.fps = fps;
		this.locale = locale;
		this.verbosity = verbosity;
		
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
				if (gameProcess != null) {
					DesktopLauncher.alert(i18n.main("desktop.running.no.close"));
					instance.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
				else {
					killProcesses();
					instance.setDefaultCloseOperation(EXIT_ON_CLOSE);
				}
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
				//TODO check if multiple games are running
				if (gameProcess == null) {
					disableUI();
					if (!launchGame()) {
						DesktopLauncher.alert(i18n.main("desktop.launch.failed"));
					}
				}
				else {
					LOG.info("cannot launch another game, still one active");
					DesktopLauncher.alert(i18n.main("desktop.running.no.start"));
				}
			}
		});
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(button);
		this.setSize(320, 240);
		this.setVisible(true);
	}
	
	// Now we actually get to the meat.
	public boolean launchGame() {	
		//TODO Add actual level.
		final GameParameters params = new GameParameters.Builder(new FooLevel())
				.requestedWidth(width)
				.requestedLocale(locale)
				.requestedHeight(height)
				.requestedFullscreen(fullscreen)
				.requestedFps(fps)
				.requestedLogLevel(verbosity)
				.requestedWindowTitle(i18n.main("desktop.game.title"))
				.build();
		// Serialize as string, pass to launcher, and run.
		try {
			gameProcess = JavaProcess.exec(ProcessLauncher.class, params.toByteArray());
			if (gameProcess != null) {
				// Launch new thread to notify us when the
				// game thread finishes. UI should stay
				// responsive.
				new Thread(new Runnable() {						
					@Override
					public void run() {
						try {
							gameProcess.waitFor();
							final int exitValue = gameProcess.exitValue();
							if (exitValue != 0) {
								LOG.warning("game exit value is " + String.valueOf(exitValue));
								DesktopLauncher.alert(i18n.main("desktop.launch.failed"));
							}
						} catch (InterruptedException e) {
							LOG.log(Level.SEVERE, "game process was interrupted", e);
						}
						finally {
							enableUI();
						}
					}
				}).start();
				return true;
			}
			else {
				enableUI();
				return false;
			}
		}
		catch (Exception e) {
			LOG.log(Level.SEVERE, "unable to launch game", e);
			enableUI();
			return false;
		}
	}
	
	public void enableUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameProcess = null;
				setEnabled(true);		
			}
		});
	}
	public void disableUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//TODO when disabling UI				
			}
		});
	}
	
	public static void alert(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
}
