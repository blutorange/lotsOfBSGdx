package de.homelab.madgaksha.scene2dext.connectible;

import java.util.Collection;
import java.util.HashSet;

public abstract class ModelConnectibleObject<T> implements ModelConnectible<T> {
	private final T value;
	private final Collection<ViewConnectible<T>> viewCollection = new HashSet<ViewConnectible<T>>();
	/**
	 * Subclasses should call this with the default value.
	 * @param value
	 */
	public ModelConnectibleObject(final T value) {
		this.value = value;
	}
	@Override
	public final void requestValue(T value) {
		final T newValue = sanitizeValue(value);
		final boolean hasChanged = !newValue.equals(value);
		value = newValue;
		if (hasChanged)
			for (final ViewConnectible<T> view : viewCollection)
				view.setValue(newValue);
	}
	@Override
	public final T getValue() {
		return value;
	}
	@Override
	public final void forView(final ViewConnectible<T> view) {
		viewCollection.add(view);
		view.onValueChange(this);
	}
	@Override
	public final void removeView(final ViewConnectible<T> view) {
		viewCollection.remove(view);
	}
	protected abstract T sanitizeValue(T v);
}
