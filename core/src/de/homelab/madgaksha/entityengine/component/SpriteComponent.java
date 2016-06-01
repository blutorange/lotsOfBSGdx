package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcepool.PoolableAtlasSprite;
import de.homelab.madgaksha.resourcepool.SpritePool;
import de.homelab.madgaksha.util.DebugStringifier;

public class SpriteComponent implements Component, Poolable {
	
	public PoolableAtlasSprite sprite;

	/**
	 * Creates a new sprite component. Used mainly for pooling.
	 */
	public SpriteComponent() {
	}
	
	/**
	 * Loads the sprite with texture from the given sprite.
	 * @param sprite The sprite with the texture to use.
	 */
	public SpriteComponent(PoolableAtlasSprite sprite) {
		setup(sprite);
	}
	
	/**
	 * Loads the sprite with the given texture.  
	 * @param texture Texture for the sprite.
	 */
	public SpriteComponent(ETexture texture) {
		setup(texture);
	}

	public SpriteComponent(SpriteAnimationComponent sac) {
		setup(sac);
	}

	public SpriteComponent(SpriteAnimationComponent sac, Vector2 origin) {
		setup(sac, origin);
	}
	
	public void setup(PoolableAtlasSprite sprite) {
		this.sprite = sprite;
	}
	
	public void setup(ETexture texture) {
		sprite = texture.asSprite();
		if (sprite != null) sprite.setOriginCenter();
	}
	
	public void setup(SpriteAnimationComponent sac) {
		final TextureRegion tr = sac.animation.getKeyFrame(0.0f);
		sprite = SpritePool.getInstance().obtain(tr);
	}
	
	public void setup(SpriteAnimationComponent sac, Vector2 origin) {
		final TextureRegion tr = sac.animation.getKeyFrame(0.0f);
		sprite = SpritePool.getInstance().obtain(tr);
		sprite.setOrigin(origin.x, origin.y);
	}
	
	@Override
	public void reset() {
		SpritePool.getInstance().free(sprite);
		sprite = null;
	}
	
	@Override
	public String toString(){
		return "SpriteComponent: " + DebugStringifier.get(sprite) + ")";
	}
}
