package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * A pool for sprites that supports {@link TextureAtlas}.
 * It uses {@link AtlasRegion}, {@link PoolableAtlasSprite}.
 * @author madgaksha
 *
 */
public class SpritePool extends Pool<PoolableAtlasSprite> {

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
	protected PoolableAtlasSprite newObject() {
		return new PoolableAtlasSprite();
	}
	
	@Override
	public PoolableAtlasSprite obtain () {
		PoolableAtlasSprite sprite = super.obtain();
		return sprite;
	}
	public PoolableAtlasSprite obtain(ETexture texture) {
		return obtain(ResourceCache.getTexture(texture));
	}
	
	public PoolableAtlasSprite obtain(AtlasRegion region) {
		PoolableAtlasSprite sprite = obtain();
		sprite.setAtlasRegion(region);
		return sprite;
	}
	
	public PoolableAtlasSprite obtain(TextureRegion region) {
		final PoolableAtlasSprite sprite = obtain();
		sprite.setTextureRegion(region);
		return sprite;
	}

}