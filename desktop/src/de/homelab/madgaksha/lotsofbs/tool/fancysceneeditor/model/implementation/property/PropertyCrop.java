package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.property;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.safemutable.DimensionalValue.Value4D;

public class PropertyCrop extends AProperty<Value4D> {

	private final APropertyEntry<Value4D> INITIAL = new PropertyEntry4D(0f, 1f, 1f, 1f, 1f, this);

	public PropertyCrop(final ModelClipData clipData) {
		super(clipData);
		super.init(INITIAL, INITIAL);
	}

	private final static String NAME = "Crop";
	private final static Value4D MIN_VALUE = new Value4D(0f,0f,0f,0f);
	private final static Value4D MAX_VALUE = new Value4D(1f,1f,1f,1f);

	@Override
	public int getDimension() {
		return 4;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Value4D getMinValue() {
		return MIN_VALUE;
	}

	@Override
	public Value4D getMaxValue() {
		return MAX_VALUE;
	}
}
