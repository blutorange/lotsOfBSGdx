package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;

public interface ModelTrack extends Iterable<ModelClip>, LifeSpanTeller {

	/**
	 * @return The name of this track. This is displayed to the left of the timeline.
	 */
	public String getLabel();

	/**
	 * Creates a new empty clip and adds it to this track.
	 * This is the only way to add clips. 
	 * <br><br>
	 * {@link ClipChangeType#ATTACHED_TO_TRACK}, {@link TrackChangeType#CLIP_ATTACHED},
	 * and {@link TimelineChangeType#CLIP_ATTACHED} must be called, in this order.
	 * @param startTime The start time in seconds.
	 * @param endTime The end time in seconds.
	 * @param clipData Clip data that cannot be changed later.
	 * @return The newly created clip.
	 */
	public ModelClip newClip(float startTime, float endTime, ModelClipData clipData);

	public void sortByStartTime(boolean ascending);
	public void sortByEndTime(boolean ascending);
	
	/**
	 * Precautions should be taken such that the start time will not be greater than the end time,
	 * and not smaller than 0.
	 * <br><br>
	 * Implementations must prevent the start time to be set to a value less than the
	 * start time of timeline they have been attached to (if any), or adjust the start time
	 * of the timeline.
	 * <br><br>
	 * {@link TrackChangeType#START_TIME} must be triggered when the start time has changed.
	 * <br><br>
	 * The start and end times of all clips must be adjusted as well when necessary. Implementations are
	 * free to decide whether clips of length zero should be deleted.
	 * @param startTime New start time in seconds.
	 */
	public void setStartTime(float startTime);
	
	/**
	 * Precautions should be taken such that the end time will not be smaller than the start time.
	 * <br><br>
	 * Implementations must prevent the end time to be set to a value greater than the
	 * end time of timeline they have been attached to (if any), or adjust the end time
	 * of the timeline.
	 * <br><br> 
	 * {@link TrackChangeType#END_TIME} must be triggered when the end time has changed.
	 * <br><br>
	 * The start and end times of all clips must be adjusted as well when necessary. Implementations are
	 * free to decide whether clips of length zero should be deleted.
	 * @param startTime New end time in seconds.
	 */
	public void setEndTime(float startTime);
	
	/**
	 * It is not possible for a track not be attached to a timeline, as tracks can be created
	 * only by {@link ModelTimeline#newTrack()}, which attaches the track to that timeline
	 * automatically. Users should not keep references to track when the timeline has already
	 * been deleted.
	 * @return The timeline this track is attached to.
	 */
	public ModelTimeline getParentTimeline();	
	
	/**
	 * Changes the label of the track, must not be null.
	 * {@link TrackChangeType#LABEL} must be triggered when it has changed.
	 * @param label The new track label.
	 */
	public void setLabel(String label);
}
