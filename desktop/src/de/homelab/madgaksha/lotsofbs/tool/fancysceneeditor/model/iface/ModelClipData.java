package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import javax.naming.InsufficientResourcesException;

public interface ModelClipData extends DetailsPanel<ModelClip> {
	/**
	 * This method must add the appropriate commands for the event to the
	 * builder. May assume it the string builder starts on a new line. Should
	 * not add a new line.
	 * 
	 * @param builder
	 *            Builder to which the commands must be added.
     * @param idProvider Provides a unique id that can be used when one is required.
	 * @throws InsufficientResourcesException When the ID provider could not generate enough IDs. Must not leave
	 * the string in the buffer in an inconsistent state. The easiest way to do so is to retrieve all necessary
	 * IDs before adding strings to the buffer.
	 */
	public void renderEvent(StringBuilder builder, IdProvider idProvider) throws InsufficientResourcesException;
	
	public ModelClip getClip();
	
	@Override
	public default ModelClip getObject() {
		return getClip();
	}
}
