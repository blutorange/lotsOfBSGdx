package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;

public interface ModelClip {
	public float getStartTime();

	public float getEndTime();

	public CharSequence getLabel();

	/**
	 * Precautions should be taken such that the start time will not be greater than the end time,
	 * and not smaller than 0.
	 * <br><br>
	 * Implementations must prevent the start time to be set to a value less than the
	 * start time of track they have been attached to (if any), or adjust the start time
	 * of the track.
	 * <br><br>
	 * {@link ClipChangeType#START_TIME} must be triggered when the start time has changed.
	 * <br><br>
	 * @param startTime New start time in seconds.
	 */
	public void setStartTime(float startTime);
	
	/**
	 * Precautions should be taken such that the end time will not be smaller than the start time.
	 * <br><br>
	 * <br><br>
	 * Implementations must prevent the end time to be set to a value greater than the
	 * end time of track they have been attached to (if any) , or adjust the end time
	 * of the track.
	 * <br><br> 
	 * {@link ClipChangeType#END_TIME} must be triggered when the end time has changed.
	 * <br><br>
	 * @param startTime New end time in seconds.
	 */
	public void setEndTime(float startTime);

	/**
	 * It is not possible for a clip not be attached to a timeline, as tracks can be created
	 * only by {@link ModelTrack#newClip()}, which attaches the track to that timeline
	 * automatically. Clips can be moved to another track with {@link ModelTimeline#moveClip}, but
	 * they cannot be detached from a track. Users should not keep references to clips when the
	 * track has already been deleted. 
	 * @return The track this clip is attached to. 
	 */
	public ModelTrack getParentTrack();

	/**
	 * @see ModelTrack#getParentTimeline()
	 */
	public ModelTimeline getParentTimeline();
	
	public ModelClipData getClipData();
}
