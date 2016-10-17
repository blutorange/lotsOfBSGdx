package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface ModelInterpolation {
	public String getLabel();
	public Actor getActor(Skin skin);
}
