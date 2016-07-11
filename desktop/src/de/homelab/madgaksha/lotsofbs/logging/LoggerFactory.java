package de.homelab.madgaksha.lotsofbs.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.badlogic.gdx.utils.StreamUtils;

public class LoggerFactory {

	private final static String LOGGING_PROPERTIES = "logging.properties";

	private LoggerFactory() {
	}

	/**
	 * Load and read the logging.properties.
	 */
	static {
		InputStream is = null;
		try {
			is = LoggerFactory.class.getResourceAsStream(LOGGING_PROPERTIES);
			if (is == null)
				throw new IOException("could not acquire resource as stream");
			LogManager.getLogManager().readConfiguration(is);
		} catch (IOException e) {
			System.err.println("unable to read logging.properties");
			e.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			if (is != null)
				StreamUtils.closeQuietly(is);
		}
	}

	/**
	 * Get a logger for the provided class.
	 * 
	 * @param c
	 *            The class for which to log.
	 * @return The logger for the class.
	 */
	public static Logger getLogger(Class<?> c) {
		final Logger logger = Logger.getLogger(c.getCanonicalName());
		return logger;
	}
}
