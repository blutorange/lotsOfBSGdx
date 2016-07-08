package de.homelab.madgaksha.cutscenesystem.fancyscene.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.util.INewObject;

public class DrawableNinePatch extends ADrawable<ENinePatch, NinePatch> implements INewObject<DrawableNinePatch> {
	private final static Logger LOG = Logger.getLogger(DrawableNinePatch.class);
	private final Color c = new Color();
	private boolean dirty = false;
	private float x, y, w, h;
	
	@Override
	protected void applyColor(float r, float g, float b, float a) {
		getColor(c);
		c.set(r, g, b, a);
		drawable.setColor(c);
	}

	@Override
	protected void applyCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
		LOG.error("crop not supported for nine patches");
	}

	@Override
	protected void applyOpacity(float opacity) {
		drawable.getColor().a = opacity;
	}

	@Override
	protected void applyOrigin(float originX, float originY) {
		dirty = true;
	}

	@Override
	protected void applyPosition(float positionX, float positionY) {
		dirty = true;
	}

	@Override
	protected void applyRotation(float degree) {
		LOG.error("rotation not supported for nine patches currently");
	}

	@Override
	protected void applyScale(float scaleX, float scaleY) {
		dirty = true;
	}

	@Override
	protected NinePatch loadResource(ENinePatch resource) {
		return ResourceCache.getNinePatch(resource);
	}

	@Override
	protected void performDispose() {
	}

	@Override
	protected void performRender(Batch batch) {
		if (dirty) computeCoordinates();
		drawable.draw(batch, x, y, w, h);
	}

	@Override
	protected boolean performUpdate(float deltaTime, float passedTime) {
		return true;
	}
	
	private void computeCoordinates() {
		dirty = false;
		w = getScaleX();
		h = getScaleY();
		x = getPositionX() - getOriginX() * w;
		y = getPositionY() - getOriginY() * h;
	}

	@Override
	public DrawableNinePatch newObject() {
		return new DrawableNinePatch();
	}
}
