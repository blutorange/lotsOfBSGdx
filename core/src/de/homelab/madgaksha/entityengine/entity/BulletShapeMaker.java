package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.resourcecache.ETexture;

/**
 * Enum for the different shapes of a bullet. Each bullet shape is defined by its image and geometry.
 * @author madgaksha
 *
 */
public enum BulletShapeMaker {
	PACMAN_LIGHTYELLOW(ETexture.BULLET_PACMAN_LIGHTYELLOW, new Rectangle(0,0,0,0), new Circle(0,0,0)),
	FLOWER_RED(ETexture.BULLET_FLOWER_RED, new Rectangle(0,0,0,0), new Circle(0,0,0)),
	;
	
	private Sprite sprite;
	private Rectangle boundingBox;
	private Shape2D exactShape;
	
	/**
	 * 
	 * @param texture The bullet's image.
	 * @param boundingBox The bullet's bounding box. Position is relative to the texture's center.
	 * @param exactShape Exact shape of the bullet. May be null if bounding box is sufficient. Position is relative to the texture's center. 
	 */
	private BulletShapeMaker(ETexture texture, Rectangle boundingBox, Shape2D exactShape) {
		this.sprite = texture.asSprite();
		this.boundingBox = boundingBox;
		this.exactShape = exactShape;
	}
	
	/**
	 * Adds the appropriate {@link Component}s for this bullet shape to the entity.
	 * @param e Entity to setup.
	 */
	public void setup(Entity e) {
		SpriteComponent sc = gameEntityEngine.createComponent(SpriteComponent.class);
		BoundingBoxComponent bbc = gameEntityEngine.createComponent(BoundingBoxComponent.class);
		
		sc.setup(sprite);
		bbc.setup(boundingBox);
		
		e.add(bbc);
		e.add(sc);
	}
}
