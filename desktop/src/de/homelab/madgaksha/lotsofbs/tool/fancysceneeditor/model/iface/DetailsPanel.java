package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface DetailsPanel {
	public Actor getActor(TimelineProvider timelineProvider, Skin skin);
	public String getDescription();
	public String getTitle();
}
