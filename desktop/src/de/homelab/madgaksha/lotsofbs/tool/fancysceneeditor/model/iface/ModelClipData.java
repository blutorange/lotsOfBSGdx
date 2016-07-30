package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import javax.naming.InsufficientResourcesException;

public interface ModelClipData extends DetailsPanel {
	/**
	 * This method must add the appropriate commands for the event to the
	 * builder. May assume it the string builder starts on a new line. Should
	 * not add a new line.
	 * 
	 * @param builder
	 *            Builder to which the commands must be added.
     * @param idProvider Provides a unique id that can be used when one is required.
	 * @throws InsufficientResourcesException 
	 */
	public void renderEvent(StringBuilder builder, IdProvider idProvider) throws InsufficientResourcesException;
	
	public void clipAttached(ModelClip clip);
}
