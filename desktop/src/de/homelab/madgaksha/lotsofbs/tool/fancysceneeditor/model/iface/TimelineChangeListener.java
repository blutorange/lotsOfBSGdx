package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface TimelineChangeListener {
	/**
	 * @param track The track that has changed.
	 * @param type The type of change. May be null.
	 */
	public void handle(ModelTimeline timeline, TimelineChangeType type);
	public static enum TimelineChangeType {
		/** Issued when the currently selected details panel has changed. */
		SELECTED,
		/** Issued when {@link ModelTimeline#getDeltaTime()} has changed.*/
		DELTA_TIME,
		/** Issued when a clip has been attached to a track of this timeline. */
		CLIP_ATTACHED,
		/** Issued when a track has been attached to this timeline. */
		TRACK_ATTACHED,
		/** Issued when {@link ModelTimeline#getStartTime()} has changed. */
		START_TIME,
		/** Issued when {@link ModelTimeline#getCurrentTime()} has changed. */
		SEEK,
		/** Issued when {@link ModelTimeline#getEndTime()} has changed. */
		END_TIME,
		;
	}
}
