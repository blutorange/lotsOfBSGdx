package de.homelab.madgaksha.cutscenesystem.fancyscene.drawable;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import de.homelab.madgaksha.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimation;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcepool.ResourcePool;
import de.homelab.madgaksha.util.INewObject;

public class DrawableAnimation extends ADrawable<EAnimation, AtlasAnimation> implements INewObject<DrawableAnimation> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DrawableAnimation.class);
	private CroppableAtlasSprite sprite;
	private float unitPerPixel;

	@Override
	protected void applyColor(float r, float g, float b, float a) {
		sprite.setColor(r, g, b, a);
	}

	@Override
	protected void applyCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
		sprite.setCrop(cropLeft, cropRight, cropBottom, cropTop);
	}

	@Override
	protected void applyOpacity(float opacity) {
		sprite.setAlpha(opacity);
	}

	@Override
	protected void applyOrigin(float originX, float originY) {
		sprite.setOrigin(originX, originY);
	}

	@Override
	protected void applyPosition(float positionX, float positionY) {
		sprite.setPosition(positionX, positionY);
	}

	@Override
	protected void applyRotation(float degrees) {
		sprite.setRotation(degrees);
	}

	@Override
	protected void applyScale(float scaleX, float scaleY) {
		sprite.setScale(scaleX, scaleY);
	}

	@Override
	protected AtlasAnimation loadResource(EAnimation resource, float unitPerPixel) {
		final AtlasAnimation animation = ResourceCache.getAnimation(resource);
		if (animation != null) {
			this.unitPerPixel = unitPerPixel;
			sprite = ResourcePool.obtainSprite(animation.getKeyFrame(0.0f));
			sprite.setScale(sprite.getWidth()*unitPerPixel, sprite.getHeight()*unitPerPixel);
		}
		return animation;
	}

	@Override
	protected void performDispose() {
		ResourcePool.freeSprite(sprite);
		sprite = null;
	}

	@Override
	protected void performRender(Batch batch) {
		sprite.draw(batch);
	}

	@Override
	protected boolean performUpdate(float deltaTime, float passedTime) {
		final AtlasRegion region = drawable.getKeyFrame(passedTime);
		if (!sprite.isAtlasRegionEqualTo(region)) {
			sprite.setAtlasRegion(region);
			sprite.setSize(region.originalWidth * unitPerPixel, region.originalHeight * unitPerPixel);
			applyAll();
		}
		return drawable.getPlayMode() != PlayMode.NORMAL && drawable.getPlayMode() != PlayMode.REVERSED
				&& drawable.isAnimationFinished(passedTime);
	}

	@Override
	public DrawableAnimation newObject() {
		return new DrawableAnimation();
	}
}
