package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import de.homelab.madgaksha.safemutable.DimensionalValue.Value4D;

public class PropertyEntry4D extends APropertyEntry<Value4D> {
	private final Value4D value = new Value4D();

	public PropertyEntry4D(final Object parent) {
		this(0, 0, 0, 0, 0, parent);
	}

	public PropertyEntry4D(final float time, final float x1, final float x2, final float x3, final float x4,
			final Object parent) {
		super(time, parent);
		final float values[] = value.getValues();
		values[0] = x1;
		values[1] = x2;
		values[2] = x3;
		values[3] = x4;
	}

	@Override
	public Value4D getValue() {
		return value;
	}

	@Override
	public APropertyEntry<Value4D> newObject(final Object parent) {
		return new PropertyEntry4D(parent);
	}
}