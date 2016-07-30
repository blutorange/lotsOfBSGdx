package de.homelab.madgaksha.lotsofbs.entityengine.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.entityengine.component.AnimationForDirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.RotationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.zorder.ZOrder2Component;
import de.homelab.madgaksha.lotsofbs.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimation;
import de.homelab.madgaksha.lotsofbs.resourcecache.EAnimationList;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

public class NpcMaker extends EntityMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(NpcMaker.class);

	// Singleton
	private static class SingletonHolder {
		private static final NpcMaker INSTANCE = new NpcMaker();
	}

	public static NpcMaker getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private NpcMaker() {
		super();
	}

	public void setup(Entity entity, Shape2D shape, EAnimationList animationList, boolean initiallyInactive,
			Vector2 initialPosition, Float initDir) {

		setup(entity, shape, initiallyInactive, initialPosition, initDir);

		RotationComponent rc = new RotationComponent(true);
		AnimationForDirectionComponent sfdc = new AnimationForDirectionComponent(animationList,
				ESpriteDirectionStrategy.ZENITH);
		SpriteAnimationComponent sac = new SpriteAnimationComponent(sfdc);
		SpriteComponent sc = new SpriteComponent(sac);

		entity.add(rc);
		entity.add(sfdc);
		entity.add(sac);
		entity.add(sc);

	}

	//TODO
	/**
	 * @param entity
	 * @param shape
	 * @param animation
	 * @param initiallyInactive
	 * @param initialPosition
	 * @param initDir
	 */
	public void setup(Entity entity, Shape2D shape, EAnimation animation, boolean initiallyInactive,
			Vector2 initialPosition, Float initDir) {
	}

	/**
	 * Adds the appropriate components to an entity to be used as an NPC.
	 * 
	 * @param entity
	 *            Entity to setup.
	 * @param shape
	 *            Shape for the enitity. NPC will be positioned at the center of
	 *            its bounding box.
	 * @param initiallyInactive
	 *            Whether the NPC is active and visible when the game starts.
	 * @param initialPosition
	 *            Initial position of the NPC in tiles. Relative to the center
	 *            of the shape's bounding box.
	 * @param initDir
	 *            Initial looking direction of the NPC.
	 */
	private void setup(Entity entity, Shape2D shape, boolean initiallyInactive, Vector2 initialPosition,
			Float initDir) {
		super.setup(entity);

		// Create components to be added.
		PositionComponent pc = MakerUtils.makePositionAtCenter(shape);
		DirectionComponent dc = new DirectionComponent(initDir);
		TemporalComponent tpc = new TemporalComponent();
		ZOrder2Component zoc = new ZOrder2Component();

		// Setup components
		PositionComponent center = MakerUtils.makePositionAtCenter(shape);
		pc.setup(center.x + initialPosition.x, center.y + initialPosition.y);
		dc.setup(initDir);

		// Add components to entity.
		entity.add(pc);
		entity.add(dc);
		entity.add(tpc);
		entity.add(zoc);
		if (initiallyInactive) {
			entity.add(new InactiveComponent());
			entity.add(new InvisibleComponent());
		}
	}

	@Override
	protected IResource<? extends Enum<?>, ?>[] requestedResources() {
		return null;
	}
}
