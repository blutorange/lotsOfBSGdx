package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.Action;
import de.homelab.madgaksha.scene2dext.model.ActionEnum;
import de.homelab.madgaksha.scene2dext.model.ActionNoOp;
import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public abstract class AbstractActionRenderer<T extends Model> implements ActionRenderer<T> {
	private Action<T> action = ActionNoOp.<T>get();

	@Override
	public ItemView<T> render(final ModelProvider<T> provider, final ActionEnum<T> action, final Skin skin) {
		return render(provider, action.getAction(), skin);
	}

	public ItemView<T> render(final ModelProvider<T> provider, final Skin skin) {
		return render(provider, action, skin);
	}

	public AbstractActionRenderer<T> setAction(final ActionEnum<T> action) {
		setAction(action.getAction());
		return this;
	}

	public AbstractActionRenderer<T> setAction(final Action<T> action) {
		if (action == null) throw new NullPointerException("Action must not be null.");
		this.action = action;
		return this;
	}
}