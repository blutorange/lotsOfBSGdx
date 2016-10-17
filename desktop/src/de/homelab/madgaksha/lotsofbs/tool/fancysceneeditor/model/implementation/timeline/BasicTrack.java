package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.timeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DetailsPanel;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.Seekable;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;

class BasicTrack implements ModelTrack, DetailsPanel<ModelTrack> {

	private float startTime;
	private float endTime;
	private String label = StringUtils.EMPTY;

	private final EnumMap<TrackChangeType, Set<TrackChangeListener>> trackChangeListener = new EnumMap<>(TrackChangeType.class);

	BasicTimeline timeline;

	private final List<ModelClip> clipList = new ArrayList<ModelClip>();


	/**
	 * Creates a new track without the start and end time initialized, which must be done by the caller.
	 * @param timeline Timeline to which this track belongs.
	 * @throws NullPointerException When timeline is null.
	 */
	BasicTrack(final BasicTimeline timeline) throws NullPointerException {
		if (timeline == null) throw new NullPointerException("track cannot be null");
		this.timeline = timeline;
	}

	@Override
	public void setLabel(final String label) {
		if (label == null) return;
		final boolean hasChanged = !this.label.equals(label);
		this.label = label;
		if (hasChanged) trackChanged(TrackChangeType.LABEL);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Iterator<ModelClip> iterator() {
		return clipList.iterator();
	}

	/**
	 * @throws NoSuchMethodException When the clip data could not be instantiated, mostly likely due to a missing constructor.
	 */
	@Override
	public ModelClip newClip(final float startTime, final float endTime, final Class<? extends ModelClipData> clipDataClazz) throws NoSuchMethodException {
		final BasicClip clip = new BasicClip(this, clipDataClazz);
		clipList.add(clip);
		clip.setStartTime(startTime);
		clip.setEndTime(endTime);
		timeline.clipChanged(clip, ClipChangeType.ATTACHED_TO_TRACK);
		trackChanged(TrackChangeType.CLIP_ATTACHED);
		timeline.timelineChanged(TimelineChangeType.CLIP_ATTACHED);
		return clip;
	}

	@Override
	public void sortByStartTime(final boolean ascending) {
		if (ascending)
			Collections.sort(clipList, ComparatorSet.clipStartTime);
		else
			Collections.sort(clipList, Collections.reverseOrder(ComparatorSet.clipStartTime));
	}

	@Override
	public void sortByEndTime(final boolean ascending) {
		if (ascending)
			Collections.sort(clipList, ComparatorSet.clipEndTime);
		else
			Collections.sort(clipList, Collections.reverseOrder(ComparatorSet.clipEndTime));
	}


	@Override
	public float getStartTime() {
		return startTime;
	}

	@Override
	public float getEndTime() {
		return endTime;
	}

	@Override
	public void setStartTime(float startTime) {
		startTime = MathUtils.clamp(startTime, 0f, endTime);
		startTime = Math.max(timeline.getStartTime(), startTime);
		final boolean hasChanged = startTime != this.startTime;
		this.startTime = startTime;
		adjustClipTime();
		if (hasChanged) trackChanged(TrackChangeType.START_TIME);

	}

	@Override
	public void setEndTime(float endTime) {
		endTime = Math.max(startTime, endTime);
		endTime = Math.min(timeline.getEndTime(), endTime);
		final boolean hasChanged = endTime != this.endTime;
		this.endTime = endTime;
		adjustClipTime();
		if (hasChanged) trackChanged(TrackChangeType.END_TIME);
	}

	@Override
	public Seekable getParentTimeline() {
		return timeline;
	}

	/** Adjusts the start and end time of all clips
	 * such that they are not smaller than the start time
	 * or greater than the end time of this track.
	 */
	private void adjustClipTime() {
		for (final ModelClip clip : this) {
			if (clip.getStartTime() < startTime)
				clip.setStartTime(startTime);
			if (clip.getEndTime() > endTime)
				clip.setEndTime(startTime);
		}
	}

	private static class ComparatorSet {
		private static Comparator<ModelClip> clipStartTime = new Comparator<ModelClip>() {
			@Override
			public int compare(final ModelClip c1, final ModelClip c2) {
				return c1.getStartTime() < c2.getStartTime() ? -1 : c1.getStartTime() == c2.getStartTime() ? 0 : 1;
			}
		};
		private static Comparator<ModelClip> clipEndTime = new Comparator<ModelClip>() {
			@Override
			public int compare(final ModelClip c1, final ModelClip c2) {
				return c1.getEndTime() < c2.getEndTime() ? -1 : c1.getEndTime() == c2.getEndTime() ? 0 : 1;
			}
		};
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + getLabel() + "@" + startTime + "-" + endTime;
	}

	@Override
	public Actor getActor(final TimelineProvider timelineProvider, final Skin skin) {
		return new Label(toString(), skin);
	}

	@Override
	public String getDescription() {
		return "One of many tracks of a timeline. It may contain multiple clips representing certain events.";
	}

	@Override
	public String getTitle() {
		return "Timeline Track";
	}

	@Override
	public ModelTrack getObject() {
		return this;
	}

	@Override
	public TrackChangeListener setStartTimeListener(final TimeIntervalListener listener) {
		final TrackChangeListener listenerId = new TrackChangeListener() {
			@Override
			public void handle(final ModelTrack timeline, final TrackChangeType type) {
				listener.handle(getStartTime(), BasicTrack.this);
			}
		};
		registerChangeListener(TrackChangeType.START_TIME, listenerId);
		return listenerId;
	}

	@Override
	public TrackChangeListener setEndTimeListener(final TimeIntervalListener listener) {
		final TrackChangeListener listenerId = new TrackChangeListener() {
			@Override
			public void handle(final ModelTrack timeline, final TrackChangeType type) {
				listener.handle(getEndTime(), BasicTrack.this);
			}
		};
		registerChangeListener(TrackChangeType.END_TIME, listenerId);
		return listenerId;
	}

	@Override
	public void removeStartTimeListener(final Object listenerId) {
		if (!(listenerId instanceof TrackChangeListener)) return;
		removeChangeListener(TrackChangeType.START_TIME, (TrackChangeListener)listenerId);
	}

	@Override
	public void removeEndTimeListener(final Object listenerId) {
		if (!(listenerId instanceof TrackChangeListener)) return;
		removeChangeListener(TrackChangeType.END_TIME, (TrackChangeListener)listenerId);
	}

	@Override
	public void registerChangeListener(final TrackChangeType type, final TrackChangeListener listener) {
		if (listener == null) throw new NullPointerException("listener cannot be null");
		getTrackChangeListenerFor(type).add(listener);
	}

	@Override
	public void removeChangeListener(final TrackChangeType type, final TrackChangeListener listener) {
		getTrackChangeListenerFor(type).remove(listener);
	}

	private Set<TrackChangeListener> getTrackChangeListenerFor(final TrackChangeType type) {
		Set<TrackChangeListener> set = trackChangeListener.get(type);
		if (set == null) {
			set = new HashSet<TrackChangeListener>();
			trackChangeListener.put(type, set);
		}
		return set;
	}

	private void trackChanged(final TrackChangeType type) {
		for (final TrackChangeListener l : getTrackChangeListenerFor(type))
			l.handle(this, type);
		timeline.trackChanged(this, type);
	}

	@Override
	public float getMinTime() {
		return timeline.getStartTime();
	}

	@Override
	public float getMaxTime() {
		return timeline.getEndTime();
	}
}
