package de.homelab.madgaksha.cutscenesystem;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.sun.xml.internal.ws.dump.LoggingDumpTube.Position;

import de.homelab.madgaksha.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.getter.GetColor;
import de.homelab.madgaksha.getter.GetVector2;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimation;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.EParticleEffect;
import de.homelab.madgaksha.resourcepool.ResourcePool;

public final class FancyDrawable {
	private final static Logger LOG = Logger.getLogger(FancyDrawable.class);

	private CroppableAtlasSprite drawableSprite = ETexture.DEFAULT.asSprite();
	private NinePatch drawableNinePatch = ResourceCache.getNinePatch(ENinePatch.DEFAULT);
	private AtlasAnimation drawableAtlasAnimation = ResourceCache.getAnimation(EAnimation.DEFAULT);
	private PooledEffect drawablePooledEffect = ResourcePool.obtainParticleEffect(EParticleEffect.DEFAULT);

	private Vector2 dimensionsNinePatch = new Vector2();
	private float dimensionsSpriteUnitsPerScreen;

	private float scaleScreenX;
	private float scaleScreenY;
	private float scaleUnitsPerScreen;

	private Vector2 position = new Vector2();
	private Vector2 origin = new Vector2();
	private Vector2 scale = new Vector2();
	private Vector2 cropX = new Vector2();
	private Vector2 cropY = new Vector2();
	private Color color = new Color();
	private float opacity;
	private float rotation;

	private boolean scalePe;
	private boolean positionPe;
	private float scaleParticleEffectLast = 1.0f;

	private Mode mode;

	public static enum Mode {
		UNSET,
		TEXTURE,
		NINE_PATCH,
		ATLAS_ANIMATION,
		PARTICLE_EFFECT,
		CLEANUP;
	}

	public FancyDrawable() {
		resetToDefaults();
	}

	@Override
	public String toString() {
		return "FancyDrawable-" + mode + "@" + position;
	}

	public void resetToDefaults() {
		if (mode == Mode.CLEANUP)
			return;

		dimensionsNinePatch.set(0.0f, 0.0f);
		dimensionsSpriteUnitsPerScreen = 1.0f;

		scaleScreenX = 640.0f / 8.0f;
		scaleScreenY = scaleScreenX * 9.0f / 8.0f;
		scaleUnitsPerScreen = 1.0f;

		position.set(0f, 0f);
		origin.set(0.5f, 0.5f);
		scale.set(1f, 1f);
		cropX.set(1f, 1f);
		cropY.set(1f, 1f);
		color.set(Color.WHITE);
		opacity = 1.0f;
		rotation = 0.0f;

		scalePe = positionPe = false;
		// scaleParticleEffectLast = scaleUnitsPerScreen * (scale.x + scale.y) *
		// 0.5f;

		mode = Mode.UNSET;
	}

	/**
	 * 
	 * Sets the position relative to the {@link #origin}.
	 * 
	 * @param position
	 */
	public void setPosition(Vector2 position) {
		this.position.set(position);
		positionPe = true;
	}

	/**
	 * Sets the position relative to the {@link #origin}.
	 * 
	 * @param x
	 *            position coordinate.
	 * @param y
	 *            position coordinate.
	 */
	public void setPosition(float x, float y) {
		this.position.set(x, y);
		positionPe = true;
	}

	/**
	 * 
	 * Sets the origin relative to which {@link #rotation} and {@link Position}
	 * are specified.
	 * 
	 * @param position
	 */
	public void setOrigin(Vector2 origin) {
		this.origin.set(origin);
		positionPe = true;
	}

	/**
	 * Sets the origin relative to which {@link #rotation} and {@link Position}
	 * are specified.
	 * 
	 * @param originX
	 *            origin coordinate.
	 * @param originY
	 *            origin coordinate.
	 */
	public void setOrigin(float originX, float originY) {
		this.origin.set(originX, originY);
		positionPe = true;
	}

	/**
	 * 
	 * Sets the scale relative to the {@link FancyDrawable#origin}.
	 * 
	 * @param scale
	 *            scaleX and scaleY.
	 */
	public void setScale(Vector2 scale) {
		this.scale.set(scale);
		scalePe = true;
	}

	public void setScaleLerp(Vector2 scaleStart, Vector2 scaleEnd, float alpha) {
		scale.set(scaleStart).lerp(scaleEnd, alpha);
		scalePe = true;
	}

	/**
	 * Sets the scale relative to the {@link FancyDrawable#origin}.
	 * 
	 * @param scaleXY
	 *            Scale in both x and y direction.
	 */
	public void setScale(float scaleXY) {
		this.scale.set(scaleXY, scaleXY);
		scalePe = true;
	}

