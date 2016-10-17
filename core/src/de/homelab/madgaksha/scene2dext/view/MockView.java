package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

/**
 * A view no displaying anything. Can be used for any model.
 * @author madgaksha
 *
 * @param <T> Type of the model.
 */
public class MockView<T extends Model> extends Actor implements ItemView<T> {
	private final ModelProvider<T> provider;
	private final Actor actor;
	public MockView(final ModelProvider<T> provider, final Skin skin) {
		this.provider = provider;
		this.actor = new Label("MockView@" + provider.getClass().getSimpleName(), skin);
	}
	@Override
	public ModelProvider<T> getModelProvider() {
		return provider;
	}
	@Override
	public Actor asActor() {
		return actor;
	}

}
