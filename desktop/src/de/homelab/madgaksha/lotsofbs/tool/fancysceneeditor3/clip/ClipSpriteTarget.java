package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancySpritetarget;
import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationModeListComponent.AnimationMode;

public class ClipSpriteTarget extends ClipDrawable {

	private float direction;
	private AnimationMode mode;

	@Override
	protected void readDrawable(final Json json, final JsonValue jsonData) {
		direction = json.readValue(Float.class, jsonData);
		mode = json.readValue(AnimationMode.class, jsonData);
	}

	@Override
	protected void writeDrawable(final Json json) {
		json.writeValue("direction", direction);
		json.writeValue("mode", mode);
	}

	@Override
	protected AFancyEvent getFancyDrawable(final float adjustedStartTime, final String key, final float dpi) {
		return new FancySpritetarget(adjustedStartTime, getZIndex(), key, dpi, direction, mode);
	}
}
