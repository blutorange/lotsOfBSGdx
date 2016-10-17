package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import java.util.Collection;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.common.IdProvidable;
import de.homelab.madgaksha.common.Single;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyShake;
import de.homelab.madgaksha.lotsofbs.enums.RichterScale;

public class ClipShake extends AClip {

	private RichterScale strength = RichterScale.M1;

	/** @deprecated Serialization */
	@Deprecated
	public ClipShake() {
	}

	public ClipShake(final RichterScale strength) {
		this.strength = strength;
	}

	@Override
	public Collection<AFancyEvent> compileJava(final IdProvidable id, final float deltaTime, final float adjustedStartTime) {
		return new Single<AFancyEvent>(new FancyShake(adjustedStartTime, getZIndex(), strength, getDuration()));
	}

	@Override
	protected void writeSubclass(final Json json) {
		json.writeValue("strength", strength);
	}

	@Override
	protected void readSubclass(final Json json, final JsonValue jsonData) {
		strength = json.readValue(RichterScale.class, jsonData);
	}
}
