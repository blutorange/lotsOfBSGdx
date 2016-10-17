package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import java.util.Collection;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.common.IdProvidable;
import de.homelab.madgaksha.common.Single;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancySound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;

public class ClipSound extends AClip {

	private float volume;
	private ESound sound;
	private String entityName;

	@Override
	protected void writeSubclass(final Json json) {
		json.writeValue("entityName", entityName);
		json.writeValue("sound", sound);
		json.writeValue("volume", volume);
	}

	@Override
	protected void readSubclass(final Json json, final JsonValue jsonData) {
		entityName = json.readValue(String.class, jsonData);
		sound = json.readValue(ESound.class, jsonData);
		volume = json.readValue(Float.class, jsonData);
	}

	@Override
	protected Collection<AFancyEvent> compileJava(final IdProvidable id, final float deltaTime, final float adjustedStartTime)
			throws InsufficientResourcesException {
		return new Single<AFancyEvent>(new FancySound(adjustedStartTime, getZIndex(), entityName, sound, volume));
	}

}