	/**
	 * Sets the scale relative to the {@link FancyDrawable#origin}.
	 * 
	 * @param scaleX
	 *            Scale factor.
	 * @param scaleY
	 *            Scale factor.
	 */
	public void setScale(float scaleX, float scaleY) {
		this.scale.set(scaleX, scaleY);
		scalePe = true;
	}

	/**
	 * Sets the amount of cropping relativ to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 * 
	 * @param cropLeft
	 * @param cropRight
	 * @param cropBottom
	 * @param cropTop
	 */
	public void setCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
		this.cropX.set(cropLeft, cropRight);
		this.cropY.set(cropBottom, cropTop);
	}

	/**
	 * Sets the amount of cropping relative to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 * 
	 * @param cropLeft
	 * @param cropRight
	 * @param cropBottom
	 * @param cropTop
	 */
	public void setCrop(Vector2 cropX, Vector2 cropY) {
		this.cropX.set(cropX);
		this.cropY.set(cropY);
	}

	/**
	 * Sets the amount of cropping relative to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 * 
	 * @param cropX
	 */
	public void setCropX(Vector2 cropX) {
		this.cropX.set(cropX);
	}

	public void setCropXLerp(Vector2 cropXStart, Vector2 cropXEnd, float alpha) {
		cropX.set(cropXStart).lerp(cropXEnd, alpha);
	}

	/**
	 * Sets the amount of cropping relative to the {@link #origin}. For example,
	 * cropLeft=1.0 means that nothing left of the origin will be cropped
	 * Setting cropLeft=0.0 means that everything to the left of the origin will
	 * be cropped.
	 * 
	 * @param cropY
	 */
	public void setCropY(Vector2 cropY) {
		this.cropY.set(cropY);
	}

	public void setCropYLerp(Vector2 cropYStart, Vector2 cropYEnd, float alpha) {
		this.cropY.set(cropYStart).lerp(cropYEnd, alpha);
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	public void setColor(Color color) {
		this.color.set(color);
	}

	public void setColor(float r, float g, float b, float a) {
		this.color.set(r, g, b, a);
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getOpacity() {
		return opacity;
	}

	public float getRotation() {
		return rotation;
	}

	private void setDrawable(AtlasRegion region, float unitsPerScreen) {
		drawableSprite.setAtlasRegion(region);
		dimensionsSpriteUnitsPerScreen = unitsPerScreen;
		resize();
	}

	public void setDrawable(ETexture texture, float unitsPerScreen) {
		if (mode == Mode.CLEANUP)
			return;
		AtlasRegion region = ResourceCache.getTexture(texture);
		if (region != null) {
			setDrawable(ResourceCache.getTexture(texture), unitsPerScreen);
			mode = FancyDrawable.Mode.TEXTURE;
		}
	}

	public void setDrawable(EAnimation animation, float unitsPerScreen) {
		if (mode == Mode.CLEANUP)
			return;
		AtlasAnimation a = ResourceCache.getAnimation(animation);
		setDrawable(a, unitsPerScreen);
	}

	public void setDrawable(AtlasAnimation animation, float unitsPerScreen) {
		if (mode == Mode.CLEANUP || animation == null)
			return;
		drawableAtlasAnimation = animation;
		mode = FancyDrawable.Mode.ATLAS_ANIMATION;
		setDrawable(animation.getKeyFrame(0.0f), unitsPerScreen);
	}

	public void setDrawable(ENinePatch ninePatch, Vector2 dimensions) {
		if (mode == Mode.CLEANUP)
			return;
		NinePatch np = ResourceCache.getNinePatch(ninePatch);
		if (np != null) {
			drawableNinePatch = np;
			dimensionsNinePatch.set(dimensions);
			mode = FancyDrawable.Mode.NINE_PATCH;
		}
	}

	public void setImage(EParticleEffect effect, float unitsPerScreen) {
		if (mode == Mode.CLEANUP)
			return;
		PooledEffect pe = ResourcePool.obtainParticleEffect(effect);
		if (pe != null) {
			resetScaleParticleEffect();
			ResourcePool.freeParticleEffect(drawablePooledEffect);
			drawablePooledEffect = pe;
			dimensionsSpriteUnitsPerScreen = unitsPerScreen;
			scaleParticleEffectLast = 1.0f;
			resize();
			scalePe = true;
			mode = Mode.PARTICLE_EFFECT;
		}
	}

	/**
	 * Called when drawable needs to be rendered.
	 */
	public void render() {
		switch (mode) {
		case ATLAS_ANIMATION:
			// no break
			// update updates the sprite to the current key frame
		case TEXTURE:
			drawableSprite.draw(batchPixel);
			break;
		case NINE_PATCH:
			float x = (position.x + 4.0f) * scaleScreenX;
			float y = (position.y + 4.5f) * scaleScreenY;
			float w = dimensionsNinePatch.x * scaleScreenX * scale.x;
			float h = dimensionsNinePatch.y * scaleScreenY * scale.y;
			drawableNinePatch.draw(batchPixel, x - w, y - h, w + w, h + h);
			break;
		case PARTICLE_EFFECT:
			drawablePooledEffect.draw(batchPixel);
			break;
		default:
			LOG.error("unknown drawable encountered during render: " + mode);
			break;
		}
	}

	/**
	 * Called when the game window is resized.
	 */
	public void resize() {
		scaleScreenX = viewportGame.getScreenWidth() / 8.0f;
		scaleScreenY = viewportGame.getScreenHeight() / 9.0f;
		scaleUnitsPerScreen = viewportGame.getScreenWidth() / (8.0f * dimensionsSpriteUnitsPerScreen);
		scalePe = true;
	}

	/**
	 * Called when this drawable gets updated.
	 * 
	 * @param deltaTime
	 * @param passedTime
	 * @return Whether this drawable is finished. Will always return true for
	 *         {@link ENinePatch}es and {@link ETexture}s, but not for
	 *         necessarily for {@link EAnimation}s or {@link EParticleEffect}s.
	 */
	public boolean update(float deltaTime, float passedTime) {
		boolean retVal = false;
		switch (mode) {
		case ATLAS_ANIMATION:
			drawableSprite.setAtlasRegion(drawableAtlasAnimation.getKeyFrame(passedTime));
			retVal = drawableAtlasAnimation.getPlayMode() == PlayMode.NORMAL
					&& drawableAtlasAnimation.isAnimationFinished(passedTime);
			// no break because the texture for the key frame needs to be
			// processed as well
		case TEXTURE:
			float scaleX = scaleUnitsPerScreen * scale.x;
			float scaleY = scaleUnitsPerScreen * scale.y;
			drawableSprite.setColor(color);
			drawableSprite.setAlpha(opacity);
			drawableSprite.setScale(scaleX, scaleY);
			drawableSprite.setOriginRelative(origin);
			drawableSprite.setPositionOrigin((position.x + 4.0f) * scaleScreenX, (position.y + 4.5f) * scaleScreenY);
			drawableSprite.setCrop(cropX, cropY) ;
			break;
		case NINE_PATCH:
			Color color = drawableNinePatch.getColor();
			color.set(color);
			color.a = opacity;
			drawableNinePatch.setColor(color);
			break;
		case PARTICLE_EFFECT:
			if (scalePe) {
				scalePe = false;
				setScaleParticleEffect();
			}
			if (positionPe) {
				positionPe = false;
				drawablePooledEffect.setPosition((position.x + 4.0f) * viewportGame.getScreenWidth() / 8.0f,
						(position.y + 4.5f) * viewportGame.getScreenHeight() / 9.0f);
			}
			drawablePooledEffect.update(deltaTime);
			retVal = drawablePooledEffect.isComplete();
			break;
		default:
			LOG.error("unknown drawable " + mode + " encountered during update: " + this);
			retVal = true;
		}
		return retVal;
	}

	private void setScaleParticleEffect() {
		float newScale = 0.5f * Math.max(scaleUnitsPerScreen * (scale.x + scale.y), 0.2f);
		drawablePooledEffect.scaleEffect(newScale / scaleParticleEffectLast);
		scaleParticleEffectLast = newScale;
	}

	private void resetScaleParticleEffect() {
		float newScale = 1.0f;
		drawablePooledEffect.scaleEffect(newScale / scaleParticleEffectLast);
		scaleParticleEffectLast = newScale;
	}

	/**
	 * Called when this drawable is not needed any longer.
	 */
	public void cleanup() {
		if (drawablePooledEffect != null) {
			resetScaleParticleEffect();
			ResourcePool.freeParticleEffect(drawablePooledEffect);
		}
		mode = Mode.CLEANUP;
	}

	public final GetVector2 getPosition = new GetVector2() {
		@Override
		public void as(Vector2 target) {
			target.set(position);
		}
	};
	public final GetVector2 getScale = new GetVector2() {
		@Override
		public void as(Vector2 target) {
			target.set(scale);
		}
	};
	public final GetVector2 getCropX = new GetVector2() {
		@Override
		public void as(Vector2 target) {
			target.set(cropX);
		}
	};
	public final GetVector2 getCropY = new GetVector2() {
		@Override
		public void as(Vector2 target) {
			target.set(cropY);
		}
	};
	public final GetVector2 getOrigin = new GetVector2() {
		@Override
		public void as(Vector2 target) {
			target.set(origin);
		}
	};
	public final GetColor getColor = new GetColor() {
		@Override
		public void as(Color target) {
			target.set(color);
		}
	};

}