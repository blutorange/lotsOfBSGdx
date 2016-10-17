package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelDrawableProperty.PropertyEntry;

public interface DrawablePropertyChangeListener {
	/**
	 * @param track The track that has changed.
	 * @param type The type of change. May be null.
	 */
	public void handle(PropertyEntry<?> entry, DrawablePropertyChangeType type);
	public static enum DrawablePropertyChangeType {
		ADDED,
		REMOVED,
		VALUE,
		TIME;
	}
	public static interface DrawablePropertyChangeListenable extends ChangeListenable<DrawablePropertyChangeType, DrawablePropertyChangeListener> {
	}
}
