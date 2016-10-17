package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.timeline;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DetailsPanel;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.ACustomDataHolder;

public class BasicTimeline extends ACustomDataHolder implements ModelTimeline {

	private float deltaTime = 0.0166667f;
	private float currentTime;
	private float startTime;
	private float endTime;


	private final List<ModelTrack> trackList = new ArrayList<ModelTrack>();
	private final EnumMap<TimelineChangeType, Set<TimelineChangeListener>> timelineChangeListener = new EnumMap<TimelineChangeType, Set<TimelineChangeListener>>(
			TimelineChangeType.class);
	private final EnumMap<TrackChangeType, Set<TrackChangeListener>> trackChangeListener = new EnumMap<TrackChangeType, Set<TrackChangeListener>>(
			TrackChangeType.class);
	private final EnumMap<ClipChangeType, Set<ClipChangeListener>> clipChangeListener = new EnumMap<ClipChangeType, Set<ClipChangeListener>>(
			ClipChangeType.class);
	private int trackCounter = 0;

	private DetailsPanel<?> selectedDetailsPanel;

	/** Creates a new timeline. */
	BasicTimeline() {
	}



	@Override
	public float getPixelPerSecond() {
		return 16;
	}

	@Override
	public float getStartTime() {
		return 0;
	}

	@Override
	public float getCurrentTime() {
		return currentTime;
	}

	@Override
	public float getEndTime() {
		return 60;
	}

	@Override
	public void setStartTime(float startTime) {
		startTime = MathUtils.clamp(startTime, 0f, endTime);
		final boolean hasChanged = startTime != this.startTime;
		this.startTime = startTime;
		setCurrentTime(currentTime);
		adjustTrackTime();
		if (hasChanged) timelineChanged(TimelineChangeType.START_TIME);
	}

	@Override
	public void setCurrentTime(float currentTime) {
		currentTime = MathUtils.clamp(currentTime, startTime, endTime);
		final boolean hasChanged = currentTime != this.currentTime;
		this.currentTime = currentTime;
		if (hasChanged) timelineChanged(TimelineChangeType.SEEK);
	}

	@Override
	public void setEndTime(float endTime) {
		endTime = Math.max(startTime, endTime);
		final boolean hasChanged = endTime != this.endTime;
		this.endTime = endTime;
		setCurrentTime(currentTime);
		adjustTrackTime();
		if (hasChanged) timelineChanged(TimelineChangeType.END_TIME);
	}

	@Override
	public float getDeltaTime() {
		return deltaTime;
	}

	@Override
	public DetailsPanel<?> getSelected() {
		return selectedDetailsPanel;
	}

	@Override
	public void setDeltaTime(float deltaTime) {
		deltaTime = Math.max(Float.MIN_NORMAL, deltaTime);
		final boolean hasChanged = deltaTime != this.deltaTime;
		this.deltaTime = deltaTime;
		if (hasChanged) timelineChanged(TimelineChangeType.DELTA_TIME);
	}

	@Override
	public void setSelected(final DetailsPanel<?> detailsPanel) {
		final boolean hasChanged = detailsPanel == null ? selectedDetailsPanel != null : !((Object)detailsPanel).equals(selectedDetailsPanel);
		selectedDetailsPanel = detailsPanel;
		if (hasChanged) timelineChanged(TimelineChangeType.SELECTED);
	}

	@Override
	public Iterator<ModelTrack> iterator() {
		return trackList.iterator();
	}

	@Override
	public int getTrackCount() {
		return trackList.size();
	}

	@Override
	public CharSequence getLabel() {
		return toString();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + getLabel() + "@" + getStartTime() + "-" + getEndTime();
	}

	@Override
	public void registerChangeListener(final TimelineChangeType type, final TimelineChangeListener listener) {
		if (listener == null) throw new NullPointerException("listener cannot be null");
		getTimelineChangeListenerFor(type).add(listener);
	}
	@Override
	public void registerChangeListener(final TrackChangeType type, final TrackChangeListener listener) {
		if (listener == null) throw new NullPointerException("listener cannot be null");
		getTrackChangeListenerFor(type).add(listener);
	}
	@Override
	public void registerChangeListener(final ClipChangeType type, final ClipChangeListener listener) {
		if (listener == null) throw new NullPointerException("listener cannot be null");
		getClipChangeListenerFor(type).add(listener);
	}

	@Override
	public void removeChangeListener(final TimelineChangeType type, final TimelineChangeListener listener) {
		getTimelineChangeListenerFor(type).remove(listener);
	}
	@Override
	public void removeChangeListener(final TrackChangeType type, final TrackChangeListener listener) {
		getTrackChangeListenerFor(type).remove(listener);
	}
	@Override
	public void removeChangeListener(final ClipChangeType type, final ClipChangeListener listener) {
		getClipChangeListenerFor(type).remove(listener);
	}

