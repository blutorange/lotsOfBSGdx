package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyPeffect;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;

public class ClipPeffect extends ClipDrawable {

	private EParticleEffect peffect;

	@Override
	protected AFancyEvent getFancyDrawable(final float adjustedStartTime, final String key, final float dpi) {
		return new FancyPeffect(adjustedStartTime, getZIndex(), key, peffect, dpi);
	}

	@Override
	protected void readDrawable(final Json json, final JsonValue jsonData) {
		peffect = json.readValue(EParticleEffect.class, jsonData);
	}

	@Override
	protected void writeDrawable(final Json json) {
		json.writeValue("peffect", peffect);
	}
}
