package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Pool;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * A pool for sprites that support {@link TextureAtlas}. It uses
 * {@link AtlasRegion}s and {@link CroppableAtlasSprite}s.
 * 
 * @author madgaksha
 *
 */
public class SpritePool extends Pool<CroppableAtlasSprite> {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SpritePool.class);
	private final static int INITIAL_SIZE = 100;
	private final static int MAX_SIZE = 10000;

	// Singleton
	private static class SingletonHolder {
		private static final SpritePool INSTANCE = new SpritePool();
	}

	public static SpritePool getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private SpritePool() {
		super(INITIAL_SIZE, MAX_SIZE);
	}

	@Override
	protected CroppableAtlasSprite newObject() {
		return new CroppableAtlasSprite();
	}

	public CroppableAtlasSprite obtain(ETexture texture) {
		return obtain(ResourceCache.getTexture(texture));
	}

	public CroppableAtlasSprite obtain(AtlasRegion region) {
		CroppableAtlasSprite sprite = obtain();
		sprite.setAtlasRegion(region);
		return sprite;
	}

}