package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import com.badlogic.gdx.utils.Json.Serializable;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;

public interface DrawableProperty extends Serializable {
	public AFancyEvent toFancyEvent(final String key, int zIndex, final float deltaTime);
}