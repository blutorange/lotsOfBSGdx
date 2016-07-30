package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface ClipChangeListener {
	/**
	 * @param clip The clip that has changed.
	 * @param type The type of change. May be null.
	 */
	public void handle(ModelClip clip, ClipChangeType type);
	
	public static enum ClipChangeType {
		/** Issued when this clip was attached to a track. The track can be accessed via {@link ModelClip#getParentTimeline()}.*/
		ATTACHED_TO_TRACK,
		/** Issued when the start time of a clip has changed. Not issued when a new clip has been created. */
		START_TIME,
		/** Issued when the end time of a clip has changed. Not issued when a new clip has been created. */
		END_TIME
	}
	
	public static interface ClipChangeListenable extends ChangeListenable<ClipChangeType, ClipChangeListener> {
	}
}
