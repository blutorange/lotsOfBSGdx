package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import de.homelab.madgaksha.safemutable.DimensionalValue.Value2D;

public class PropertyEntry2D extends APropertyEntry<Value2D> {
	private final Value2D value = new Value2D();
	public PropertyEntry2D(final Object parent) {
		this(0, 0, 0, parent);
	}
	public PropertyEntry2D(final float time, final float x, final float y, final Object parent) {
		super(time, parent);
		value.getValues()[0] = x;
		value.getValues()[1] = y;
	}

	@Override
	public Value2D getValue() {
		return value;
	}

	@Override
	public APropertyEntry<Value2D> newObject(final Object parent) {
		return new PropertyEntry2D(parent);
	}
}