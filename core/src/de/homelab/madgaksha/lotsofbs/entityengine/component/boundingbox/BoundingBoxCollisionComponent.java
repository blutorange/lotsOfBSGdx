package de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox;

import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ABoundingBoxComponent;

/**
 * A bounding box for colliding one entity with another.
 * 
 * @see BoundingBoxComponent
 * @author mad_gaksha
 */
public class BoundingBoxCollisionComponent extends ABoundingBoxComponent {
	public BoundingBoxCollisionComponent() {
		super();
	}

	public BoundingBoxCollisionComponent(Rectangle r) {
		super(r);
	}

	public BoundingBoxCollisionComponent(float minX, float minY, float maxX, float maxY) {
		super(minX, minY, maxX, maxY);
	}

	public BoundingBoxCollisionComponent(BoundingBoxCollisionComponent bbcc) {
		super(bbcc);
	}

	public BoundingBoxCollisionComponent(BoundingBoxRenderComponent bbrc) {
		super(bbrc);
	}

	public BoundingBoxCollisionComponent(BoundingBoxMapComponent bbmc) {
		super(bbmc);
	}
}
