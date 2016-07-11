package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.homelab.madgaksha.lotsofbs.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxRenderComponent;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.util.GeoUtil;

/**
 * Enum for the different shapes of a bullet. Each bullet shape is defined by
 * its image and geometry.
 * 
 * @author madgaksha
 *
 */
public enum BulletShapeMaker {
	ORB_NOCOLOR(ETexture.BULLET_ORB_NOCOLOR, Box.ORB, null, BoxRender.ORB),

	PACMAN_BLACK(ETexture.BULLET_PACMAN_BLACK, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_RED(ETexture.BULLET_PACMAN_RED, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_LIGHTRED(ETexture.BULLET_PACMAN_LIGHTRED, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_PINK(ETexture.BULLET_PACMAN_PINK, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_LIGHTPINK(ETexture.BULLET_PACMAN_LIGHTPINK, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_BLUE(ETexture.BULLET_PACMAN_BLUE, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_LIGHTBLUE(ETexture.BULLET_PACMAN_LIGHTBLUE, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_CYAN(ETexture.BULLET_PACMAN_CYAN, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_LIGHTCYAN(ETexture.BULLET_PACMAN_LIGHTCYAN, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_GREEN(ETexture.BULLET_PACMAN_GREEN, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_LIGHTGREEN(ETexture.BULLET_PACMAN_LIGHTGREEN, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_YELLOW(ETexture.BULLET_PACMAN_YELLOW, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_LIGHTYELLOW(ETexture.BULLET_PACMAN_LIGHTYELLOW, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_ORANGE(ETexture.BULLET_PACMAN_ORANGE, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_LIGHTORANGE(ETexture.BULLET_PACMAN_LIGHTORANGE, Box.PACMAN, null, BoxRender.PACMAN, 150L),
	PACMAN_WHITE(ETexture.BULLET_PACMAN_WHITE, Box.PACMAN, null, BoxRender.PACMAN, 150L),

	STAR_BLACK(ETexture.BULLET_STAR_BLACK, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_RED(ETexture.BULLET_STAR_RED, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_LIGHTRED(ETexture.BULLET_STAR_LIGHTRED, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_PINK(ETexture.BULLET_STAR_PINK, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_LIGHTPINK(ETexture.BULLET_STAR_LIGHTPINK, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_BLUE(ETexture.BULLET_STAR_BLUE, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_LIGHTBLUE(ETexture.BULLET_STAR_LIGHTBLUE, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_CYAN(ETexture.BULLET_STAR_CYAN, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_LIGHTCYAN(ETexture.BULLET_STAR_LIGHTCYAN, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_GREEN(ETexture.BULLET_STAR_GREEN, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_LIGHTGREEN(ETexture.BULLET_STAR_LIGHTGREEN, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_YELLOW(ETexture.BULLET_STAR_YELLOW, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_LIGHTYELLOW(ETexture.BULLET_STAR_LIGHTYELLOW, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_ORANGE(ETexture.BULLET_STAR_ORANGE, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_LIGHTORANGE(ETexture.BULLET_STAR_LIGHTORANGE, Box.STAR, null, BoxRender.STAR, 150L),
	STAR_WHITE(ETexture.BULLET_STAR_WHITE, Box.STAR, null, BoxRender.STAR, 150L),

	FLOWER_BLACK(ETexture.BULLET_FLOWER_BLACK, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),
	FLOWER_RED(ETexture.BULLET_FLOWER_RED, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),
	FLOWER_PINK(ETexture.BULLET_FLOWER_PINK, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),
	FLOWER_BLUE(ETexture.BULLET_FLOWER_BLUE, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),
	FLOWER_CYAN(ETexture.BULLET_FLOWER_CYAN, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),
	FLOWER_GREEN(ETexture.BULLET_FLOWER_GREEN, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),
	FLOWER_YELLOW(ETexture.BULLET_FLOWER_YELLOW, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),
	FLOWER_WHITE(ETexture.BULLET_FLOWER_WHITE, Box.FLOWER, Shape.FLOWER, BoxRender.FLOWER, 500L),

	NOTE_RED(EAnimation.NOTE_RED, Box.NOTE, null, BoxRender.NOTE, 1000L),
	NOTE_BLUE(EAnimation.NOTE_BLUE, Box.NOTE, null, BoxRender.NOTE, 1000L),
	NOTE_YELLOW(EAnimation.NOTE_YELLOW, Box.NOTE, null, BoxRender.NOTE, 1000L),
	NOTE_PINK(EAnimation.NOTE_PINK, Box.NOTE, null, BoxRender.NOTE, 1000L),

	PLASMA_RED(ETexture.BULLET_PLASMA_RED, Box.PLASMA, Shape.PLASMA, BoxRender.PLASMA, 2000L),
	PLASMA_BLUE(ETexture.BULLET_PLASMA_BLUE, Box.PLASMA, Shape.PLASMA, BoxRender.PLASMA, 2000L),
	PLASMA_GREEN(ETexture.BULLET_PLASMA_GREEN, Box.PLASMA, Shape.PLASMA, BoxRender.PLASMA, 2000L),
	PLASMA_YELLOW(ETexture.BULLET_PLASMA_YELLOW, Box.PLASMA, Shape.PLASMA, BoxRender.PLASMA, 2000L),

	// Score bullets.
	GEMLET_BROWN(ETexture.BULLET_GEMLET_BROWN, Box.GEMLET, null, BoxRender.GEMLET),
	GEMLET_BLUE(ETexture.BULLET_GEMLET_BLUE, Box.GEMLET, null, BoxRender.GEMLET),
	GEMLET_RED(ETexture.BULLET_GEMLET_RED, Box.GEMLET, null, BoxRender.GEMLET),
	GEMLET_GREEN(ETexture.BULLET_GEMLET_GREEN, Box.GEMLET, null, BoxRender.GEMLET),;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BulletShapeMaker.class);

	private CroppableAtlasSprite sprite;
	private AtlasAnimation animation;
	private final IResource<? extends Enum<?>, ?> resource;
	private final Rectangle boundingBoxCollision;
	private final Rectangle boundingBoxRender;
	private final Shape2D exactShape;
	public final long score;

	/**
	 * @param texture
	 *            The bullet's image.
	 * @param boundingBox
	 *            The bullet's bounding box. Position is relative to the
	 *            texture's center.
	 * @param exactShape
	 *            Exact shape of the bullet. May be null if bounding box is
	 *            sufficient. Position is relative to the texture's center.
	 */
	private BulletShapeMaker(ETexture texture, Rectangle boundingBoxCollision, Shape2D exactShape,
			Rectangle boundingBoxRender) {
		this(texture, boundingBoxCollision, exactShape, boundingBoxRender, 0L);
	}

	/**
	 * 
	 * @param texture
	 *            The bullet's image.
	 * @param boundingBox
	 *            The bullet's bounding box. Position is relative to the
	 *            texture's center.
	 * @param exactShape
	 *            Exact shape of the bullet. May be null if bounding box is
	 *            sufficient. Position is relative to the texture's center.
	 * @param score
	 *            Score this bullet is worth.
	 */
	private BulletShapeMaker(ETexture texture, Rectangle boundingBoxCollision, Shape2D exactShape,
			Rectangle boundingBoxRender, long score) {
		this.resource = texture;
		this.sprite = texture.asSprite();
		this.boundingBoxCollision = boundingBoxCollision;
		this.boundingBoxRender = boundingBoxRender;
		this.exactShape = exactShape;
		this.score = score;
	}

	private BulletShapeMaker(EAnimation animation, Rectangle boundingBoxCollision, Shape2D exactShape,
			Rectangle boundingBoxRender) {
		this(animation, boundingBoxCollision, exactShape, boundingBoxRender, 0L);
	}

	/**
	 * 
	 * @param texture
	 *            The bullet's animation.
	 * @param boundingBox
	 *            The bullet's bounding box. Position is relative to the
	 *            texture's center.
	 * @param exactShape
	 *            Exact shape of the bullet. May be null if bounding box is
	 *            sufficient. Position is relative to the texture's center.
	 * @param score
	 *            Score this bullet is worth.
	 */
	private BulletShapeMaker(EAnimation animation, Rectangle boundingBoxCollision, Shape2D exactShape,
			Rectangle boundingBoxRender, long score) {
		this.resource = animation;
		this.animation = ResourceCache.getAnimation(animation);
		this.boundingBoxCollision = boundingBoxCollision;
		this.boundingBoxRender = boundingBoxRender;
		this.exactShape = exactShape;
		this.score = score;
	}

	/**
	 * Adds the appropriate {@link Component}s for this bullet shape to the
	 * entity.
	 * 
	 * @param e
	 *            Entity to setup.
	 */
	public void setup(Entity e) {
		SpriteComponent sc = gameEntityEngine.createComponent(SpriteComponent.class);

		if (animation == null) {
			sc.setup(sprite);
		} else {
			SpriteAnimationComponent sac = gameEntityEngine.createComponent(SpriteAnimationComponent.class);
			sac.setup(animation);
			sc.setup(sac);
			e.add(sac);
		}

		BoundingBoxCollisionComponent bbcc = gameEntityEngine.createComponent(BoundingBoxCollisionComponent.class);
		BoundingBoxRenderComponent bbrc = gameEntityEngine.createComponent(BoundingBoxRenderComponent.class);
		bbcc.setup(boundingBoxCollision);
		bbrc.setup(boundingBoxRender);
		e.add(bbcc).add(bbrc).add(sc);

		if (exactShape != null) {
			ShapeComponent spc = gameEntityEngine.createComponent(ShapeComponent.class);
			spc.setup(GeoUtil.cloneShape(exactShape));
			e.add(spc);
		}
	}

	public IResource<? extends Enum<?>, ?> getResource() {
		return resource;
	}

	private final static class Box {
		private final static Rectangle FLOWER = new Rectangle(-31.0f, -29.0f, 62.0f, 58.0f);
		private final static Rectangle GEMLET = new Rectangle(-20.0f, -20f, 40.0f, 40.0f);
		private final static Rectangle NOTE = new Rectangle(-25.0f, -30.0f, 50.0f, 60.0f);
		private final static Rectangle ORB = new Rectangle(-13.0f, -13.0f, 13.0f, 13.0f);
		private final static Rectangle PACMAN = new Rectangle(-7.0f, -7.0f, 14.0f, 14.0f);
		private final static Rectangle PLASMA = new Rectangle(-62.0f, -62.0f, 124.0f, 124.0f);
		private final static Rectangle STAR = new Rectangle(-7.0f, -7.0f, 14.0f, 14.0f);
	}

	private final static class BoxRender {
		private final static Rectangle FLOWER = new Rectangle(-35.0f, -35.0f, 70.0f, 70.0f);
		private final static Rectangle GEMLET = new Rectangle(-25.0f, -25f, 50.0f, 50.0f);
		private final static Rectangle NOTE = new Rectangle(-35.0f, -35.0f, 70.0f, 70.0f);
		private final static Rectangle ORB = new Rectangle(-20.0f, -20.0f, 40.0f, 40.0f);
		private final static Rectangle PACMAN = new Rectangle(-10.0f, -10.0f, 20.0f, 20.0f);
		private final static Rectangle PLASMA = new Rectangle(-70.0f, -70.0f, 140.0f, 140.0f);
		private final static Rectangle STAR = new Rectangle(-10.0f, -10.0f, 20.0f, 20.0f);
	}

	private final static class Shape {
		private final static Shape2D FLOWER = new Circle(0.0f, 0.0f, 30.0f);
		private final static Shape2D PLASMA = new Circle(0.0f, 0.0f, 62.0f);
	}
}
