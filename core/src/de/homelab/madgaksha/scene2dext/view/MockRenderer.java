package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public class MockRenderer<T extends Model> implements ItemRenderer<T> {
	@Override
	public ItemView<T> render(final ModelProvider<T> provider, final Skin skin) {
		return new MockView<T>(provider, skin);
	}
}