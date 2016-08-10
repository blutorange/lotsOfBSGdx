package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.drawableproperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DimensionalValue;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty;

public abstract class AProperty<T extends DimensionalValue> implements ModelDrawableProperty<T> {

	protected final List<PropertyEntry<T>> list = new ArrayList<>();
	private ModelClipData clipData;

	public AProperty(final PropertyEntry<T> start, final PropertyEntry<T> end) {
		final PropertyEntry<T> initStart = start.cloneObject();
		final PropertyEntry<T> initEnd = end.cloneObject();
		initStart.setTime(0f);
		initEnd.setTime(0f);
		list.add(initStart);
		list.add(initEnd);
	}

	@Override
	public final void setClipData(final ModelClipData clipData) {
		this.clipData = clipData;
		list.get(0).setTime(getStartTime());
		list.get(list.size()-1).setTime(getEndTime());
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

	protected ModelClipData getClipData() {
		return clipData;
	}
}
