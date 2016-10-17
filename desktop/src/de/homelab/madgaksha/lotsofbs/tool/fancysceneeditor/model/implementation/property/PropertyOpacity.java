package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.safemutable.DimensionalValue.Value1D;

public class PropertyOpacity extends AProperty<Value1D> {
	private final static String NAME = "Opacity";
	private final static Value1D MIN_VALUE = new Value1D(0f);
	private final static Value1D MAX_VALUE = new Value1D(1f);


	private final APropertyEntry<Value1D> INITIAL = new PropertyEntry1D(0f, 1f, this);

	public PropertyOpacity(final ModelClipData clipData) {
		super(clipData);
		super.init(INITIAL, INITIAL);
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
