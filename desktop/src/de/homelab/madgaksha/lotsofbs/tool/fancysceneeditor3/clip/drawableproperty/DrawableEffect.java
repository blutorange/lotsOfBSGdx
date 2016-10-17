package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.drawableproperty;

import java.util.Collection;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.utils.Json.Serializable;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip.ClipDrawable;

public interface DrawableEffect extends Serializable {
	public Collection<AFancyEvent> toFancyEventList(final String key, final int zIndex, ClipDrawable clipDrawable, float deltaTime) throws InsufficientResourcesException;
}