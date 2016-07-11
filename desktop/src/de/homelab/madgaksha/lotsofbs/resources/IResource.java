package de.homelab.madgaksha.lotsofbs.resources;

import java.net.URL;

/**
 * The interface that must be implemented by each resource Enum. It contains
 * methods for acquiring the URL of the resource and loading its data it to an
 * appropriate Object.
 * 
 * @author madgaksha
 */
public interface IResource {
	/**
	 * @return The url to the file representing this resource.
	 */
	public URL getUrl();

	/**
	 * @return This. The Enum instance representing this resource.
	 */
	public Enum<?> getEnum();

	/**
	 * @return The maximum number of resources that can be loaded/cached at
	 *         once.
	 */
	public int getLimit();

	/**
	 * Creates and returns an object representing this resource, eg. an
	 * {@see AudioFile}. The data should be loaded into RAM.
	 * 
	 * @return The object representing this resource.
	 */
	public Object getObject();
}