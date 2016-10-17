package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface ChangeListenable<TYPE,LISTENER> {
	/**
	 * Registers a listener for a certain event that will be triggered only for this clip.
	 * When there are clip change listener registered on the timeline, they must still get triggered.
	 * @param type Type of change the listener should be informed about.
	 * @param listener For handling the change event.
	 */
	public void registerChangeListener(TYPE type, LISTENER listener);
	/**
	 * Removes the listener of the given type. NoOp when no such listener was registered.
	 * @param type Type of the event.
	 * @param listener Listener to remove.
	 */
	public void removeChangeListener(TYPE type, LISTENER listener);
	default void registerChangeListener(final LISTENER listener, @SuppressWarnings("unchecked") final TYPE... typeList) {
		for (final TYPE type : typeList) registerChangeListener(type, listener);
	}
}
