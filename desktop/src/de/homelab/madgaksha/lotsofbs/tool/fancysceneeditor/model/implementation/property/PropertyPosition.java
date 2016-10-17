package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.safemutable.DimensionalValue.Value2D;

public class PropertyPosition extends AProperty<Value2D> {

	private final APropertyEntry<Value2D> INITIAL = new PropertyEntry2D(0f, 0f, 0f, this);

	public PropertyPosition(final ModelClipData clipData) {
		super(clipData);
		super.init(INITIAL, INITIAL);

	}

	private final static String NAME = "Position";
	private final static Value2D MIN_VALUE = new Value2D(-100f,-100f);
	private final static Value2D MAX_VALUE = new Value2D(100f, 100f);

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Value2D getMinValue() {
		return MIN_VALUE;
	}

	@Override
	public Value2D getMaxValue() {
		return MAX_VALUE;
	}
}
