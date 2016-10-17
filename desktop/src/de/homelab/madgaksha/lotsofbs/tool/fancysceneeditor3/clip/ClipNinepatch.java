package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyNinepatch;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;

public class ClipNinepatch extends ClipDrawable {

	private ENinePatch ninepatch;

	@Override
	protected AFancyEvent getFancyDrawable(final float adjustedStartTime, final String key, final float dpi) {
		return new FancyNinepatch(adjustedStartTime, getZIndex(), key, ninepatch);
	}

	@Override
	protected void readDrawable(final Json json, final JsonValue jsonData) {
		ninepatch = json.readValue(ENinePatch.class, jsonData);
	}

	@Override
	protected void writeDrawable(final Json json) {
		json.writeValue("ninepatch", ninepatch);
	}
}
