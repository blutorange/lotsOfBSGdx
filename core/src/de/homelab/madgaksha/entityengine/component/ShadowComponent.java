package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.resourcecache.ETexture;

/**
 * Represents the force that is applied to objects.
 * 
 * 
 * @author mad_gaksha
 */
public class ShadowComponent implements Component, Poolable {
	private final static float DEFAULT_OFFSET_X = 0.0f;
	private final static float DEFAULT_OFFSET_Y = -32.0f;
	private final static float DEFAULT_SCALE_FACTOR_X = 0.0f;
	private final static float DEFAULT_SCALE_FACTOR_Y = -0.025f;
	private final static float DEFAULT_OFFSET_ROTATE_X = 1.0f;
	private final static float DEFAULT_OFFSET_ROTATE_Y = 0.0f;

	public Sprite sprite;
	public float offsetX = DEFAULT_OFFSET_X;
	public float offsetY = DEFAULT_OFFSET_Y;
	public float scaleFactorX = DEFAULT_SCALE_FACTOR_X;
	public float scaleFactorY = DEFAULT_SCALE_FACTOR_Y;
	public float offsetRotateX = DEFAULT_OFFSET_ROTATE_X;
	public float offsetRotateY = DEFAULT_OFFSET_ROTATE_Y;

	public ShadowComponent() {
	}

	public ShadowComponent(ETexture texture) {
		this(texture, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y,
				DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public ShadowComponent(Sprite sprite) {
		this(sprite, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y,
				DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public ShadowComponent(ETexture texture, float offsetX, float offsetY) {
		this(texture, offsetX, offsetY, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y, DEFAULT_OFFSET_ROTATE_X,
				DEFAULT_OFFSET_ROTATE_Y);
	}

	public ShadowComponent(Sprite sprite, float offsetX, float offsetY) {
		this(sprite, offsetX, offsetY, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y, DEFAULT_OFFSET_ROTATE_X,
				DEFAULT_OFFSET_ROTATE_Y);
	}

	public ShadowComponent(ETexture texture, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY) {
		this(texture, offsetX, offsetY, scaleFactorX, scaleFactorY, DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public ShadowComponent(Sprite sprite, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY) {
		this(sprite, offsetX, offsetY, scaleFactorX, scaleFactorY, DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public ShadowComponent(ETexture texture, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY,
			float offsetRotateX, float offsetRotateY) {
		sprite = texture.asSprite();
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
		this.offsetRotateX = offsetRotateX;
		this.offsetRotateY = offsetRotateY;
		sprite.setOriginCenter();
	}

	public ShadowComponent(Sprite sprite, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY,
			float offsetRotateX, float offsetRotateY) {
		sprite = new Sprite();
		this.sprite = sprite;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
		this.offsetRotateX = offsetRotateX;
		this.offsetRotateY = offsetRotateY;
	}

	@Override
	public void reset() {
		sprite = null;
		offsetX = DEFAULT_OFFSET_X;
		offsetY = DEFAULT_OFFSET_Y;
		scaleFactorX = DEFAULT_SCALE_FACTOR_X;
		scaleFactorY = DEFAULT_SCALE_FACTOR_Y;
		offsetRotateX = DEFAULT_OFFSET_ROTATE_X;
		offsetRotateY = DEFAULT_OFFSET_ROTATE_Y;
	}

}
