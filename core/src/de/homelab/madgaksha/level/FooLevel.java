package de.homelab.madgaksha.level;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ETiledMap;
import de.homelab.madgaksha.resourcecache.IResource;

/**
 * Only for testing purposes.
 * 
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
		return 16000.0f;
	}

	@Override
	public float requestedMapHeightW() {
		return 16000.0f;
	}

	@Override
	protected ETexture requestedBackgroundImage() {
		return ETexture.FOOLEVEL_BACKGROUND;
	}

	@Override
	protected IResource[] requestedRequiredResources() {
		return new IResource[] {
				ETexture.BADLOGIC,
				ETexture.ESTELLE_RUNNING,
				ETexture.JOSHUA_RUNNING,
				EMusic.TEST_ADX_STEREO,
				ETiledMap.MAP_FOOLEVEL
		};
	}

	@Override
	public ETiledMap requestedTiledMap() {
		return ETiledMap.MAP_FOOLEVEL;
	}	
	
	@Override
	public EMusic requestedBgm() {
		return EMusic.TEST_ADX_STEREO;
	}

}
