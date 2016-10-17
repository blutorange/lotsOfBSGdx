package de.homelab.madgaksha.scene2dext.view;



import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.ModelProvider;
import de.homelab.madgaksha.scene2dext.model.NumberModel;
import de.homelab.madgaksha.scene2dext.view.NumericInput.FloatNumericInput;

public class FloatView extends FloatNumericInput implements ItemView<NumberModel<Float>> {
	private final ModelProvider<NumberModel<Float>> modelProvider;
	private boolean editMode;

	public FloatView(final ModelProvider<NumberModel<Float>> provider, final Skin skin) {
		super(provider.getProvidedObject().getValue(), skin);
		modelProvider = provider;
		editMode = isEditMode();
	}

	@Override
	public void draw (final Batch batch, final float parentAlpha) {
		final NumberModel<Float> model = modelProvider.getProvidedObject();
		if (isEditMode()) {
			editMode = true;
			model.setValue(getValue());
		}
		else {
			if (editMode) {
				model.setValue(getValue());
				editMode = false;
			}
			setValue(model.getValue());
		}
		super.draw(batch, parentAlpha);
	}

	@Override
	public ModelProvider<NumberModel<Float>> getModelProvider() {
		return modelProvider;
	}

	@Override
	public Actor asActor() {
		return this;
	}
}
