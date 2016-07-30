package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import java.util.Iterator;

import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeListenable;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;

public interface ModelTimeline extends Iterable<ModelTrack>, TimelineChangeListenable, LifeSpanTeller {

	/**
	 * Zoom factor in time-direction.
	 * @return Number of pixels for one second of the timeline.
	 */
	public float getPixelPerSecond();

	/**
	 * @return The current position on the timeline in seconds. Must be between the start and end time.
	 */
	public float getCurrentTime();
	default int getCurrentFrame() {
		return MathUtils.round(getCurrentTime() / getDeltaTime());
	}
	
	/**
	 * Precautions should be taken such that {@link #getStartTime()} time will not be greater than {@link #getEndTime()},
	 * and not smaller than 0.
	 * <br><br>
	 * {@link TimelineChangeType#START_TIME} must be triggered when the start time has changed.
	 * <br><br>
	 * {@link TimelineChangeType#SEEK} must be triggered when the current position has changed.
	 * <br><br>
	 * The start and end times of all tracks must be adjusted as well when necessary. Implementations are
	 * free to decide whether tracks of length zero should be deleted.
	 * @param startTime New start time in seconds.  
	 */
	public void setStartTime(float startTime);

	/**
	 * Precautions should be taken such that #{@link #getCurrentTime()} does not return a value
	 * smaller than the start time or greater than the end time.
	 * Sets the current time in seconds.
	 * {@link TimelineChangeType#SEEK} must be triggered only when the time has changed.
	 */
	public void setCurrentTime(float time);

	/**
	 * Precautions should be taken such that {@link #getEndTime()} will not be smaller than {@link #getStartTime()}.
	 * <br><br>
	 * {@link TimelineChangeType#START_TIME} must be triggered when the start time has changed.
	 * <br><br>
	 * {@link TimelineChangeType#SEEK} must be triggered when the current position has changed.
	 * <br><br>
	 * The start and end times of all tracks must be adjusted as well when necessary. Implementations are
	 * free to decide whether tracks of length zero should be deleted.
	 * @param startTime New end time in seconds.  
	 */
	public void setEndTime(float endTime);
	
	/**
	 * @return An iterator for the tracks this model contains.
	 */
	@Override
	public Iterator<ModelTrack> iterator();
	
	public int getTrackCount();

	/**
	 * @return The name of this timeline. Displayed to the top left of the timeline.
	 */
	public CharSequence getLabel();
	
	/**
	 * Creates a new, empty track and adds it to this timeline. This is only way
	 * to add tracks to a timeline.
	 * <br><br>
	 * {@link TrackChangeType#ATTACHED_TO_TIMELINE} and {@link TimelineChangeType#TRACK_ATTACHED}
	 * must be called, in this order.
	 * @param startTime The start time in seconds.
	 * @param endTime The end time in seconds.
	 * @param label The label of the newly created track. Can be null.
	 * @return The newly created track added to the timeline.
	 */
	public ModelTrack newTrack(float startTime, float endTime, String label);
	
	public void registerChangeListener(TrackChangeType type, TrackChangeListener listener);
	public void registerChangeListener(ClipChangeType type, ClipChangeListener listener);
	default void registerChangeListener(TrackChangeListener listener, TrackChangeType... typeList) {
		for (TrackChangeType type : typeList) registerChangeListener(type, listener);
	}
	default void registerChangeListener(ClipChangeListener listener, ClipChangeType... typeList) {
		for (ClipChangeType type : typeList) registerChangeListener(type, listener);
	}

	
	/**
	 * Must not be less or equal to 0. Can be used for frame stepping etc.
	 * @return Inverse of the frame rate of this timeline in seconds.
	 */
	public float getDeltaTime();
	
	/**
	 * Precautions should be taken such that {@link #getDeltaTime()} does return a value greater than 0.
	 * <br><br>
	 * {@link TimelineChangeType#DELTA_TIME} must be called when a new value has been set for deltaTime.
	 * @param deltaTime The new delta time in seconds.
	 */
	public void setDeltaTime(float deltaTime);
	
	/**
	 * Sets the track, clip, clip data etc. that has been selected currently and whose
	 * details will be shown on screen.
	 * @param detailsPanel The selected details panel. May be null to indicate no selection.
	 */
	public void setSelected(DetailsPanel detailsPanel);

	/** @return The currently selected details panel, or null when none is selected. */
	public DetailsPanel getSelected();
	default boolean hasSelected() {
		return getSelected() != null;
	}
	
}
