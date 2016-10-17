package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public interface ItemRenderer<T extends Model> {
	public ItemView<T> render(ModelProvider<T> provider, Skin skin);
}