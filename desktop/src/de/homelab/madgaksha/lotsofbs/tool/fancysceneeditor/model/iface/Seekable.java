package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;

public interface Seekable {

	/**
	 * @return The current position on the timeline in seconds. Must be between the start and end time.
	 */
	float getCurrentTime();

	/**
	 * Precautions should be taken such that #{@link #getCurrentTime()} does not return a value
	 * smaller than the start time or greater than the end time.
	 * Sets the current time in seconds.
	 * {@link TimelineChangeType#SEEK} must be triggered only when the time has changed.
	 */
	void setCurrentTime(float time);
}