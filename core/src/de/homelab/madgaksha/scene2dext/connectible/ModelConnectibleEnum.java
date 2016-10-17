package de.homelab.madgaksha.scene2dext.connectible;

public class ModelConnectibleEnum<T extends Enum<T>> extends ModelConnectibleObject<Enum<T>> {
	public ModelConnectibleEnum(final Enum<T> value) {
		super(value);
	}
	@Override
	protected Enum<T> sanitizeValue(final Enum<T> v) {
		return v;
	}
}
