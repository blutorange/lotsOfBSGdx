package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.converter.ColorLongConverter;
import de.homelab.madgaksha.scene2dext.model.ColorModel;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;
import de.homelab.madgaksha.scene2dext.model.NumberConversionModel;
import de.homelab.madgaksha.scene2dext.model.NumberModel;
import de.homelab.madgaksha.scene2dext.model.StaticModelProvider;
import de.homelab.madgaksha.scene2dext.view.NumericInput.LongNumericInput;

public class ColorRendererHex implements ItemRenderer<ColorModel> {
	@Override
	public ItemView<ColorModel> render(final ModelProvider<ColorModel> provider, final Skin skin) {
		return new ColorViewHex(provider, skin);
	}

	private static class ColorViewHex extends Container<LongNumericInput> implements ItemView<ColorModel> {
		private final ModelProvider<ColorModel> provider;

		public ColorViewHex(final ModelProvider<ColorModel> provider, final Skin skin) {
			super();
			this.provider = provider;
			createView(skin);
		}

		private void createView(final Skin skin) {
			final NumberModel<Long> longModel = new NumberConversionModel<Color, Long, ColorModel>(
					ColorLongConverter.HEX.get(), provider);
			final ModelProvider<NumberModel<Long>> longProvider = new StaticModelProvider<NumberModel<Long>>(longModel);
			final LongView longView = new LongView(longProvider, 16, skin);
			longView.setMinMax(0L, 0xFFFFFFFFL);
			longView.setStep(1L);
			setActor(longView);
		}

		@Override
		public ModelProvider<ColorModel> getModelProvider() {
			return provider;
		}

		@Override
		public Actor asActor() {
			return this;
		}
	}
}
