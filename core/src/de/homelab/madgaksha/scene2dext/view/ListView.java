package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.scene2dext.model.ListModel;
import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public class ListView<T extends Model> extends Table implements ItemView<ListModel<T>> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ListView.class);
	private final ModelProvider<ListModel<T>> provider;
	private final ItemRenderer<T> renderer;
	private int size = 0;
	private final Skin skin;
	private final boolean vertical;

	public ListView(final ModelProvider<ListModel<T>> provider, final ItemRenderer<T> renderer, final boolean vertical,
			final Skin skin) {
		super();
		this.provider = provider;
		this.renderer = renderer;
		this.skin = skin;
		this.vertical = vertical;
	}

	@Override
	public ModelProvider<ListModel<T>> getModelProvider() {
		return provider;
	}

	@Override
	public void validate() {
		updateView();
		super.validate();
	}

	@Override
	public Actor asActor() {
		return this;
	}

	private void updateView() {
		final ListModel<T> model = provider.getProvidedObject();
		final int newSize = model.getSize();
		if (newSize != size) {
			clearChildren();
			for (int i = 0; i != model.getSize(); ++i) {
				final ListRowProvider<T> listRowProvider = new ListRowProvider<T>(provider, i);
				if (vertical) {
					add(renderer.render(listRowProvider, skin).asActor()).expandX().fillX();
					row();
				}
				else {
					add(renderer.render(listRowProvider, skin).asActor()).expandY().fillY();
				}
			}
			size = newSize;
			invalidateHierarchy();
		}
	}
}
