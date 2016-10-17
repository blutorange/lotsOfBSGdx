package de.homelab.madgaksha.scene2dext.model;

public class DynamicProvider<T> implements Provider<T> {
	private T object;
	public DynamicProvider(final T object) {
		this.object = object;
	}
	@Override
	public T getProvidedObject() {
		return object;
	}
	public void setProvidedObject(final T object) throws NullPointerException {
		if (object == null) throw new NullPointerException("Object must not be null.");
		this.object = object;
	}
}
