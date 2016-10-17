package de.homelab.madgaksha.scene2dext.connectible;

import java.util.Collection;
import java.util.HashSet;

import de.homelab.madgaksha.safemutable.SafeMutable;

public abstract class ModelConnectibleSafeMutable<T extends SafeMutable<T>> implements ModelConnectible<T> {
	private final T value;
	private final Collection<ViewConnectible<T>> viewCollection = new HashSet<ViewConnectible<T>>();
	/**
	 * Subclasses should call this with the default value.
	 * @param value
	 */
	public ModelConnectibleSafeMutable(final T value) {
		this.value = value.cloneValue();
	}
	@Override
	public final void requestValue(final T value) {
		final T newValue = getValue();
		sanitizeValue(newValue);
		final boolean hasChanged = !newValue.equals(value);
		value.setValue(newValue);
		if (hasChanged)
			for (final ViewConnectible<T> view : viewCollection)
				view.setValue(newValue);
	}
	@Override
	public final T getValue() {
		return value.cloneValue();
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
	protected abstract void sanitizeValue(T v);
}
