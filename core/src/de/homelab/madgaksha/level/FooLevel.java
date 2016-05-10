package de.homelab.madgaksha.level;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.resourcecache.Resources.ETexture;


/**
 * Only for testing purposes.
 * @author madgaksha
 */
@Deprecated
public class FooLevel extends ALevel {

	@Override
	public void write(Json json) {
		super.write(json);
		// custom code for custom fields goes here...
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);
		// custom code for custom fields goes here...
	}

	@Override
	public float requestedMapWidthW() {
		return 10.0f;
	}

	@Override
	public float requestedMapHeightW() {
		return 10.0f;
	}

	@Override
	protected ETexture requestedBackgroundImage() {
		return ETexture.FOOLEVEL_BACKGROUND;
	}

}
