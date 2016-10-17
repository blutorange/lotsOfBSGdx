package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.scene2dext.model.EnumModel;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public class EnumRendererDropdown<T extends Enum<T>> implements ItemRenderer<EnumModel<T>> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EnumRendererDropdown.class);

	@Override
	public EnumViewDropdown<T> render(final ModelProvider<EnumModel<T>> provider, final Skin skin) {
		return new EnumViewDropdown<T>(provider, skin);
	}

	public static class EnumViewDropdown<T extends Enum<T>> extends SelectBox<T> implements ItemView<EnumModel<T>> {
		private final ModelProvider<EnumModel<T>> provider;
		private Class<T> declaringClass;
		private Enum<T> lastValue;
		public EnumViewDropdown(final ModelProvider<EnumModel<T>> provider, final Skin skin) {
			super(skin);
			this.provider = provider;
			setListener();
			setOptions();
		}

		@Override
		public void draw(final Batch batch, final float parentAlpha) {
			final T value = provider.getProvidedObject().getValue();
			if (declaringClass != value.getDeclaringClass()) {
				setOptions();
			}
			if (value != lastValue) {
				lastValue = value;
				setSelected(value);
			}
			super.draw(batch, parentAlpha);
		}

		@Override
		public ModelProvider<EnumModel<T>> getModelProvider() {
			return provider;
		}

		@Override
		public Actor asActor() {
			return this;
		}

		private void setListener() {
			addListener(new ChangeListener() {
				@Override
				public void changed(final ChangeEvent event, final Actor actor) {
					provider.getProvidedObject().setValue(getSelected());
				}
			});
		}

		private void setOptions() {
			final T value = provider.getProvidedObject().getValue();
			declaringClass = value.getDeclaringClass();
			final T[] enumList = declaringClass.getEnumConstants();
			setItems(enumList);
			setSelected(value);
			lastValue = value;
		}
	}
}
