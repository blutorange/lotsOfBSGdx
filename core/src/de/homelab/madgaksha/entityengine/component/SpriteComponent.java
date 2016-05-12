package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcecache.Resources.ETexture;

public class SpriteComponent implements Component, Poolable {
	
	public Sprite sprite = new Sprite();

	/**
	 * Loads the sprite with texture from the given sprite.
	 * @param sprite The sprite with the texture to use.
	 */
	public SpriteComponent(Sprite sprite) {
		this.sprite.setTexture(sprite.getTexture());
	}
	
	/**
	 * Loads the sprite with the given texture.  
	 * @param texture Texture for the sprite.
	 */
	public SpriteComponent(ETexture texture) {
		this.sprite.setTexture(ResourceCache.getTexture(texture));
	}

	@Override
	public void reset() {
		//TODO
		// We can reuse the Sprite instance. ?
	}
}
