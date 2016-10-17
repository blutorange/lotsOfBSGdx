package de.homelab.madgaksha.common;

public interface Serializable<T> {
	public Serializer<T> getSerializer();
}
