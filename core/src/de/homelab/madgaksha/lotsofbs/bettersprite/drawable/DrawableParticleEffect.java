package de.homelab.madgaksha.lotsofbs.bettersprite.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.MathUtils;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcepool.EParticleEffect;
import de.homelab.madgaksha.lotsofbs.resourcepool.ResourcePool;
import de.homelab.madgaksha.lotsofbs.util.INewObject;

public class DrawableParticleEffect extends ADrawable<EParticleEffect, PooledEffect>
		implements INewObject<DrawableParticleEffect> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DrawableParticleEffect.class);
	private final static float MAX_SCALE = 1000.0f;
	// must be the inverse of MAX_SCALE, or resetting the particle effect will
	// not work properly
	private final static float MIN_SCALE = 1.0f / MAX_SCALE;
	private float scaleLast;
	private float unitPerPixel;

	/**
	 * Setting the color is a rather slow operation, try to avoid this if
	 * possible.
	 */
	@Override
	protected void applyColor(float r, float g, float b, float a) {
	}

	/** Not supported. */
	@Override
	protected void applyCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
	}

	/** Not supported. */
	@Override
	protected void applyOpacity(float opacity) {
	}

	/** Not supported. */
	@Override
	protected void applyOrigin(float originX, float originY) {
	}

	@Override
	protected void applyPosition(float positionX, float positionY) {
		drawable.setPosition(positionX, positionY);
	}

	/** Not supported. */
	@Override
	protected void applyRotation(float degree) {
	}

	@Override
	protected void applyScale(float scaleX, float scaleY) {
		float scaleNew = MathUtils.clamp(0.5f * unitPerPixel * (scaleX+scaleY), MIN_SCALE, MAX_SCALE);
		if (scaleNew != scaleLast) {
			drawable.scaleEffect(scaleNew / scaleLast);
			scaleLast = scaleNew;
		}
	}

	@Override
	protected PooledEffect loadResource(EParticleEffect resource) {
		return ResourcePool.obtainParticleEffect(resource);
	}

	@Override
	protected void performDispose() {
		final float scale = 1.0f / scaleLast;
		applyScale(scale, scale);
		ResourcePool.freeParticleEffect(drawable);
	}

	@Override
	protected void performRender(Batch batch) {
		// Apply color when not set to white
		if (isColorSet()) {
			final float r = getColorR();
			final float g = getColorG();
			final float b = getColorB();
			final float a = getColorA();
			for (ParticleEmitter pe : drawable.getEmitters()) {
				final Color c = pe.getSprite().getColor();
				pe.getSprite().setColor(c.r * r, c.g * g, c.b * b, c.a * a);
			}
		}
		drawable.draw(batch);
	}

	@Override
	protected boolean performUpdate(float deltaTime, float passedTime) {
		drawable.update(deltaTime);
		return drawable.isComplete();
	}

	@Override
	public DrawableParticleEffect newObject() {
		return new DrawableParticleEffect();
	}

	@Override
	protected void initResource(float unitPerPixel) {
		this.scaleLast = 1.0f;
		this.unitPerPixel = unitPerPixel;
		applyScale(1f, 1f);
	}
}