	@Override
	public ModelTrack newTrack(final float startTime, final float endTime, String label) {
		final ModelTrack track = new BasicTrack(this);
		trackList.add(track);
		track.setStartTime(startTime);
		track.setEndTime(endTime);
		if (label == null)
			label = "Track " + (++trackCounter );
		track.setLabel(label);
		trackChanged(track, TrackChangeType.ATTACHED_TO_TIMELINE);
		timelineChanged(TimelineChangeType.TRACK_ATTACHED);
		return track;
	}

	/** Informs all clip listeners for the given type about the change. */
	void clipChanged(final ModelClip clip, final ClipChangeType type) {
		for (final ClipChangeListener l : getClipChangeListenerFor(type))
			l.handle(clip, type);
	}
	/** Informs all track listeners for the given type about the change. */
	void trackChanged(final ModelTrack track, final TrackChangeType type) {
		for (final TrackChangeListener l : getTrackChangeListenerFor(type))
			l.handle(track, type);
	}
	/** Informs all timeline listeners for the given type about the change. */
	void timelineChanged(final TimelineChangeType type) {
		for (final TimelineChangeListener l : getTimelineChangeListenerFor(type))
			l.handle(this, type);
	}

	private Set<TimelineChangeListener> getTimelineChangeListenerFor(final TimelineChangeType type) {
		Set<TimelineChangeListener> set = timelineChangeListener.get(type);
		if (set == null) {
			set = new HashSet<TimelineChangeListener>();
			timelineChangeListener.put(type, set);
		}
		return set;
	}
	private Set<TrackChangeListener> getTrackChangeListenerFor(final TrackChangeType type) {
		Set<TrackChangeListener> set = trackChangeListener.get(type);
		if (set == null) {
			set = new HashSet<TrackChangeListener>();
			trackChangeListener.put(type, set);
		}
		return set;
	}
	private Set<ClipChangeListener> getClipChangeListenerFor(final ClipChangeType type) {
		Set<ClipChangeListener> set = clipChangeListener.get(type);
		if (set == null) {
			set = new HashSet<ClipChangeListener>();
			clipChangeListener.put(type, set);
		}
		return set;
	}

	/** Adjusts the start and end time of all tracks
	 * such that they are not smaller than the start time
	 * or greater than the end time of this timeline.
	 */
	private void adjustTrackTime() {
		for (final ModelTrack track : this) {
			if (track.getStartTime() < startTime)
				track.setStartTime(startTime);
			if (track.getEndTime() > endTime)
				track.setEndTime(startTime);
		}
	}

	/**
	 * @param duration Duration of the timeline in seconds.
	 * @return Returns a timeline starting a 0 and ending at duration. The current position is set to the start time.
	 */
	public static BasicTimeline newTimeline(final float duration) {
		final BasicTimeline timeline = new BasicTimeline();
		timeline.setStartTime(0f);
		timeline.setEndTime(duration);
		timeline.setCurrentTime(0f);
		return timeline;
	}

	@Override
	public TimelineChangeListener setStartTimeListener(final TimeIntervalListener listener) {
		final TimelineChangeListener listenerId = new TimelineChangeListener() {
			@Override
			public void handle(final ModelTimeline timeline, final TimelineChangeType type) {
				listener.handle(getStartTime(), BasicTimeline.this);
			}
		};
		registerChangeListener(TimelineChangeType.START_TIME, listenerId);
		return listenerId;
	}

	@Override
	public TimelineChangeListener setEndTimeListener(final TimeIntervalListener listener) {
		final TimelineChangeListener listenerId = new TimelineChangeListener() {
			@Override
			public void handle(final ModelTimeline timeline, final TimelineChangeType type) {
				listener.handle(getEndTime(), BasicTimeline.this);
			}
		};
		registerChangeListener(TimelineChangeType.END_TIME, listenerId);
		return listenerId;
	}

	@Override
	public void removeStartTimeListener(final Object listenerId) {
		if (!(listenerId instanceof TimelineChangeListener)) return;
		removeChangeListener(TimelineChangeType.START_TIME, (TimelineChangeListener)listenerId);
	}

	@Override
	public void removeEndTimeListener(final Object listenerId) {
		if (!(listenerId instanceof TimelineChangeListener)) return;
		removeChangeListener(TimelineChangeType.END_TIME, (TimelineChangeListener)listenerId);
	}

	@Override
	public float getMinTime() {
		return 0f;
	}

	@Override
	public float getMaxTime() {
		return Float.MAX_VALUE;
	}

}

