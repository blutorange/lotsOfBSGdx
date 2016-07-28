package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.implementation.clipdata;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.ModelClipData;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor.model.iface.TimelineProvider;

public class DrawableClipData implements ModelClipData {
	@Override
	public String getTypeName() {
		return "Drawable";
	}

	@Override
	public Actor getActor(TimelineProvider timelineProvider, Skin skin) {
		return new Label(getTypeName() + "@" + hashCode(), skin);
	}

}
