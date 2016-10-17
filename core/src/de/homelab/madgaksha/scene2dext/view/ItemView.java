package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public interface ItemView<T extends Model> {
	public ModelProvider<T> getModelProvider();
	/**
	 * Implementing classes usually inherit from {@link Actor} or one of its
	 * subclasses such as {@link Table} and can simply return themselves.
	 *
	 * @return The actor for this view.
	 */
	public Actor asActor();
}
