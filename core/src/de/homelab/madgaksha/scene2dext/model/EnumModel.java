package de.homelab.madgaksha.scene2dext.model;

public class EnumModel<T extends Enum<T>> extends ImmutableModel<T> {
	public EnumModel(final Class<T> clazz) {
		this(clazz.getEnumConstants()[0]);
	}
	public EnumModel(final T initialValue) throws NullPointerException {
		super(initialValue);
	}
	public Enum<T> getEnum() {
		return value;
	}
}