package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;

public class ClipAnimation extends ClipDrawable {

	private EAnimation animation;

	@Override
	protected AFancyEvent getFancyDrawable(final float adjustedStartTime, final String key, final float dpi) {
		return new FancyAnimation(adjustedStartTime, getZIndex(), key, dpi, animation);
	}

	@Override
	protected void readDrawable(final Json json, final JsonValue jsonData) {
		animation = json.readValue(EAnimation.class, jsonData);
	}

	@Override
	protected void writeDrawable(final Json json) {
		json.writeValue("animation", animation);
	}
}
