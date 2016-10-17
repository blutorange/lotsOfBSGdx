package de.homelab.madgaksha.lotsofbs.tool.fancysceneeditor3.clip;

import java.util.Collection;

import javax.naming.InsufficientResourcesException;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.common.IdProvidable;
import de.homelab.madgaksha.common.Single;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.AFancyEvent;
import de.homelab.madgaksha.lotsofbs.cutscenesystem.fancyscene.FancyDamagetarget;

public class ClipDamageTarget extends AClip {
	private long damageMin;
	private long damageMax;
	@Override
	protected void writeSubclass(final Json json) {
		json.writeValue("damageMin", damageMin);
		json.writeValue("damageMax", damageMax);
	}

	@Override
	protected void readSubclass(final Json json, final JsonValue jsonData) {
		damageMin = json.readValue(Long.class, jsonData);
		damageMax = json.readValue(Long.class, jsonData);
	}

	@Override
	protected Collection<AFancyEvent> compileJava(final IdProvidable id, final float deltaTime, final float adjustedStartTime)
			throws InsufficientResourcesException {
		return new Single<AFancyEvent>(new FancyDamagetarget(adjustedStartTime, getZIndex(), damageMin, damageMax));
	}

}
