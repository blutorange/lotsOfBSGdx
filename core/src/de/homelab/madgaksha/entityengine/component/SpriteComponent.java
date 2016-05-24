package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcecache.ETexture;

public class SpriteComponent implements Component, Poolable {
	
	public Sprite sprite;

	/**
	 * Loads the sprite with texture from the given sprite.
	 * @param sprite The sprite with the texture to use.
	 */
	public SpriteComponent(Sprite sprite) {
		this.sprite = sprite;
	}
	
	/**
	 * Loads the sprite with the given texture.  
	 * @param texture Texture for the sprite.
	 */
	public SpriteComponent(ETexture texture) {
		sprite = texture.asSprite();
	}

	public SpriteComponent(SpriteAnimationComponent sac) {
		final TextureRegion tr = sac.animation.getKeyFrame(0.0f);
		sprite = new Sprite(tr); // constructor calls #setOriginCenter()
		sprite.setTexture(tr.getTexture());
	}
	
	@Override
	public void reset() {
		//TODO
		// We can reuse the Sprite instance. ?
		sprite = new Sprite();
		sprite.setCenter(0.0f, 0.0f);
		sprite.setColor(Color.WHITE);
		sprite.setFlip(false, false);
		sprite.setOriginCenter();
		sprite.setPosition(0.0f, 0.0f);
		sprite.setRotation(0.0f);
		sprite.setScale(1.0f);
	}
}
