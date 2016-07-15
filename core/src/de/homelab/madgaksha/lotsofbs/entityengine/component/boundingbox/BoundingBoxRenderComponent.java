package de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ModelComponent;

/**
 * A bounding box for deciding whether an entity is on-screen and should be
 * rendered.
 * 
 * @see BoundingBoxComponent
 * @author mad_gaksha
 */
public class BoundingBoxRenderComponent extends ABoundingBoxComponent {
	public BoundingBoxRenderComponent() {
		super();
	}

	public BoundingBoxRenderComponent(Rectangle r) {
		super(r);
	}

	public BoundingBoxRenderComponent(float minX, float minY, float maxX, float maxY) {
		super(minX, minY, maxX, maxY);
	}
	
	@Override
	public void setup(BoundingBox boundingBox) {
		final float halfMax = 0.5f * Math.max(Math.max(boundingBox.getWidth(), boundingBox.getWidth()),boundingBox.getDepth());
		this.minX = boundingBox.getCenterX() - halfMax;
		this.minY = boundingBox.getCenterY() - halfMax;
		this.maxX = boundingBox.getCenterX() + halfMax;
		this.maxY = boundingBox.getCenterY() + halfMax;
	}

	@Deprecated
	public BoundingBoxRenderComponent(ModelInstance mi) {
		super(mi);
	}

	@Deprecated
	public BoundingBoxRenderComponent(ModelComponent mc) {
		super(mc);
	}
}
