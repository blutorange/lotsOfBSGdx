package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineChangeListener.TimelineChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TrackChangeListener.TrackChangeType;

class BasicTrack implements ModelTrack {

	private float startTime;
	private float endTime;
	private String label = StringUtils.EMPTY;
	BasicTimeline timeline;

	private final List<ModelClip> clipList = new ArrayList<ModelClip>();


	/**
	 * Creates a new track without the start and end time initialized, which must be done by the caller.
	 * @param timeline Timeline to which this track belongs.
	 * @throws NullPointerException When timeline is null.
	 */
	BasicTrack(BasicTimeline timeline) throws NullPointerException {
		if (timeline == null) throw new NullPointerException("track cannot be null");
		this.timeline = timeline;
	}
	
	@Override
	public void setLabel(String label) {
		if (label == null) return;
		final boolean hasChanged = !this.label.equals(label);
		this.label = label;
		if (hasChanged) timeline.trackChanged(this, TrackChangeType.LABEL);;
	}
	
	@Override
	public CharSequence getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return "BasicTrack@" + hashCode();
	}

	@Override
	public Iterator<ModelClip> iterator() {
		return clipList.iterator();
	}
	
	@Override
	public ModelClip newClip(float startTime, float endTime, ModelClipData clipData) {
		final BasicClip clip = new BasicClip(this, clipData);
		clipList.add(clip);
		clip.setStartTime(startTime);
		clip.setEndTime(endTime);
		timeline.clipChanged(clip, ClipChangeType.ATTACHED_TO_TRACK);
		timeline.trackChanged(this, TrackChangeType.CLIP_ATTACHED);
		timeline.timelineChanged(TimelineChangeType.CLIP_ATTACHED);		
		return clip;
	}

	@Override
	public void sortByStartTime(boolean ascending) {
		if (ascending)
			Collections.sort(clipList, ComparatorSet.clipStartTime);
		else
			Collections.sort(clipList, Collections.reverseOrder(ComparatorSet.clipStartTime));
	}
	
	@Override
	public void sortByEndTime(boolean ascending) {
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
		if (hasChanged) timeline.trackChanged(this, TrackChangeType.START_TIME);
		
	}

	@Override
	public void setEndTime(float endTime) {
		endTime = Math.max(startTime, endTime);
		endTime = Math.min(timeline.getEndTime(), endTime);
		final boolean hasChanged = endTime != this.endTime;
		this.endTime = endTime;
		adjustClipTime();
		if (hasChanged) timeline.trackChanged(this, TrackChangeType.END_TIME);
	}

	@Override
	public ModelTimeline getParentTimeline() {
		return timeline;
	}
	
	/** Adjusts the start and end time of all clips
	 * such that they are not smaller than the start time
	 * or greater than the end time of this track.
	 */
	private void adjustClipTime() {
		for (ModelClip clip : this) {
			if (clip.getStartTime() < startTime) {
				clip.setStartTime(startTime);
			}
			if (clip.getEndTime() > endTime) {
				clip.setEndTime(startTime);				
			}
		}
	}
	
	private static class ComparatorSet {
		private static Comparator<ModelClip> clipStartTime = new Comparator<ModelClip>() {
			@Override
			public int compare(ModelClip c1, ModelClip c2) {
				return c1.getStartTime() < c2.getStartTime() ? -1 : c1.getStartTime() == c2.getStartTime() ? 0 : 1;
			}
		};
		private static Comparator<ModelClip> clipEndTime = new Comparator<ModelClip>() {
			@Override
			public int compare(ModelClip c1, ModelClip c2) {
				return c1.getEndTime() < c2.getEndTime() ? -1 : c1.getEndTime() == c2.getEndTime() ? 0 : 1;
			}
		};
	}
}
