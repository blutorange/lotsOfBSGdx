package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.scene2dext.model.ColorModel;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;
import de.homelab.madgaksha.scene2dext.model.NumberModel;
import de.homelab.madgaksha.scene2dext.model.StaticModelProvider;

public class ColorRendererRGBA implements ItemRenderer<ColorModel> {
	boolean displayLabels = false;

	@Override
	public ColorViewRGBA render(final ModelProvider<ColorModel> provider, final Skin skin) {
		return new ColorViewRGBA(provider, displayLabels, skin);
	}

	public ColorRendererRGBA setDisplayLabels(final boolean displayLabels) {
		this.displayLabels = displayLabels;
		return this;
	}

	public static class ColorViewRGBA extends Table implements ItemView<ColorModel> {
		private final ModelProvider<ColorModel> provider;

		public ColorViewRGBA(final ModelProvider<ColorModel> provider, final boolean displayLabels, final Skin skin) {
			this.provider = provider;
			createView(displayLabels, skin);
		}

		@Override
		public ModelProvider<ColorModel> getModelProvider() {
			return provider;
		}

		@Override
		public Actor asActor() {
			return this;
		}

		private void createView(final boolean displayLabels, final Skin skin) {
			final ModelProvider<NumberModel<Float>> providerR = new StaticModelProvider<NumberModel<Float>>(new NumberModelR(provider));
			final ModelProvider<NumberModel<Float>> providerG = new StaticModelProvider<NumberModel<Float>>(new NumberModelG(provider));
			final ModelProvider<NumberModel<Float>> providerB = new StaticModelProvider<NumberModel<Float>>(new NumberModelB(provider));
			final ModelProvider<NumberModel<Float>> providerA = new StaticModelProvider<NumberModel<Float>>(new NumberModelA(provider));
			final FloatView viewR = new FloatView(providerR, skin);
			final FloatView viewG = new FloatView(providerG, skin);
			final FloatView viewB = new FloatView(providerB, skin);
			final FloatView viewA = new FloatView(providerA, skin);
			viewR.setMinMax(0f, 1f);
			viewG.setMinMax(0f, 1f);
			viewB.setMinMax(0f, 1f);
			viewA.setMinMax(0f, 1f);
			viewR.setStep(0.01f);
			viewG.setStep(0.01f);
			viewB.setStep(0.01f);
			viewA.setStep(0.01f);
			if (displayLabels) {
				final Label labelR = new Label("R", skin);
				final Label labelG = new Label("G", skin);
				final Label labelB = new Label("B", skin);
				final Label labelA = new Label("A", skin);
				labelR.setAlignment(Align.center);
				labelG.setAlignment(Align.center);
				labelB.setAlignment(Align.center);
				labelA.setAlignment(Align.center);
				add(labelR).fillX();
				add(labelG).fillX();
				add(labelB).fillX();
				add(labelA).fillX();
				row();
			}
			add(viewR).expandX().fillX();
			add(viewG).expandX().fillX();
			add(viewB).expandX().fillX();
			add(viewA).expandX().fillX();
		}
	}

	private static class NumberModelR extends NumberModel<Float> {
		final ModelProvider<ColorModel> provider;
		public NumberModelR(final ModelProvider<ColorModel> provider) {
			super(provider.getProvidedObject().getValue().r);
			this.provider = provider;
		}
		@Override
		public void setValue(final Float value) {
			provider.getProvidedObject().setR(value);
		}
		@Override
		public Float getValue() {
			return provider.getProvidedObject().getValue().r;
		}
	}
	private static class NumberModelG extends NumberModel<Float> {
		final ModelProvider<ColorModel> provider;
		public NumberModelG(final ModelProvider<ColorModel> provider) {
			super(provider.getProvidedObject().getValue().g);
			this.provider = provider;
		}
		@Override
		public void setValue(final Float value) {
			provider.getProvidedObject().setG(value);
		}
		@Override
		public Float getValue() {
			return provider.getProvidedObject().getValue().g;
		}
	}
	private static class NumberModelB extends NumberModel<Float> {
		final ModelProvider<ColorModel> provider;
		public NumberModelB(final ModelProvider<ColorModel> provider) {
			super(provider.getProvidedObject().getValue().b);
			this.provider = provider;
		}
		@Override
		public void setValue(final Float value) {
			provider.getProvidedObject().setB(value);
		}
		@Override
		public Float getValue() {
			return provider.getProvidedObject().getValue().b;
		}
	}
	private static class NumberModelA extends NumberModel<Float>{
		final ModelProvider<ColorModel> provider;
		public NumberModelA(final ModelProvider<ColorModel> provider) {
			super(provider.getProvidedObject().getValue().a);
			this.provider = provider;
		}
		@Override
		public void setValue(final Float value) {
			provider.getProvidedObject().setA(value);
		}
		@Override
		public Float getValue() {
			return provider.getProvidedObject().getValue().a;
		}
	}
}
