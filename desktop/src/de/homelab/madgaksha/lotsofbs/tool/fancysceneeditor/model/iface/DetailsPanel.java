package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface DetailsPanel<T> {
	/**
	 * @param timelineProvider
	 * @param skin
	 * @return The actor representing the details.
	 */
	public Actor getActor(TimelineProvider timelineProvider, Skin skin);
	public String getDescription();
	public String getTitle();
	/** @return  The object for this details panel. */
	public T getObject();
}
