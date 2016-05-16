package de.homelab.madgaksha.level;

import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ETiledMap;
import de.homelab.madgaksha.resourcecache.IResource;;

/**
 * Only for testing purposes.
 * 
 * @author madgaksha
 */
public class Level01 extends ALevel {

	@Override
	protected ETexture requestedBackgroundImage() {
		return ETexture.MAIN_BACKGROUND;
	}

	@Override
	protected IResource[] requestedRequiredResources() {
		return new IResource[] {
				ETexture.JOSHUA_RUNNING,
				EMusic.SOPHISTICATED_FIGHT,
				ETiledMap.LEVEL_01
		};
	}

	@Override
	public ETiledMap requestedTiledMap() {
		return ETiledMap.LEVEL_01;
	}	
	
	@Override
	public EMusic requestedBgm() {
		return EMusic.SOPHISTICATED_FIGHT;
	}

	@Override
	public Vector2 requestedPlayerInitialPosition() {
		return new Vector2(35,20);
	}

	@Override
	public String requestedI18nNameKey() {
		return "level.01.name";
	}

}