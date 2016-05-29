package de.homelab.madgaksha.entityengine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxRenderComponent;

/**
 * A bounding box. Subclassed for different types of bounding boxes used for
 * collision detection, restricting player to non-blocking tiles on the map etc. 
 * 
 * <br><br>
 * 
 * Bounding boxes are always relative to the position of the entity.
 * 
 * <br><br>
 * 
 * Consider the player entity to see how these bounding boxes are different:
 * 
 * <ul>
 * <li>{@link BoundingBoxRenderComponent} is a rectangle enclosing all pixels of the player's sprite.</li>
 * <li>{@link BoundingBoxCollisionComponent} is a rectangle with a width of 1 pixel at the player's center.</li>
 * <li>{@link BoundingBoxMapComponent} is a rectangle around the shadow of the player who is floating in air.</li>
 * </ul>
 * 
 * @author mad_gaksha
 */
public abstract class ABoundingBoxComponent implements Component, Poolable {
	private final static float DEFAULT_MIN_X = -0.5f;
	private final static float DEFAULT_MIN_Y = -0.5f;
	
	private final static float DEFAULT_MAX_X = 0.5f;
	private final static float DEFAULT_MAX_Y = 0.5f;
	
	public float minX = DEFAULT_MIN_X;
	public float minY = DEFAULT_MIN_Y;
	public float maxX = DEFAULT_MAX_X;
	public float maxY = DEFAULT_MAX_Y;
	
	public ABoundingBoxComponent() {
	}

	public ABoundingBoxComponent(Rectangle r) {
		setup(r);
	}
	public ABoundingBoxComponent(float minX, float minY, float maxX, float maxY) {
		setup(minX,minY,maxX,maxY);
	}

	public void setup(Rectangle r) {
		this.minX = r.x;
		this.minY = r.y;
		this.maxX = r.x + r.width;
		this.maxY = r.y + r.height;
	}
	public void setup(float minX, float minY, float maxX, float maxY) {
		this.minX= minX;
		this.minY= minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	
	@Override
	public void reset() {
		minX = DEFAULT_MIN_X;
		minY = DEFAULT_MIN_Y;
		maxX = DEFAULT_MAX_X;
		maxY = DEFAULT_MAX_Y;
	}

	@Override
	public String toString() {
		return "(" + minX + ", " + minY + ")-(" + maxX + ", " + maxY + ")";
	}
	
}
