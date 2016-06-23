package de.homelab.madgaksha.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import de.homelab.madgaksha.logging.LoggerFactory;

public class LaunchConfig {
	private final static Logger LOG = LoggerFactory.getLogger(LaunchConfig.class);
	public Integer fps;
	public Integer width, height;
	public Boolean fullscreen;
	public Float textboxSpeed;
	private final static String configFile = "config.properties";
	private final static int DEFAULT_WIDTH = 720;
	private final static int DEFAULT_HEIGHT = 480;
	private final static int DEFAULT_FPS = 30;
	private final static float DEFAULT_TEXTBOX_SPEED = 15.0f;
	private final static boolean DEFAULT_FULLSCREEN = false;

	public LaunchConfig(Integer w, Integer h, Integer f, Boolean fs, Float ts) {
		Properties props = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(new File(configFile));
			props.load(is);
		} catch (Exception e) { // keep default configuration
		} finally {
			if (is != null)
				IOUtils.closeQuietly(is);
		}

		// Read from config file and merge with cli params.
		textboxSpeed = ts;
		fullscreen = fs;
		fps = f;
		width = w;
		height = h;
		if (fps == null)
			fps = parseInt(props.getProperty("fps"), DEFAULT_FPS);
		if (width == null)
			width = parseInt(props.getProperty("width"), DEFAULT_WIDTH);
		if (height == null)
			height = parseInt(props.getProperty("height"), DEFAULT_HEIGHT);
		if (fullscreen == null)
			fullscreen = parseBoolean(props.getProperty("fullscreen"), DEFAULT_FULLSCREEN);
		if (textboxSpeed == null)
			textboxSpeed = parseFloat(props.getProperty("textboxSpeed"), DEFAULT_TEXTBOX_SPEED);
		writeConfig();
	}

	private Integer parseInt(String number, Integer defaultNumber) {
		if (number == null)
			return defaultNumber;
		final Scanner s = new Scanner(number);
		Integer x;
		if (s.hasNextInt(10)) {
			x = s.nextInt(10);
		} else
			x = defaultNumber;
		s.close();
		return x;
	}

	private Float parseFloat(String number, Float defaultNumber) {
		if (number == null)
			return defaultNumber;
		final Scanner s = new Scanner(number);
		Float x;
		if (s.hasNextFloat()) {
			x = s.nextFloat();
		} else
			x = defaultNumber;
		s.close();
		return x;
	}

	private Boolean parseBoolean(String bool, Boolean defaultBoolean) {
		if (bool == null)
			return defaultBoolean;
		final Scanner s = new Scanner(bool);
		Boolean b;
		if (s.hasNextBoolean()) {
			b = s.nextBoolean();
		} else
			b = defaultBoolean;
		s.close();
		return b;
	}

	public void writeConfig() {
		Properties props = new Properties();
		props.setProperty("width", String.valueOf(width));
		props.setProperty("height", String.valueOf(height));
		props.setProperty("fps", String.valueOf(fps));
		props.setProperty("fullscreen", String.valueOf(fullscreen));
		props.setProperty("textboxSpeed", String.valueOf(textboxSpeed));
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(configFile));
			LOG.log(Level.INFO, "writing configuration to " + new File(configFile).getAbsolutePath());
			props.store(os, "main configuration");
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "could not write configuration file", e);
		} finally {
			if (os != null)
				IOUtils.closeQuietly(os);
		}
	}

	public void setDefaults() {
		fps = DEFAULT_FPS;
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		fullscreen = DEFAULT_FULLSCREEN;
		textboxSpeed = DEFAULT_TEXTBOX_SPEED;
	}
}
