package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.resourcecache.ETexture;

/**
 * Enum for the different shapes of a bullet. Each bullet shape is defined by its image and geometry.
 * @author madgaksha
 *
 */
public enum BulletShapeMaker {
	PACMAN_LIGHTYELLOW(ETexture.BULLET_PACMAN_LIGHTYELLOW, new Rectangle(0,0,0,0), null),
	FLOWER_RED(ETexture.BULLET_FLOWER_RED, new Rectangle(-31.0f,-29.0f,62.0f,58.0f), new Circle(0.0f,0.0f,30.0f)),
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
		BoundingBoxCollisionComponent bbcc = gameEntityEngine.createComponent(BoundingBoxCollisionComponent.class);

		sc.setup(sprite);
		bbcc.setup(boundingBox);

		e
			.add(bbcc)
			.add(sc);
		
		if (exactShape != null) {
			ShapeComponent spc = gameEntityEngine.createComponent(ShapeComponent.class);
			spc.setup(exactShape);
			e.add(spc);
		}
	}
}
