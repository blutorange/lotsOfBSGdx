package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DimensionalValue.Value1D;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DimensionalValue.Value2D;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.DimensionalValue.Value4D;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty.PropertyEntry;

public interface ModelDrawableProperty<T extends DimensionalValue> extends Iterable<PropertyEntry<T>>, TimeIntervalGetter {
	public String getName();
	public int getDimension();
	public int size();
	public T getMinValue();
	public T getMaxValue();
	public void setClipData(ModelClipData clipData);

	public static interface PropertyEntry<T extends DimensionalValue> extends Clone<PropertyEntry<T>> {
		public float getTime();
		public void setTime(float startTime);
		public T getValue();
		public PropertyEntry<T> newObject();
		@Override
		default PropertyEntry<T> cloneObject() {
			final PropertyEntry<T> entry = newObject();
			entry.setTime(getTime());
			entry.getValue().setValues(getValue().getValues());
			return entry;
		}
	}

	public static class PropertyEntry1D implements PropertyEntry<Value1D> {
		private float time;
		private final Value1D value = new Value1D();
		public PropertyEntry1D() {}
		public PropertyEntry1D(final float time, final float x) {
			this.time = time;
			value.getValues()[0] = x;
		}
		@Override
		public float getTime() {
			return time;
		}

		@Override
		public Value1D getValue() {
			return value;
		}

		@Override
		public void setTime(final float startTime) {
			this.time = startTime;
		}

		@Override
		public PropertyEntry<Value1D> newObject() {
			return new PropertyEntry1D();
		}
	}

	public static class PropertyEntry2D implements PropertyEntry<Value2D> {
		private float time;
		private final Value2D value = new Value2D();
		public PropertyEntry2D() {}
		public PropertyEntry2D(final float time, final float x, final float y) {
			this.time = time;
			value.getValues()[0] = x;
			value.getValues()[1] = y;
		}
		@Override
		public float getTime() {
			return time;
		}

		@Override
		public Value2D getValue() {
			return value;
		}

		@Override
		public void setTime(final float startTime) {
			this.time = startTime;
		}

		@Override
		public PropertyEntry<Value2D> newObject() {
			return new PropertyEntry2D();
		}
	}

	public static class PropertyEntry4D implements PropertyEntry<Value4D> {
		private float time;
		private final Value4D value = new Value4D();
		public PropertyEntry4D() {}
		public PropertyEntry4D(final float time, final float x1, final float x2, final float x3, final float x4) {
			this.time = time;
			final float values[] = value.getValues();
			values[0] = x1;
			values[1] = x2;
			values[2] = x3;
			values[3] = x4;
		}
		@Override
		public float getTime() {
			return time;
		}

		@Override
		public Value4D getValue() {
			return value;
		}

		@Override
		public void setTime(final float startTime) {
			this.time = startTime;
		}

		@Override
		public PropertyEntry<Value4D> newObject() {
			return new PropertyEntry4D();
		}
	}
}
