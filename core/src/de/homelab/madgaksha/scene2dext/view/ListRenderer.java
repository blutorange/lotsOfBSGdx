package de.homelab.madgaksha.scene2dext.view;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.scene2dext.model.ListModel;
import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;

public class ListRenderer<T extends Model> implements ItemRenderer<ListModel<T>> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ListRenderer.class);

	private final ItemRenderer<T> renderer;
	private boolean vertical = true;

	public ListRenderer(final ItemRenderer<T> renderer) {
		this(renderer, ListOrientation.VERTICAL);
	}

	public ListRenderer(final ItemRenderer<T> renderer, final ListOrientation orientation) {
		this.renderer = renderer;
		this.vertical = orientation == ListOrientation.VERTICAL;
	}

	public void setOrientation(final ListOrientation orientation) {
		vertical = orientation == ListOrientation.VERTICAL;
	}

	public void setItemRenderer(final ItemRenderer<T> renderer) {
		if (renderer != null) throw new NullPointerException("Renderer cannot be null.");
	}

	@Override
	public ListView<T> render(final ModelProvider<ListModel<T>> provider, final Skin skin) {
		return new ListView<T>(provider, renderer, vertical, skin);
	}

	public static enum ListOrientation {
		HORIZONTAL, VERTICAL;
	}
}
