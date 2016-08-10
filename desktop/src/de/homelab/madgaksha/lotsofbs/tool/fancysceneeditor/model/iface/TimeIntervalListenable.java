package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface TimeIntervalListenable extends TimeInterval {
	/**
	 * Sets the start time listener. There may be many listeners.
	 * @param listener The start time listener.
	 * @return An ID that can be passed to {@link #removeStartTimeListener(Object)} to remove the listener set by this call to setStartTimeListener.
	 */
	public Object setStartTimeListener(TimeIntervalListener listener);
	/**
	 * Sets the end time listener. . There may be many listeners.
	 * @param listener The end time listener.
	 * @return An ID that can be passed to {@link #removeEndTimeListener(Object)} to remove the listener set by this call to setEndTimeListener.
	 */
	public Object setEndTimeListener(TimeIntervalListener listener);

	/**
	 * Removes the specified start time listener.
	 * @param listenerId The ID as returned by {@link #setStartTimeListener(TimeIntervalListener)}.
	 */
	public void removeStartTimeListener(Object listenerId);
	/**
	 * Removes the specified end time listener.
	 * @param listenerId The ID as returned by {@link #setEndTimeListener(TimeIntervalListener)}.
	 */
	public void removeEndTimeListener(Object listenerId);

	public static interface TimeIntervalListener {
		public void handle(float startTime, TimeInterval interval);
	}
}