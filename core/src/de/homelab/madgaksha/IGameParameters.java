package de.homelab.madgaksha;

import java.util.Locale;

public interface IGameParameters {
	public Locale getRequestedLocale();
	public int getRequestedWidth();
	public int getRequestedHeight();
	public boolean getRequestedFullscreen();
	public int getRequestedFps();
	// getRequiredTextures()
	// getRequiredAdx()
	//...
}
