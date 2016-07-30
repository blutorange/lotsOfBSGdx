package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface LifeSpanTeller {
	/** @return Start time. Must not be greater than the end time. */
	public float getStartTime();

	/** @return End time. Must not be smaller than the start time. */
	public float getEndTime();
	
	/** The duration, ie. the difference between the end time and start time. */
	default float getDuration() {
		return getEndTime()-getStartTime();
	}
}
