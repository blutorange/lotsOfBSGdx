package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.basic;

import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;

class BasicClip implements ModelClip {

	private float startTime;
	private float endTime;
	private final ModelClipData clipData;
	BasicTrack track;
	
	/**
	 * Creates a new track without the start and end time initialized, which must be done by the caller.
	 * @param track Track to which this clip belongs.
	 * @throws NullPointerException When track is null.
	 */
	BasicClip(BasicTrack track, ModelClipData clipData) throws NullPointerException {
		if (track == null) throw new NullPointerException("track cannot be null");
		if (clipData == null) throw new NullPointerException("clip data cannot be null");
		this.track = track;
		this.clipData = clipData;
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
	public CharSequence getLabel() {
		return toString();
	}
	
	@Override
	public String toString() {
		return "MockClip=" + hashCode();
	}
	
	@Override
	public void setStartTime(float startTime) {
		startTime = MathUtils.clamp(startTime, 0f, endTime);
		startTime = Math.max(track.getStartTime(), startTime);
		final boolean hasChanged = startTime != this.startTime;
		this.startTime = startTime;
		if (hasChanged) track.timeline.clipChanged(this, ClipChangeType.START_TIME);
	}
	
	@Override
	public void setEndTime(float endTime) {
		endTime = Math.max(startTime, endTime);
		endTime = Math.min(track.getEndTime(), endTime);
		final boolean hasChanged = endTime != this.endTime;
		this.endTime = endTime;
		if (hasChanged) track.timeline.clipChanged(this, ClipChangeType.END_TIME);
	}

	@Override
	public ModelTrack getParentTrack() {
		return track;
	}
	
	@Override
	public ModelTimeline getParentTimeline() {
		return track.getParentTimeline();
	}

	@Override
	public ModelClipData getClipData() {
		return clipData;
	}
}
