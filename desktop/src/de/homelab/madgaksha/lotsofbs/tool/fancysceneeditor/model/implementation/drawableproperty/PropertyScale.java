package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.drawableproperty;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DimensionalValue.Value1D;

public class PropertyScale extends AProperty<Value1D> {
	private final static String NAME = "Scale";
	private final static Value1D MIN_VALUE = new Value1D(0f);
	private final static Value1D MAX_VALUE = new Value1D(100f);


	private final static PropertyEntry<Value1D> INITIAL = new PropertyEntry1D();
	static {
		INITIAL.getValue().setValues(new float[]{1f});
	}

	public PropertyScale() {
		super(INITIAL, INITIAL);
	}


	@Override
	public int getDimension() {
		return 1;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Value1D getMinValue() {
		return MIN_VALUE;
	}

	@Override
	public Value1D getMaxValue() {
		return MAX_VALUE;
	}
}
