package de.homelab.madgaksha.scene2dext.model;

public class DynamicModelProvider<T extends Model> extends DynamicProvider<T> implements ModelProvider<T> {
	public DynamicModelProvider(final T object) {
		super(object);
	}
}