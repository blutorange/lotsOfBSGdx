package de.homelab.madgaksha.cutscenesystem.fancyscene.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.homelab.madgaksha.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcepool.ResourcePool;
import de.homelab.madgaksha.util.INewObject;

public class DrawableSprite extends ADrawable<ETexture, CroppableAtlasSprite> implements INewObject<DrawableSprite> {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DrawableSprite.class);
	
	@Override
	protected void applyColor(float r, float g, float b, float a) {
		drawable.setColor(r, g, b, a);
	}

	@Override
	protected void applyCrop(float left, float right, float bottom, float top) {
		drawable.setCrop(left, right, bottom, top);
	}

	@Override
	public CroppableAtlasSprite loadResource(ETexture resource) {
		return resource.asSprite();
	}
	
	@Override
	protected void applyOpacity(float opacity) {
		drawable.setAlpha(opacity);
	}
	
	@Override
	protected void applyOrigin(float originX, float originY) {
		drawable.setOriginRelative(originX, originY);
	}

	@Override
	protected void applyPosition(float x, float y) {
		drawable.setPositionOrigin(x, y);
	}

	@Override
	protected void applyRotation(float degrees) {
		drawable.setRotation(degrees);
	}

	@Override
	protected void applyScale(float scaleX, float scaleY) {
		drawable.setScale(scaleX, scaleY);
	}

	@Override
	protected void performDispose() {
		ResourcePool.freeSprite(drawable);
	}

	@Override
	protected void performRender(Batch batch) {
		drawable.draw(batch);
	}

	@Override
	protected boolean performUpdate(float deltaTime, float passedTime) {
		return true;
	}

	@Override
	public DrawableSprite newObject() {
		return new DrawableSprite();
	}
}
