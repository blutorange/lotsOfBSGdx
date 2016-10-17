package de.homelab.madgaksha.scene2dext.connectible;

public class ModelConnectibleFloat extends ModelConnectibleObject<Float>  {
	private final Float min;
	private Float max;
	public ModelConnectibleFloat() {
		this(0f);
	}
	public ModelConnectibleFloat(final Float f) {
		super(f);
		min = max = f;
	}
	@Override
	protected Float sanitizeValue(final Float v) {
		return v < min ? min : v > max ? max : v;
	}

}
