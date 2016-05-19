package de.homelab.madgaksha.entityengine.entitysystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.ReceiveTouchComponent;
import de.homelab.madgaksha.entityengine.component.ShapeComponent;
import de.homelab.madgaksha.entityengine.component.TriggerTouchComponent;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup03Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup04Component;
import de.homelab.madgaksha.entityengine.component.collision.ReceiveTouchGroup05Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup01Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup02Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup03Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup04Component;
import de.homelab.madgaksha.entityengine.component.collision.TriggerTouchGroup05Component;
import de.homelab.madgaksha.logging.Logger;


/**
 * <table>
 *  <tr>
 *   <th>Group</th>
 *   <th>{@link TriggerTouchComponent}</th>
 *   <th>{@link ReceiveTouchComponent}</th>
 *  </tr>
 *  <tr>
 *   <td>01</td>
 *   <td>Player</td>
 *   <td>Enemy, GeneralMapEvent</td>
 *  </tr>
 *  <tr>
 *   <td>02</td>
 *   <td></td>
 *   <td></td>
 *  </tr>  
 *  <tr>
 *   <td>03</td>
 *   <td></td>
 *   <td></td>
 *  </tr>  
 *  <tr>
 *   <td>04</td>
 *   <td></td>
 *   <td></td>
 *  </tr>  
 *  <tr>
 *   <td>05</td>
 *   <td></td>
 *   <td></td>
 *  </tr>  
 * </table>
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

	public CollisionSystem() {
		this(DefaultPriority.collisionSystem);
	}

	@SuppressWarnings("unchecked")
	public CollisionSystem(int priority) {
		super(priority);
		this.familyTouch01 = Family.all(PositionComponent.class, TriggerTouchGroup01Component.class).get();
		this.familyTouch02 = Family.all(PositionComponent.class, TriggerTouchGroup02Component.class).get();
		this.familyTouch03 = Family.all(PositionComponent.class, TriggerTouchGroup03Component.class).get();
		this.familyTouch04 = Family.all(PositionComponent.class, TriggerTouchGroup04Component.class).get();
		this.familyTouch05 = Family.all(PositionComponent.class, TriggerTouchGroup05Component.class).get();

		this.familyReceive01 = Family.all(PositionComponent.class, ReceiveTouchGroup01Component.class).get();
		this.familyReceive02 = Family.all(PositionComponent.class, ReceiveTouchGroup02Component.class).get();
		this.familyReceive03 = Family.all(PositionComponent.class, ReceiveTouchGroup03Component.class).get();
		this.familyReceive04 = Family.all(PositionComponent.class, ReceiveTouchGroup04Component.class).get();
		this.familyReceive05 = Family.all(PositionComponent.class, ReceiveTouchGroup05Component.class).get();
	}

	@Override
	public void update(float deltaTime) {
		TriggerTouchComponent ttc;
		ReceiveTouchComponent rtc;
		for (Entity alice : entitiesTouch01) {
			for (Entity bob : entitiesReceive01) {
				ttc = Mapper.triggerTouchGroup01Component.get(alice);
				rtc = Mapper.receiveTouchGroup01Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (Entity alice : entitiesTouch02) {
			for (Entity bob : entitiesReceive02) {
				ttc = Mapper.triggerTouchGroup02Component.get(alice);
				rtc = Mapper.receiveTouchGroup02Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (Entity alice : entitiesTouch03) {
			for (Entity bob : entitiesReceive03) {
				ttc = Mapper.triggerTouchGroup03Component.get(alice);
				rtc = Mapper.receiveTouchGroup03Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (Entity alice : entitiesTouch04) {
			for (Entity bob : entitiesReceive04) {
				ttc = Mapper.triggerTouchGroup04Component.get(alice);
				rtc = Mapper.receiveTouchGroup04Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
		for (Entity alice : entitiesTouch05) {
			for (Entity bob : entitiesReceive05) {
				ttc = Mapper.triggerTouchGroup05Component.get(alice);
				rtc = Mapper.receiveTouchGroup05Component.get(bob);
				collide(alice, bob, ttc, rtc);
			}
		}
	}

	@Override
	public void addedToEngine(Engine engine) {
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
	}

	@Override
	public void removedFromEngine(Engine engine) {
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

	private void collide(Entity alice, Entity bob, TriggerTouchComponent ttc, ReceiveTouchComponent rtc) {
		final PositionComponent pcAlice = Mapper.positionComponent.get(alice);
		final PositionComponent pcBob = Mapper.positionComponent.get(bob);
		final BoundingBoxComponent bbcAlice = Mapper.boundingBoxComponent.get(alice);
		final BoundingBoxComponent bbcBob = Mapper.boundingBoxComponent.get(bob);
		final ShapeComponent scAlice = Mapper.shapeComponent.get(alice);
		final ShapeComponent scBob = Mapper.shapeComponent.get(bob);
		// TODO actual collision checking
		if (true) {
			ttc.triggerAcceptingObject.callbackTouch(bob);
			rtc.triggerReceivingObject.callbackTouched(alice);
		}
	}
}