package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.ModelProvider;
import de.homelab.madgaksha.scene2dext.model.NumberModel;
import de.homelab.madgaksha.scene2dext.view.NumericInput.IntegerNumericInput;

public class IntegerView extends IntegerNumericInput implements ItemView<NumberModel<Integer>> {
	private final ModelProvider<NumberModel<Integer>> modelProvider;
	private boolean editMode;
	public IntegerView(final ModelProvider<NumberModel<Integer>> provider, final Skin skin) {
		this(provider, 10, skin);
	}
	public IntegerView(final ModelProvider<NumberModel<Integer>> provider, final int base, final Skin skin) {
		super(provider.getProvidedObject().getValue(), base, skin);
		modelProvider = provider;
		editMode = isEditMode();
	}

	@Override
	public void draw (final Batch batch, final float parentAlpha) {
		final NumberModel<Integer> model = modelProvider.getProvidedObject();
		if (isEditMode()) {
			editMode = true;
			model.setValue(getValue());
		}
		else {
			if (editMode) {
				model.setValue(getValue());
			}
			editMode = false;
			setValue(model.getValue());
		}
		super.draw(batch, parentAlpha);
	}

	@Override
	public ModelProvider<NumberModel<Integer>> getModelProvider() {
		return modelProvider;
	}

	@Override
	public Actor asActor() {
		return this;
	}
}
