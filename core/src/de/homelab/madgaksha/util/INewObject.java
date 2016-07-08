package de.homelab.madgaksha.util;

/**
 * Objects that can return a new instance of their type.
 * @author madgaksha
 */
public interface INewObject<T> {
	public T newObject();
}
