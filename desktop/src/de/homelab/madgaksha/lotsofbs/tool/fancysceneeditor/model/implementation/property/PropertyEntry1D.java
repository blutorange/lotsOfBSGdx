package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import de.homelab.madgaksha.safemutable.DimensionalValue.Value1D;

public class PropertyEntry1D extends APropertyEntry<Value1D> {
	private final Value1D value = new Value1D();
	public PropertyEntry1D(final Object parent) {
		this(0f, 0f, parent);
	}
	public PropertyEntry1D(final float time, final float x, final Object parent) {
		super(time, parent);
		value.getValues()[0] = x;
	}

	@Override
	public Value1D getValue() {
		return value;
	}

	@Override
	public APropertyEntry<Value1D> newObject(final Object parent) {
		return new PropertyEntry1D(parent);
	}

}
