package de.homelab.madgaksha.entityengine.component.boundingbox;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;

import de.homelab.madgaksha.entityengine.component.ABoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.ModelComponent;

/**
 * A bounding box for deciding whether an entity is on-screen and should be rendered.
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
	public BoundingBoxRenderComponent(ModelInstance mi) {
		super(mi);
	}
	public BoundingBoxRenderComponent(ModelComponent mc) {
		super(mc);
	}
}
