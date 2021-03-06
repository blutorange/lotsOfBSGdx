package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;
import de.homelab.madgaksha.lotsofbs.util.DebugStringifier;

public class SpriteComponent implements Component, Poolable {

	private boolean managed = false;

	public CroppableAtlasSprite sprite;

	/**
	 * Creates a new sprite component. Used mainly for pooling.
	 */
	public SpriteComponent() {
	}

	/**
	 * Loads the sprite with texture from the given sprite.
	 * 
	 * @param sprite
	 *            The sprite with the texture to use.
	 */
	public SpriteComponent(CroppableAtlasSprite sprite) {
		setup(sprite);
	}

	/**
	 * Loads the sprite with the given texture.
	 * 
	 * @param texture
	 *            Texture for the sprite.
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

	public void setup(CroppableAtlasSprite sprite) {
		this.sprite = sprite;
		this.managed = false;
	}

	public void setup(ETexture texture) {
		sprite = texture.asSprite();
		this.managed = true;
		if (sprite != null)
			sprite.setOriginCenter();
	}

	public void setup(SpriteAnimationComponent sac) {
		final AtlasRegion ar = sac.animation.getKeyFrame(0.0f);
		this.managed = false;
		sprite = ResourcePool.obtainSprite(ar);
	}

	public void setup(SpriteAnimationComponent sac, Vector2 origin) {
		final AtlasRegion ar = sac.animation.getKeyFrame(0.0f);
		sprite = ResourcePool.obtainSprite(ar);
		this.managed = false;
		sprite.setOrigin(origin.x, origin.y);
	}

	@Override
	public void reset() {
		if (managed)
			ResourcePool.freeSprite(sprite);
		sprite = null;
		managed = false;
	}

	@Override
	public String toString() {
		return "SpriteComponent: " + DebugStringifier.get(sprite) + ")";
	}
}
