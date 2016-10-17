package de.homelab.madgaksha.scene2dext.view;

import de.homelab.madgaksha.scene2dext.model.ListModel;
import de.homelab.madgaksha.scene2dext.model.Model;
import de.homelab.madgaksha.scene2dext.model.ModelProvider;
import de.homelab.madgaksha.scene2dext.model.Provider;

public class ListRowProvider<T extends Model> implements ModelProvider<T> {
	private final Provider<ListModel<T>> provider;
	private final int position;
	public ListRowProvider(final Provider<ListModel<T>> provider, final int position) {
		this.provider = provider;
		this.position = position;
	}
	@Override
	public T getProvidedObject() {
		return provider.getProvidedObject().getEntry(position);
	}
}