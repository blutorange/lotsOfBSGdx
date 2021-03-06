package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.visibleWorldBoundingBox;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.ETrigger;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerScreenComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup02Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup03Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup04Component;
import de.homelab.madgaksha.lotsofbs.entityengine.component.collision.TriggerTouchGroup05Component;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.CallbackMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.lotsofbs.enums.EShapeType;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.util.GeoUtil;

/**
 * <table>
 * <tr>
 * <th>Group</th>
 * <th>{@link TriggerTouchComponent}</th>
 * <th>{@link ReceiveTouchComponent}</th>
 * </tr>
 * <tr>
 * <td>01</td>
 * <td>Player Hit Circle Entity</td>
 * <td>Enemy Bullet, MapEvents ({@link CallbackMaker}, {@link ItemMaker})</td>
 * </tr>
 * <tr>
 * <td>02</td>
 * <td>Enemy</td>
 * <td>Player Bullets</td>
 * </tr>
 * <tr>
 * <td>03</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>04</td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>05</td>
 * <td></td>
 * <td></td>
 * </tr>
 * </table>
 *
 * @author madgaksha
 *
 */
public class CollisionSystem extends EntitySystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CollisionSystem.class);

	private Family familyTouch01 = null;
	private Family familyTouch02 = null;
	private Family familyTouch03 = null;
	private Family familyTouch04 = null;
	private Family familyTouch05 = null;

	private Family familyReceive01 = null;
	private Family familyReceive02 = null;
	private Family familyReceive03 = null;
	private Family familyReceive04 = null;
	private Family familyReceive05 = null;

	private ImmutableArray<Entity> entitiesTouch01;
	private ImmutableArray<Entity> entitiesTouch02;
	private ImmutableArray<Entity> entitiesTouch03;
	private ImmutableArray<Entity> entitiesTouch04;
	private ImmutableArray<Entity> entitiesTouch05;

	private ImmutableArray<Entity> entitiesReceive01;
	private ImmutableArray<Entity> entitiesReceive02;
	private ImmutableArray<Entity> entitiesReceive03;
	private ImmutableArray<Entity> entitiesReceive04;
	private ImmutableArray<Entity> entitiesReceive05;

	private Family familyScreen = null;
	private ImmutableArray<Entity> entitiesScreen;

	private final static PositionComponent nullPositionComponent = new PositionComponent(0f, 0f, 0f);

	public CollisionSystem() {
		this(DefaultPriority.collisionSystem);
	}

	public CollisionSystem(final int priority) {
		super(priority);
		this.familyTouch01 = Family
				.all(PositionComponent.class, TriggerTouchGroup01Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyTouch02 = Family
				.all(PositionComponent.class, TriggerTouchGroup02Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyTouch03 = Family
				.all(PositionComponent.class, TriggerTouchGroup03Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyTouch04 = Family
				.all(PositionComponent.class, TriggerTouchGroup04Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyTouch05 = Family
				.all(PositionComponent.class, TriggerTouchGroup05Component.class, BoundingBoxCollisionComponent.class)
				.get();

		this.familyReceive01 = Family
				.all(PositionComponent.class, ReceiveTouchGroup01Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyReceive02 = Family
				.all(PositionComponent.class, ReceiveTouchGroup02Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyReceive03 = Family
				.all(PositionComponent.class, ReceiveTouchGroup03Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyReceive04 = Family
				.all(PositionComponent.class, ReceiveTouchGroup04Component.class, BoundingBoxCollisionComponent.class)
				.get();
		this.familyReceive05 = Family
				.all(PositionComponent.class, ReceiveTouchGroup05Component.class, BoundingBoxCollisionComponent.class)
				.get();

		this.familyScreen = Family
				.all(TriggerScreenComponent.class, PositionComponent.class, BoundingBoxCollisionComponent.class).get();
	}

	@Override
	public void update(final float deltaTime) {
		TriggerTouchComponent ttc;
		ReceiveTouchComponent rtc;

		// Collide entities within the same group.

		for (final Entity alice : entitiesTouch01) {
			for (final Entity bob : entitiesReceive01) {
				ttc = Mapper.triggerTouchGroup01Component.get(alice);
				rtc = Mapper.receiveTouchGroup01Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (final Entity alice : entitiesTouch02) {
			for (final Entity bob : entitiesReceive02) {
				ttc = Mapper.triggerTouchGroup02Component.get(alice);
				rtc = Mapper.receiveTouchGroup02Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (final Entity alice : entitiesTouch03) {
			for (final Entity bob : entitiesReceive03) {
				ttc = Mapper.triggerTouchGroup03Component.get(alice);
				rtc = Mapper.receiveTouchGroup03Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (final Entity alice : entitiesTouch04) {
			for (final Entity bob : entitiesReceive04) {
				ttc = Mapper.triggerTouchGroup04Component.get(alice);
				rtc = Mapper.receiveTouchGroup04Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (final Entity alice : entitiesTouch05) {
			for (final Entity bob : entitiesReceive05) {
				ttc = Mapper.triggerTouchGroup05Component.get(alice);
				rtc = Mapper.receiveTouchGroup05Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}

		for (final Entity odo : entitiesScreen) {
			collideScreen(odo);
		}
	}

	@Override
	public void addedToEngine(final Engine engine) {
		entitiesTouch01 = engine.getEntitiesFor(familyTouch01);
		entitiesTouch02 = engine.getEntitiesFor(familyTouch02);
		entitiesTouch03 = engine.getEntitiesFor(familyTouch03);
		entitiesTouch04 = engine.getEntitiesFor(familyTouch04);
		entitiesTouch05 = engine.getEntitiesFor(familyTouch05);

		entitiesReceive01 = engine.getEntitiesFor(familyReceive01);
		entitiesReceive02 = engine.getEntitiesFor(familyReceive02);
		entitiesReceive03 = engine.getEntitiesFor(familyReceive03);
		entitiesReceive04 = engine.getEntitiesFor(familyReceive04);
		entitiesReceive05 = engine.getEntitiesFor(familyReceive05);

		entitiesScreen = engine.getEntitiesFor(familyScreen);
	}

	@Override
	public void removedFromEngine(final Engine engine) {
		entitiesTouch01 = null;
		entitiesTouch02 = null;
		entitiesTouch03 = null;
		entitiesTouch04 = null;
		entitiesTouch05 = null;

		entitiesReceive01 = null;
		entitiesReceive02 = null;
		entitiesReceive03 = null;
		entitiesReceive04 = null;
		entitiesReceive05 = null;
	}

	private static void collide(final Entity alice, final Entity bob, final TriggerTouchComponent ttc, final ReceiveTouchComponent rtc) {
		final PositionComponent pcAlice = Mapper.positionComponent.get(alice);
		final PositionComponent pcBob = Mapper.positionComponent.get(bob);
		final BoundingBoxCollisionComponent bbccAlice = Mapper.boundingBoxCollisionComponent.get(alice);
		final BoundingBoxCollisionComponent bbccBob = Mapper.boundingBoxCollisionComponent.get(bob);
		final ShapeComponent scAlice = Mapper.shapeComponent.get(alice);
		final ShapeComponent scBob = Mapper.shapeComponent.get(bob);
		// Apply offset
		pcAlice.x += pcAlice.offsetX;
		pcAlice.y += pcAlice.offsetY;
		pcBob.x += pcBob.offsetX;
		pcBob.y += pcBob.offsetY;
		// Check whether bounding boxes collide
		if (bbccAlice.maxX + pcAlice.x > bbccBob.minX + pcBob.x) {
			if (bbccAlice.minX + pcAlice.x < bbccBob.maxX + pcBob.x) {
				if (bbccAlice.maxY + pcAlice.y > bbccBob.minY + pcBob.y) {
					if (bbccAlice.minY + pcAlice.y < bbccBob.maxY + pcBob.y) {
						// test exact shape if asked to
						if (scAlice == null || scBob == null || GeoUtil.isCollision(scAlice.shape, scBob.shape,
								scAlice.shapeType, scBob.shapeType, pcAlice, pcBob)) {
							// Remove offset
							pcAlice.x -= pcAlice.offsetX;
							pcAlice.y -= pcAlice.offsetY;
							pcBob.x -= pcBob.offsetX;
							pcBob.y -= pcBob.offsetY;
							// Call callback.
							ttc.triggerAcceptingObject.callbackTrigger(alice, ETrigger.TOUCH);
							rtc.triggerReceivingObject.callbackTouched(bob, alice);
							// Apply offset (collisions are rare).
							pcAlice.x += pcAlice.offsetX;
							pcAlice.y += pcAlice.offsetY;
							pcBob.x += pcBob.offsetX;
							pcBob.y += pcBob.offsetY;
						}
					}
				}
			}
		}
		// Remove offset
		pcAlice.x -= pcAlice.offsetX;
		pcAlice.y -= pcAlice.offsetY;
		pcBob.x -= pcBob.offsetX;
		pcBob.y -= pcBob.offsetY;
	}

	private static void collideScreen(final Entity odo) {
		final PositionComponent pc = Mapper.positionComponent.get(odo);
		final BoundingBoxCollisionComponent bbcc = Mapper.boundingBoxCollisionComponent.get(odo);
		final ShapeComponent sc = Mapper.shapeComponent.get(odo);
		final TriggerScreenComponent tsc = Mapper.triggerScreenComponent.get(odo);
		// Apply offset
		pc.x += pc.offsetX;
		pc.y += pc.offsetY;
		// Check bounding box
		if (visibleWorldBoundingBox.x + visibleWorldBoundingBox.width > bbcc.minX + pc.x) {
			if (visibleWorldBoundingBox.x < bbcc.maxX + pc.x) {
				if (visibleWorldBoundingBox.y + visibleWorldBoundingBox.height > bbcc.minY + pc.y) {
					if (visibleWorldBoundingBox.y < bbcc.maxY + pc.y) {
						if (tsc.preciseCheck && sc != null) {
							// Check exact shape.
							if (GeoUtil.isCollision(GlobalBag.visibleWorld, sc.shape, EShapeType.POLYGON, sc.shapeType,
									nullPositionComponent, pc)) {
								tsc.triggerAcceptingObject.callbackTrigger(odo, ETrigger.SCREEN);
							}
						} else
							tsc.triggerAcceptingObject.callbackTrigger(odo, ETrigger.SCREEN);
					}
				}
			}
		}
		// Remove offset
		pc.x -= pc.offsetX;
		pc.y -= pc.offsetY;
	}

}