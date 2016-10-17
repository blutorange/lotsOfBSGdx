package de.homelab.madgaksha.scene2dext.model;

public class ImmutableModel<T> implements SingleValueModel<T> {
	protected T value;
	public ImmutableModel(final T initialValue) throws NullPointerException {
		if (initialValue == null) throw new NullPointerException("Value must not be null.");
		value = initialValue;
	}
	@Override
	public void setValue(final T value) {
		this.value = value;
	}
	@Override
	public T getValue() {
		return value;
	}
}