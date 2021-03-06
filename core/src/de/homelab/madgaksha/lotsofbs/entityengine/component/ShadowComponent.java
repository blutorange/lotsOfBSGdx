package de.homelab.madgaksha.lotsofbs.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableSprite;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;

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
	private final static boolean DEFAULT_RELATIVE_TO_CAMERA = true;

	public CroppableSprite sprite;
	public float offsetX = DEFAULT_OFFSET_X;
	public float offsetY = DEFAULT_OFFSET_Y;
	public float scaleFactorX = DEFAULT_SCALE_FACTOR_X;
	public float scaleFactorY = DEFAULT_SCALE_FACTOR_Y;
	public float offsetRotateX = DEFAULT_OFFSET_ROTATE_X;
	public float offsetRotateY = DEFAULT_OFFSET_ROTATE_Y;
	public boolean relativeToCamera = DEFAULT_RELATIVE_TO_CAMERA;

	public ShadowComponent() {
	}

	public ShadowComponent(ETexture texture) {
		setup(texture);
	}

	public ShadowComponent(CroppableAtlasSprite sprite) {
		setup(sprite);
	}

	public ShadowComponent(ETexture texture, float offsetX, float offsetY) {
		setup(texture, offsetX, offsetY);
	}

	public ShadowComponent(CroppableAtlasSprite sprite, float offsetX, float offsetY) {
		setup(sprite, offsetX, offsetY);
	}

	public ShadowComponent(ETexture texture, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY) {
		setup(texture, offsetX, offsetY, scaleFactorX, scaleFactorY);
	}

	public ShadowComponent(CroppableAtlasSprite sprite, float offsetX, float offsetY, float scaleFactorX,
			float scaleFactorY) {
		setup(sprite, offsetX, offsetY, scaleFactorX, scaleFactorY);
	}

	public ShadowComponent(ETexture texture, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY,
			float offsetRotateX, float offsetRotateY) {
		setup(texture, offsetX, offsetY, scaleFactorX, scaleFactorY, offsetRotateX, offsetRotateY);
	}

	public ShadowComponent(CroppableAtlasSprite sprite, float offsetX, float offsetY, float scaleFactorX,
			float scaleFactorY, float offsetRotateX, float offsetRotateY) {
		setup(sprite, offsetX, offsetY, scaleFactorX, scaleFactorY, offsetRotateX, offsetRotateY);
	}

	public void setup(ETexture texture) {
		setup(texture, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y,
				DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public void setup(CroppableAtlasSprite sprite) {
		setup(sprite, DEFAULT_OFFSET_X, DEFAULT_OFFSET_Y, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y,
				DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public void setup(ETexture texture, float offsetX, float offsetY) {
		setup(texture, offsetX, offsetY, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y, DEFAULT_OFFSET_ROTATE_X,
				DEFAULT_OFFSET_ROTATE_Y);
	}

	public void setup(CroppableAtlasSprite sprite, float offsetX, float offsetY) {
		setup(sprite, offsetX, offsetY, DEFAULT_SCALE_FACTOR_X, DEFAULT_SCALE_FACTOR_Y, DEFAULT_OFFSET_ROTATE_X,
				DEFAULT_OFFSET_ROTATE_Y);
	}

	public void setup(ETexture texture, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY) {
		setup(texture, offsetX, offsetY, scaleFactorX, scaleFactorY, DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public void setup(CroppableAtlasSprite sprite, float offsetX, float offsetY, float scaleFactorX,
			float scaleFactorY) {
		setup(sprite, offsetX, offsetY, scaleFactorX, scaleFactorY, DEFAULT_OFFSET_ROTATE_X, DEFAULT_OFFSET_ROTATE_Y);
	}

	public void setup(ETexture texture, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY,
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

	public void setup(CroppableAtlasSprite sprite, float offsetX, float offsetY, float scaleFactorX, float scaleFactorY,
			float offsetRotateX, float offsetRotateY) {
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
		this.relativeToCamera = DEFAULT_RELATIVE_TO_CAMERA;
	}

}
