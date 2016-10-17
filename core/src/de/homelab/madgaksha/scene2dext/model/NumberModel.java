package de.homelab.madgaksha.scene2dext.model;

public class NumberModel<T extends Number> extends ImmutableModel<T> {
	public NumberModel(final T initialValue) {
		super(initialValue);
	}
}