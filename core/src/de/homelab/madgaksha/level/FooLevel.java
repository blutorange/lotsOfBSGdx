package de.homelab.madgaksha.level;

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
	protected ETexture requestedBackgroundImage() {
		return ETexture.FOOLEVEL_BACKGROUND;
	}

	@Override
	protected IResource[] requestedRequiredResources() {
		return new IResource[] {
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
