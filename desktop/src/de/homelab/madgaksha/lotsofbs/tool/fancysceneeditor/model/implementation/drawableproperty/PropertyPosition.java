package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.drawableproperty;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DimensionalValue.Value2D;

public class PropertyPosition extends AProperty<Value2D> {

	private final static PropertyEntry<Value2D> INITIAL = new PropertyEntry2D();
	static {
		INITIAL.getValue().setValues(new float[]{0f, 0f});
	}

	public PropertyPosition() {
		super(INITIAL, INITIAL);
		list.add(1, new PropertyEntry2D(0.5f,-1f,2f));
		list.add(1, new PropertyEntry2D(0.25f,3f,0f));
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
