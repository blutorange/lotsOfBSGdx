package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.ModelProvider;
import de.homelab.madgaksha.scene2dext.model.NumberModel;

public interface NumberRenderer<T extends Number> extends ItemRenderer<NumberModel<T>> {
	public static class FloatRenderer implements NumberRenderer<Float> {
		@Override
		public ItemView<NumberModel<Float>> render(final ModelProvider<NumberModel<Float>> provider, final Skin skin) {
			return new FloatView(provider, skin);
		}
	}
	public static class IntegerRenderer implements NumberRenderer<Integer> {
		private int base;
		public void setBase(final int base) {
			this.base = base;
		}
		@Override
		public ItemView<NumberModel<Integer>> render(final ModelProvider<NumberModel<Integer>> provider, final Skin skin) {
			return new IntegerView(provider, base, skin);
		}
	}
	public static class LongRenderer implements NumberRenderer<Long> {
		private int base;
		public void setBase(final int base) {
			this.base = base;
		}
		@Override
		public ItemView<NumberModel<Long>> render(final ModelProvider<NumberModel<Long>> provider, final Skin skin) {
			return new LongView(provider, base, skin);
		}
	}
}