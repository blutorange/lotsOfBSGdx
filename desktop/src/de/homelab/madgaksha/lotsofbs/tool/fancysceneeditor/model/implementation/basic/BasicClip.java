package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.basic;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ClipChangeListener.ClipChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClip;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTimeline;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;

class BasicClip implements ModelClip {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BasicClip.class);
	
	private float startTime;
	private float endTime;
	private final ModelClipData clipData;
	private final EnumMap<ClipChangeType, Set<ClipChangeListener>> clipChangeListener = new EnumMap<ClipChangeType, Set<ClipChangeListener>>(ClipChangeType.class);
	private final Map<String,Object> dataMap = new HashMap<>(); 
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
	public void setStartTime(float startTime) {
		startTime = MathUtils.clamp(startTime, 0f, endTime);
		startTime = Math.max(track.getStartTime(), startTime);
		final boolean hasChanged = startTime != this.startTime;
		this.startTime = startTime;
		if (hasChanged) clipChanged(ClipChangeType.START_TIME);
	}
	
	@Override
	public void setEndTime(float endTime) {
		endTime = Math.max(startTime, endTime);
		endTime = Math.min(track.getEndTime(), endTime);
		final boolean hasChanged = endTime != this.endTime;
		this.endTime = endTime;
		if (hasChanged) clipChanged(ClipChangeType.END_TIME);
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

	@Override
	public void registerChangeListener(ClipChangeType type, ClipChangeListener listener) {
		if (listener == null) throw new NullPointerException("listener cannot be null"); 
		getClipChangeListenerFor(type).add(listener);
	}
	
	private void clipChanged(ClipChangeType type) {
		for (ClipChangeListener l : getClipChangeListenerFor(type))
			l.handle(this, type);
		track.timeline.clipChanged(this, type);
	}
	
	private Set<ClipChangeListener> getClipChangeListenerFor(ClipChangeType type) {
		Set<ClipChangeListener> set = clipChangeListener.get(type);
		if (set == null) {
			set = new HashSet<ClipChangeListener>();
			clipChangeListener.put(type, set);
		}
		return set;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + getClipData().getTitle() + "@" + startTime + "-" + endTime;
	}

	@Override
	public void setData(String key, Object data) throws NullPointerException {
		if (key == null || data == null) throw new NullPointerException("Key or data cannot be null.");
		dataMap.put(key, data);		
	}

	@Override
	public Object getData(String key) throws NoSuchElementException {
		if (key == null) throw new NullPointerException("Key cannot be null.");
		final Object data = dataMap.get(key);
		if (data == null) throw new NoSuchElementException("No data exists for the specified type.");
		return data;
	}
}
