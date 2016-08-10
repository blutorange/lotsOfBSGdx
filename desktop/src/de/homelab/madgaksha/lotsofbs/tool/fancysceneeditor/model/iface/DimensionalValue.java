package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface DimensionalValue extends Clone<DimensionalValue> {
	public int getDimension();
	public float[] getValues();
	public void setValues(final float[] values);
	public DimensionalValue newObject();
	@Override
	default DimensionalValue cloneObject() {
		final DimensionalValue val = newObject();
		val.setValues(getValues());
		return val;
	}

	public static class Value1D implements DimensionalValue {
		public final float values[] = new float[1];
		public Value1D() {
			this(0f);
		}
		public Value1D(final float x1) {
			values[0] = x1;
		}
		@Override
		public int getDimension() {
			return values.length;
		}
		@Override
		public float[] getValues() {
			return values;
		}
		@Override
		public Value1D newObject() {
			return new Value1D();
		}
		@Override
		public void setValues(final float[] values) {
			if (values.length != 1) throw new IllegalArgumentException("Array length must be 1");
			this.values[0] = values[0];
		}
	}
	public static class Value2D implements DimensionalValue {
		public final float values[] = new float[2];
		public Value2D() {
			this(0f, 0f);
		}

		public Value2D(final float x1, final float x2) {
			values[0] = x1;
			values[1] = x2;
		}
		@Override
		public int getDimension() {
			return values.length;
		}
		@Override
		public float[] getValues() {
			return values;
		}
		@Override
		public Value2D newObject() {
			return new Value2D();
		}
		@Override
		public void setValues(final float[] values) {
			if (values.length != 2) throw new IllegalArgumentException("Array length must be 2");
			this.values[0] = values[0];
			this.values[1] = values[1];
		}
	}
	public static class Value3D implements DimensionalValue {
		public final float values[] = new float[3];
		public Value3D() {
			this(0f, 0f, 0f);
		}
		public Value3D(final float x1, final float x2, final float x3) {
			values[0] = x1;
			values[1] = x2;
			values[2] = x3;
		}
		@Override
		public int getDimension() {
			return values.length;
		}
		@Override
		public float[] getValues() {
			return values;
		}
		@Override
		public Value3D newObject() {
			return new Value3D();
		}
		@Override
		public void setValues(final float[] values) {
			if (values.length != 3) throw new IllegalArgumentException("Array length must be 3");
			this.values[0] = values[0];
			this.values[1] = values[1];
			this.values[2] = values[2];
		}
	}
	public static class Value4D implements DimensionalValue {
		public final float values[] = new float[4];
		public Value4D() {
			this(0f, 0f, 0f, 0f);
		}
		public Value4D(final float x1, final float x2, final float x3, final float x4) {
			values[0] = x1;
			values[1] = x2;
			values[2] = x3;
			values[3] = x4;
		}
		@Override
		public int getDimension() {
			return values.length;
		}
		@Override
		public float[] getValues() {
			return values;
		}
		@Override
		public Value4D newObject() {
			return new Value4D();
		}
		@Override
		public void setValues(final float[] values) {
			if (values.length != 4) throw new IllegalArgumentException("Array length must be 4");
			this.values[0] = values[0];
			this.values[1] = values[1];
			this.values[2] = values[2];
			this.values[3] = values[3];
		}
	}
}
