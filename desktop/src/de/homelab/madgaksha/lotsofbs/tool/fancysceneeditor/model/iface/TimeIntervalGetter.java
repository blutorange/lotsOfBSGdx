package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface TimeIntervalGetter {
	/** @return Start time. Must not be greater than the end time. */
	public float getStartTime();

	/** @return End time. Must not be smaller than the start time. */
	public float getEndTime();

	/** @return The minimum value that should be passed to {@link #setStartTime(float)} */
	public float getMinTime();
	/** @return The maximum value that should be passed to {@link #setEndTime(float)} */
	public float getMaxTime();

	/** The duration, ie. the difference between the end time and start time. */
	default float getDuration() {
		return getEndTime()-getStartTime();
	}

	default float getMinDuration() {
		return 0f;
	}
	default float getMaxDuration() {
		return getMaxTime() - getStartTime();
	}
	default float getMinStartTime() {
		return getMinTime();
	}
	default float getMaxStartTime() {
		return Math.min(getMaxTime(), getEndTime());
	}
	default float getMinEndTime() {
		return Math.max(getMinTime(), getStartTime());
	}
	default float getMaxEndTime() {
		return getMaxTime();
	}

}
