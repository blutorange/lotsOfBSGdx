package de.homelab.madgaksha;

import java.util.Locale;

import de.homelab.madgaksha.Level.ALevel;

public interface IGameParameters {
	public Locale getRequestedLocale();
	public int getRequestedWidth();
	public int getRequestedHeight();
	public boolean getRequestedFullscreen();
	public int getRequestedFps();
	public int getRequestedLogLevel();
	public ALevel getLevel();
}
