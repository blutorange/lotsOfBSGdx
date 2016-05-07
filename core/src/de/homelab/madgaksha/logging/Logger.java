package de.homelab.madgaksha.logging;

import java.util.Date;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Logger {
	
	private final Class<?> loggerClass;
	private final static String format = "[%s] - %s - %s";
	private Logger(Class<?> c) {
		loggerClass = c;
	}

	private void log(int level, String msg, Throwable error) {
		final String s;
		final String date = new Date().toString();
		switch (level) {
		case Application.LOG_INFO:
			s = String.format(format, "INFO", date, loggerClass.getCanonicalName());
			Gdx.app.log(s, msg, error);
			break;
		case Application.LOG_ERROR:
			s = String.format(format, "ERROR", date, loggerClass.getCanonicalName());
			Gdx.app.error(s, msg, error);
			break;		
		case Application.LOG_DEBUG:
			s = String.format(format, "DEBUG", date, loggerClass.getCanonicalName());
			Gdx.app.debug(s, msg, error);
			break;
		}
	}
	
	public void setLogLevel(int logLevel) {
		Gdx.app.setLogLevel(logLevel);
	}
	
	public void info(String msg, Throwable error) {
		log(Application.LOG_INFO, msg,error);
	}
	public void error(String msg, Throwable error) {
		log(Application.LOG_ERROR, msg,error);
	}
	public void debug(String msg, Throwable error) {
		log(Application.LOG_DEBUG, msg,error);
	}
	
	public void info(String msg) {
		info(msg,null);		
	}
	public void error(String msg) {
		error(msg,null);
	}
	public void debug(String msg) {
		debug(msg,null);
	}
	
	
	/**
	 * Get a logger for the provided class.
	 * @param c The class for which to log.
	 * @return The logger for the class.
	 */
	public static Logger getLogger(Class<?> c) {
		return new Logger(c);
	}
}
