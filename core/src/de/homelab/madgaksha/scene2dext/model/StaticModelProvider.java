package de.homelab.madgaksha.scene2dext.model;

public class StaticModelProvider<T extends Model> extends StaticProvider<T> implements ModelProvider<T> {
	public StaticModelProvider(final T object) {
		super(object);
	}
}