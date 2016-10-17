package de.homelab.madgaksha.scene2dext.model;

public class StaticProvider<T> implements Provider<T> {
	private final T object;
	public StaticProvider(final T object) {
		this.object = object;
	}
	@Override
	public T getProvidedObject() {
		return object;
	}
}
