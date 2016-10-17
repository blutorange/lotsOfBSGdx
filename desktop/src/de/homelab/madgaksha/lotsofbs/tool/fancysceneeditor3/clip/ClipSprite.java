package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancySprite;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;

public class ClipSprite extends ClipDrawable {

	private ETexture texture;

	/** @deprecated Serialization */
	@Deprecated
	public ClipSprite() {
	}
	public ClipSprite(final ETexture texture) {
		this.texture = texture;
	}

	@Override
	protected AFancyEvent getFancyDrawable(final float adjustedStartTime, final String key, final float dpi) {
		return new FancySprite(adjustedStartTime, getZIndex(), key, dpi, texture);
	}

	@Override
	protected void readDrawable(final Json json, final JsonValue jsonData) {
		texture = json.readValue(ETexture.class, jsonData);
	}

	@Override
	protected void writeDrawable(final Json json) {
		json.writeValue("texture", texture);
	}
}
