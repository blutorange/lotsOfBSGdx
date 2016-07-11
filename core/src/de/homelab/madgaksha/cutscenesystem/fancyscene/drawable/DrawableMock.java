package de.homelab.madgaksha.cutscenesystem.fancyscene.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.util.INewObject;

public class DrawableMock extends ADrawable<Object, Object> implements INewObject<DrawableMock> {
	private final static Logger LOG = Logger.getLogger(DrawableMock.class);
	private final static Object mock = new Object();

	@Override
	protected void applyColor(float r, float g, float b, float a) {
	}

	@Override
	protected void applyCrop(float cropLeft, float cropRight, float cropBottom, float cropTop) {
	}

	@Override
	protected void applyOpacity(float opacity) {
	}

	@Override
	protected void applyOrigin(float originX, float originY) {
	}

	@Override
	protected void applyPosition(float positionX, float positionY) {
	}

	@Override
	protected void applyRotation(float degree) {
	}

	@Override
	protected void applyScale(float scaleX, float scaleY) {
	}

	@Override
	protected Object loadResource(Object resource, float unitPerPixel) {
		LOG.error("mock loadResource: " + this.hashCode());
		return mock;
	}

	@Override
	protected void performDispose() {
		LOG.error("mock performDispose: " + this.hashCode());
	}

	@Override
	protected void performRender(Batch batch) {
	}

	@Override
	protected boolean performUpdate(float deltaTime, float passedTime) {
		return true;
	}

	@Override
	public DrawableMock newObject() {
		LOG.error("mock newObject: " + this.hashCode());
		return new DrawableMock();
	}
}