package de.homelab.madgaksha.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerFactory {
	
	private final static String LOGGING_PROPERTIES = "./logging.properties";
	
	private LoggerFactory() {
	}
	
	/**
	 * Load and read the logging.properties.
	 */
	static {
		InputStream is = null;
		try {
			final URL url = LoggerFactory.class.getResource(LOGGING_PROPERTIES);
			if (url == null) throw new IOException();
			is = url.openStream();
			LogManager.getLogManager().readConfiguration(is);
		} catch (IOException e) {
			System.err.println("unable to read logging.properties");
			e.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	/**
	 * Get a logger for the provided class.
	 * @param c The class for which to log.
	 * @return The logger for the class.
	 */
	public static Logger getLogger(Class<?> c) {
		final Logger logger = Logger.getLogger(c.getCanonicalName());
		return logger;
	}
}
