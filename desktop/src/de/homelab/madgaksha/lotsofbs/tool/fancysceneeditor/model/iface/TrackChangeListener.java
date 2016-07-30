package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface TrackChangeListener {
	/**
	 * @param track The track that has changed.
	 * @param type The type of change. May be null.
	 */
	public void handle(ModelTrack track, TrackChangeType type);
	public static enum TrackChangeType {
		/** Issued when the track's label has changed. */
		LABEL,
		/** Issued when this track has been attached to a timeline. */
		ATTACHED_TO_TIMELINE,
		/** Issued when a clip has been added to this track. */
		CLIP_ATTACHED,
		/** Issued when the start time has actually changed. */
		START_TIME,
		/** Issued when the end time has actually changed. */
		END_TIME,
	}
	public static interface TrackChangeListenable extends ChangeListenable<TrackChangeType, TrackChangeListener> {
	}
}
