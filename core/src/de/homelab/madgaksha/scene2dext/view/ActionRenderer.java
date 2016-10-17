package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.Action;
import de.homelab.madgaksha.scene2dext.model.ActionEnum;
import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public interface ActionRenderer<T extends Model> {
	public ItemView<T> render(ModelProvider<T> provider, Action<T> action, Skin skin);
	public ItemView<T> render(ModelProvider<T> provider, ActionEnum<T> action, Skin skin);
}
