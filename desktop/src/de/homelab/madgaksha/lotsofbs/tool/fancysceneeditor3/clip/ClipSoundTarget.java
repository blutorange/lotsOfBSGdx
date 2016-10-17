package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import java.util.Collection;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.common.IdProvidable;
import de.homelab.madgaksha.common.Single;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancySoundtarget;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VoiceComponent.VoiceRetriever;

public class ClipSoundTarget extends AClip {

	private VoiceRetriever voiceRetriever;
	private float volume;

	@Override
	protected void writeSubclass(final Json json) {
		json.writeValue("voiceRetriever", voiceRetriever);
		json.writeValue("volume", volume);
	}

	@Override
	protected void readSubclass(final Json json, final JsonValue jsonData) {
		voiceRetriever = json.readValue(VoiceRetriever.class, jsonData);
		volume = json.readValue(Float.class, jsonData);
	}

	@Override
	protected Collection<AFancyEvent> compileJava(final IdProvidable id, final float deltaTime, final float adjustedStartTime)
			throws InsufficientResourcesException {
		return new Single<AFancyEvent>(new FancySoundtarget(adjustedStartTime, getZIndex(), voiceRetriever, volume));
	}
}
