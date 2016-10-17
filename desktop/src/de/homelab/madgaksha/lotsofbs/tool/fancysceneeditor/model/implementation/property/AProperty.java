package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DrawablePropertyChangeListener;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DrawablePropertyChangeListener.DrawablePropertyChangeType;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.ACustomDataHolder;
import de.homelab.madgaksha.safemutable.DimensionalValue;

public abstract class AProperty<T extends DimensionalValue> extends ACustomDataHolder
implements ModelDrawableProperty<T> {

	protected final List<PropertyEntry<T>> list = new ArrayList<>();
	private final EnumMap<DrawablePropertyChangeType, Set<DrawablePropertyChangeListener>> drawablePropertyChangeListener = new EnumMap<>(
			DrawablePropertyChangeType.class);
	private final ModelClipData clipData;

	protected AProperty(final ModelClipData clipData) {
		if (clipData == null) throw new NullPointerException("clip data cannot be null");
		this.clipData = clipData;

	}

	protected void init(final APropertyEntry<T> start, final APropertyEntry<T> end) {
		final PropertyEntry<T> initStart = start.cloneObject();
		final PropertyEntry<T> initEnd = end.cloneObject();
		list.add(initStart);
		list.add(initEnd);
		initStart.setTime(getStartTime());
		initEnd.setTime(getEndTime());
	}

	public void timeChanged() {
		final float startTime = getStartTime();
		final float endTime = getEndTime();
		list.get(0).setTime(startTime);
		list.get(list.size()-1).setTime(endTime);
		for (final PropertyEntry<? extends DimensionalValue> entry : this) {
			final float time = entry.getTime();
			if (time < startTime) {
				entry.setTime(startTime);
			}
			if (time > endTime) {
				entry.setTime(endTime);
			}
		}
	}

	@Override
	public final Iterator<PropertyEntry<T>> iterator() {
		return list.iterator();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public float getStartTime() {
		return getMinTime();
	}

	@Override
	public float getEndTime() {
		return getMaxTime();
	}

	@Override
	public float getMinTime() {
		return clipData.getClip().getStartTime();
	}

	@Override
	public float getMaxTime() {
		return clipData.getClip().getEndTime();
	}

	@Override
	public void registerChangeListener(final DrawablePropertyChangeType type,
			final DrawablePropertyChangeListener listener) {
		if (listener == null)
			throw new NullPointerException("listener cannot be null");
		getDrawablePropertyChangeListenerFor(type).add(listener);
	}

	@Override
	public void removeChangeListener(final DrawablePropertyChangeType type, final DrawablePropertyChangeListener listener) {
		getDrawablePropertyChangeListenerFor(type).remove(listener);
	}

	@Override
	public int getIndex(final PropertyEntry<?> entry) throws NoSuchElementException {
		final int index = list.indexOf(entry);
		if (index < 0) throw new NoSuchElementException("no such entry");
		return index;
	}

	public float getMinTimeOf(final APropertyEntry<T> entry) {
		try {
			final int pos = getIndex(entry);
			return pos != 0 ? list.get(pos-1).getTime() : getStartTime();
		}
		catch (final NoSuchElementException e) {
			return -Float.MAX_VALUE;
		}
	}

	public float getMaxTimeOf(final APropertyEntry<T> entry) {
		try {
			final int pos = getIndex(entry);
			return pos < list.size()-1 ? list.get(pos+1).getTime() : getEndTime();
		}
		catch (final NoSuchElementException e) {
			return Float.MAX_VALUE;
		}
	}

	@Override
	public PropertyEntry<T> insertPropertyEntry(final int position) throws IndexOutOfBoundsException {
		final PropertyEntry<T> someEntry = list.get(0);
		if (!(someEntry instanceof APropertyEntry)) throw new IllegalStateException("list must contain only AProperty entries");
		final APropertyEntry<T> entry = ((APropertyEntry<T>)someEntry).newObject(this);
		list.add(position, entry);

		final float timeBefore = position > 0 ? list.get(position-1).getTime() : getStartTime();
		final float timeAfter = position < list.size() ? list.get(position).getTime() : getEndTime();
		entry.setTime((timeBefore+timeAfter)*0.5f);

		fireChangeEvent(DrawablePropertyChangeType.ADDED, entry);
		return entry;
	}

	@Override
	public void removePropertyEntry(final int position) throws IndexOutOfBoundsException {
		final PropertyEntry<T> entry = list.get(position);
		list.remove(position);
		fireChangeEvent(DrawablePropertyChangeType.REMOVED, entry);
	}


	void fireChangeEvent(final DrawablePropertyChangeType type, final PropertyEntry<T> entry) {
		for (final DrawablePropertyChangeListener listener : getDrawablePropertyChangeListenerFor(type)) {
			listener.handle(entry, type);
		}
	}

	private Set<DrawablePropertyChangeListener> getDrawablePropertyChangeListenerFor(
			final DrawablePropertyChangeType type) {
		Set<DrawablePropertyChangeListener> set = drawablePropertyChangeListener.get(type);
		if (set == null) {
			set = new HashSet<DrawablePropertyChangeListener>();
			drawablePropertyChangeListener.put(type, set);
		}
		return set;
	}

	protected ModelClipData getClipData() {
		return clipData;
	}
}
