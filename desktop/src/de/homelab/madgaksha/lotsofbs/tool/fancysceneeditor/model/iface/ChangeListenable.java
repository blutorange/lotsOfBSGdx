package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

public interface ChangeListenable<TYPE,LISTENER> {
	/**
	 * Registers a listener for a certain event that will be triggered only for this clip.
	 * When there are clip change listener registered on the timeline, they must still get triggered.
	 * @param type Type of change the listener should be informed about.
	 * @param listener For handling the change event.
	 */
	public void registerChangeListener(TYPE type, LISTENER listener);
	default void registerChangeListener(LISTENER listener, @SuppressWarnings("unchecked") TYPE... typeList) {
		for (TYPE type : typeList) registerChangeListener(type, listener);
	}
}
