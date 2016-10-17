package de.homelab.madgaksha.scene2dext.model;

public interface SingleValueModel<T> extends Model {
	public void setValue(T value);
	public T getValue();
}
