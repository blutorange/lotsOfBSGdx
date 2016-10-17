package de.homelab.madgaksha.scene2dext.model;

public class StaticFloatListProvider extends StaticModelProvider<ListModel<NumberModel<Float>>> {
	public StaticFloatListProvider(final float... values) {
		super(new FloatListModel(values));
	}
	@Override
	public FloatListModel getProvidedObject() {
		return (FloatListModel)super.getProvidedObject();
	}

	public static class FloatListModel extends ArrayListModel<NumberModel<Float>> {
		public FloatListModel(final float... values) {
			list.ensureCapacity(values.length);
			for (int i = 0; i != values.length; ++i) {
				final NumberModel<Float> numberModel = new NumberModel<Float>(values[i]);
				list.add(numberModel);
			}
		}
		public void add(final float value) {
			final NumberModel<Float> numberModel = new NumberModel<Float>(value);
			list.add(numberModel);
		}
	}
}
