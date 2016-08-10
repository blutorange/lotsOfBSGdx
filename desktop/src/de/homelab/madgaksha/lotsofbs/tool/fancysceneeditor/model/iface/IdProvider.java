package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import javax.naming.InsufficientResourcesException;

public interface IdProvider {
	/**
	 * A unique ID different from any other this object has returned so far.
	 * @return The unique ID.
	 * @throws InsufficientResourcesException When no more unique IDs are available that could be returned.
	 */
	public String uniqueId() throws InsufficientResourcesException;
}
