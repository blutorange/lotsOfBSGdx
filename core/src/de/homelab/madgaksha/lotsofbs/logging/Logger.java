package de.homelab.madgaksha.lotsofbs.logging;

import java.io.PrintStream;
import java.util.Date;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Logger {

	private final Class<?> loggerClass;
	private final static String format = "[%s] - %s - %s";

	public final static int ALL = Application.LOG_DEBUG;
	public final static int DEBUG = Application.LOG_DEBUG;
	public final static int NONE = Application.LOG_NONE;
	public final static int INFO = Application.LOG_INFO;
	public final static int ERROR = Application.LOG_ERROR;
	
	private Logger(Class<?> c) {
		loggerClass = c;
	}

	private void log(int level, String msg, Throwable error) {
		final String s;
		final String date = new Date().toString();
		String loggerClassName = loggerClass.getCanonicalName();
		if (loggerClassName == null)
			loggerClassName = "???undefined.class???";
		switch (level) {
		case Application.LOG_INFO:
			s = String.format(format, "INFO", date, loggerClassName);
			if (Gdx.app == null)
				logViaSystemOut(s, msg, error);
			else if (error == null)
				Gdx.app.log(s, msg);
			else
				Gdx.app.log(s, msg, error);
			break;
		case Application.LOG_ERROR:
			s = String.format(format, "ERROR", date, loggerClassName);
			if (Gdx.app == null)
				logViaSystemErr(s, msg, error);
			else if (error == null)
				Gdx.app.error(s, msg);
			else
				Gdx.app.error(s, msg, error);
			break;
		case Application.LOG_DEBUG:
			s = String.format(format, "DEBUG", date, loggerClassName);
			if (Gdx.app == null)
				logViaSystemOut(s, msg, error);
			else if (error == null)
				Gdx.app.debug(s, msg);
			else
				Gdx.app.debug(s, msg, error);
			break;
		}
	}

	private void logViaSystem(PrintStream ps, String s, String msg, Throwable error) {
		ps.println(s + ": " + msg);
		if (error != null) {
			error.printStackTrace(ps);
		}
	}

	private void logViaSystemOut(String s, String msg, Throwable error) {
		logViaSystem(System.out, s, msg, error);
	}

	private void logViaSystemErr(String s, String msg, Throwable error) {
		logViaSystem(System.err, s, msg, error);
	}

	public static void setDefaultLevel(int logLevel) {
		Gdx.app.setLogLevel(logLevel);
	}

	public void info(String msg, Throwable error) {
		log(Application.LOG_INFO, msg, error);
	}

	public void error(String msg, Throwable error) {
		log(Application.LOG_ERROR, msg, error);
	}

	public void debug(String msg, Throwable error) {
		log(Application.LOG_DEBUG, msg, error);
	}

	public void info(String msg) {
		info(msg, null);
	}

	public void error(String msg) {
		error(msg, null);
	}

	public void debug(String msg) {
		debug(msg, null);
	}

	public void info(Object msg) {
		info(String.valueOf(msg), null);
	}

	public void error(Object msg) {
		error(String.valueOf(msg), null);
	}

	public void debug(Object msg) {
		debug(String.valueOf(msg), null);
	}

	/**
	 * Get a logger for the provided class.
	 * 
	 * @param c
	 *            The class for which to log.
	 * @return The logger for the class.
	 */
	public static Logger getLogger(Class<?> c) {
		return new Logger(c);
	}
}
