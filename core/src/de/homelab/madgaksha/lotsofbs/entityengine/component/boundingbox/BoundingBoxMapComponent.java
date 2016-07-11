package de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox;

import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.lotsofbs.entityengine.component.ABoundingBoxComponent;

/**
 * A bounding box component for restricting entities to non-blocking map tiles.
 * 
 * @see BoundingBoxComponent
 * @author mad_gaksha
 */
public class BoundingBoxMapComponent extends ABoundingBoxComponent {
	public BoundingBoxMapComponent() {
		super();
	}

	public BoundingBoxMapComponent(Rectangle r) {
		super(r);
	}

	public BoundingBoxMapComponent(float minX, float minY, float maxX, float maxY) {
		super(minX, minY, maxX, maxY);
	}
}
