package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.basic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelTrack;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.Seekable;

class BasicClip extends ABasicPart implements ModelClip {
	private final static Logger LOG = Logger.getLogger(BasicClip.class);

	private float startTime;
	private float endTime;
	private final ModelClipData clipData;
	private final EnumMap<ClipChangeType, Set<ClipChangeListener>> clipChangeListener = new EnumMap<>(
			ClipChangeType.class);
	private final Map<String, Object> dataMap = new HashMap<>();
	BasicTrack track;

	/**
	 * Creates a new track without the start and end time initialized, which
	 * must be done by the caller.
	 *
	 * @param track
	 *            Track to which this clip belongs.
	 * @param clipDataClazz
	 *            Class of the clip data for this clip.
	 * @throws NullPointerException
	 *             When track is null.
	 * @throws NoSuchMethodException
	 *             When the clip data could not be instantiated, mostly likely
	 *             due to a missing constructor.
	 */
	BasicClip(final BasicTrack track, final Class<? extends ModelClipData> clipDataClazz)
			throws NullPointerException, NoSuchMethodException {
		if (track == null)
			throw new NullPointerException("track cannot be null");
		if (clipDataClazz == null)
			throw new NullPointerException("clip data cannot be null");
		this.track = track;
		clipData = newClipData(clipDataClazz);
	}

	private final <T extends ModelClipData> T newClipData(final Class<T> clazz) throws NoSuchMethodException {
		try {
			for (final Constructor<?> c : clazz.getDeclaredConstructors()) {
				final Class<?>[] cl = c.getParameterTypes();
				if (cl.length == 1 && ModelClip.class.isAssignableFrom(cl[0])) {
					@SuppressWarnings("unchecked")
					final Constructor<T> ct = (Constructor<T>) c;
					ct.setAccessible(true);
					final T clipData = ct.newInstance(this);
					return clipData;
				}
			}
			throw new NoSuchMethodException("could not create new clip data");
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | ClassCastException e) {
			LOG.error("could not create new clip data", e);
			throw new NoSuchMethodException("could not create new clip data");
		}
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
		if (hasChanged)
			clipChanged(ClipChangeType.START_TIME);
	}

	@Override
	public void setEndTime(float endTime) {
		endTime = Math.max(startTime, endTime);
		endTime = Math.min(track.getEndTime(), endTime);
		final boolean hasChanged = endTime != this.endTime;
		this.endTime = endTime;
		if (hasChanged)
			clipChanged(ClipChangeType.END_TIME);
	}

	@Override
	public ModelTrack getParentTrack() {
		return track;
	}

	@Override
	public Seekable getParentTimeline() {
		return track.getParentTimeline();
	}

	@Override
	public ModelClipData getClipData() {
		return clipData;
	}

	@Override
	public void registerChangeListener(final ClipChangeType type, final ClipChangeListener listener) {
		if (listener == null)
			throw new NullPointerException("listener cannot be null");
		getClipChangeListenerFor(type).add(listener);
	}

	private void clipChanged(final ClipChangeType type) {
		for (final ClipChangeListener l : getClipChangeListenerFor(type))
			l.handle(this, type);
		for (final ClipChangeListener l : getClipChangeListenerFor(ClipChangeType.ALL))
			l.handle(this, ClipChangeType.ALL);
		track.timeline.clipChanged(this, type);
		track.timeline.clipChanged(this, ClipChangeType.ALL);
	}

	private Set<ClipChangeListener> getClipChangeListenerFor(final ClipChangeType type) {
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
	public void setData(final String key, final Object data) throws NullPointerException {
		if (key == null || data == null)
			throw new NullPointerException("Key or data cannot be null.");
		dataMap.put(key, data);
	}

	@Override
	public Object getData(final String key) throws NoSuchElementException {
		if (key == null)
			throw new NullPointerException("Key cannot be null.");
		final Object data = dataMap.get(key);
		if (data == null)
			throw new NoSuchElementException("No data exists for the specified type.");
		return data;
	}

	@Override
	public ClipChangeListener setStartTimeListener(final TimeIntervalListener listener) {
		final ClipChangeListener listenerId = new ClipChangeListener() {
			@Override
			public void handle(final ModelClip timeline, final ClipChangeType type) {
				listener.handle(getStartTime(), BasicClip.this);
			}
		};
		registerChangeListener(ClipChangeType.START_TIME, listenerId);
		return listenerId;
	}

	@Override
	public ClipChangeListener setEndTimeListener(final TimeIntervalListener listener) {
		final ClipChangeListener listenerId = new ClipChangeListener() {
			@Override
			public void handle(final ModelClip timeline, final ClipChangeType type) {
				listener.handle(getEndTime(), BasicClip.this);
			}
		};
		registerChangeListener(ClipChangeType.END_TIME, listenerId);
		return listenerId;
	}

	@Override
	public void removeStartTimeListener(final Object listenerId) {
		if (!(listenerId instanceof ClipChangeListener)) return;
		getClipChangeListenerFor(ClipChangeType.START_TIME).remove(listenerId);

	}

	@Override
	public void removeEndTimeListener(final Object listenerId) {
		if (!(listenerId instanceof ClipChangeListener)) return;
		getClipChangeListenerFor(ClipChangeType.END_TIME).remove(listenerId);
	}

	@Override
	public float getMinTime() {
		return track.getStartTime();
	}

	@Override
	public float getMaxTime() {
		return track.getEndTime();
	}

	@Override
	public void invalidateClipData() {
		clipChanged(ClipChangeType.CLIP_DATA);
	}
}
