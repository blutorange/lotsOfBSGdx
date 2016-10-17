package de.homelab.madgaksha.safemutable;

public interface SafeMutable<T> {
	public T cloneValue();
	public void setValue(T value);
}