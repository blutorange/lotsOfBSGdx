package de.homelab.madgaksha.lotsofbs.resourcecache;

import java.util.EnumMap;

public interface IResource<K extends Enum<K>, V> {
	/**
	 * @return This. The Enum instance representing this resource.
	 */
	public Enum<K> getEnum();

	/**
	 * @return The maximum number of resources that can be loaded/cached at
	 *         once.
	 */
	public int getLimit();

	/**
	 * Creates and returns an object representing this resource, eg. an
	 * {@link Music}. The data should be loaded into RAM.
	 * 
	 * @return The object representing this resource.
	 */
	public V getObject();

	/**
	 * Disposes the object and removes from the cache. Blocks until done.
	 */
	public void clear();

	/** Clears all resources of this type. */
	public void clearAllOfThisKind();

	/**
	 * The map used to cache the objects.
	 */
	@SuppressWarnings("rawtypes")
	EnumMap getMap();
}
