package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.statusScreen;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.component.BoundingBoxComponent;
import de.homelab.madgaksha.entityengine.component.BoundingSphereComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.ComponentQueueComponent;
import de.homelab.madgaksha.entityengine.component.DamageQueueComponent;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.EnemyIconComponent;
import de.homelab.madgaksha.entityengine.component.InactiveComponent;
import de.homelab.madgaksha.entityengine.component.InvisibleComponent;
import de.homelab.madgaksha.entityengine.component.PainPointsComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.enums.ECollisionGroup;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;
import de.homelab.madgaksha.util.GeoUtil;

public abstract class EnemyMaker extends EntityMaker implements IBehaving, ITrigger, IReceive {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(EnemyMaker.class);
	
	/**
	 * Adds the appropriate components to an entity to be used as an enemy.
	 * @param entity
	 *            Entity to setup.
	 * @param shape
	 *            Shape of the entity. Enemy will be positioned at the center of
	 *            its bounding box. If an offset is given, it is added to the
	 *            center. When trigger type is set to touch, the enemy will be
	 *            triggered when the player touches the shape.
	 * @param trigger
	 *            How the enemy should be triggered.
	 * @param initialPosition
	 *            Offset to the initial position, which defaults to the center
	 *            of the bounding box of shape defined in the map.
	 * @param initDir
	 *            Initial looking direction.
	 */
	public void setup(Entity entity, Shape2D shape, ETrigger trigger, Vector2 initialPosition, Float initDir) {
		super.setup(entity);
		
		// Create components to be added.
		Component tc = MakerUtils.makeTrigger(this, this, trigger, ECollisionGroup.PLAYER_GROUP);
		PositionComponent pcTrigger = MakerUtils.makePositionAtCenter(shape);
		BoundingBoxComponent bbcEnemy = new BoundingBoxComponent(requestedBoundingBox());
		BoundingBoxComponent bbcTrigger = new BoundingBoxComponent(GeoUtil.getBoundingBox(shape));
		BoundingSphereComponent bsc = new BoundingSphereComponent(requestedBoundingCircle());
		PositionComponent pcEnemy = new PositionComponent(initialPosition.x + pcTrigger.x,
				initialPosition.y + pcTrigger.y);
		PainPointsComponent ppc = new PainPointsComponent(requestedMaxPainPoints());
		DamageQueueComponent dqc = new DamageQueueComponent();
		CameraFocusComponent cfc = new CameraFocusComponent();
		DirectionComponent dc = new DirectionComponent(initDir);
		InactiveComponent iac = new InactiveComponent();
		InvisibleComponent ivc = new InvisibleComponent();		
		TemporalComponent tpc = new TemporalComponent();
		ComponentQueueComponent cqc = new ComponentQueueComponent();
		EnemyIconComponent eic = new EnemyIconComponent(requestedIconMain().asSprite(), requestedIconSub().asSprite());
		
		// Initially, the bounding box is the area the player needs to
		// touch to spawn the enemy. When the enemy spawns, the bounding box
		// must be set to the actual bounding box of the enemy.
		// Bounding box needs to be relative to the enemy's origin.
		bbcTrigger.minX -= pcTrigger.x;
		bbcTrigger.minY -= pcTrigger.y;
		bbcTrigger.maxX -= pcTrigger.x;
		bbcTrigger.maxY -= pcTrigger.y;
		
		// Setup components to be changed once the enemy spawns.
		cqc.add.add(pcEnemy);
		cqc.add.add(bbcEnemy);
		cqc.add.add(bsc);
		cqc.remove.add(BoundingBoxComponent.class);
		cqc.remove.add(InactiveComponent.class);
		cqc.remove.add(InvisibleComponent.class);
		cqc.remove.add(tc.getClass());
		cqc.remove.add(PositionComponent.class);

		// Add components to entity.
		entity.add(tpc);
		entity.add(cfc);
		entity.add(dc);
		entity.add(iac);
		entity.add(ivc);
		entity.add(bbcTrigger);
		entity.add(pcTrigger);
		entity.add(ppc);
		entity.add(dqc);
		entity.add(eic);
		if (tc != null)	entity.add(tc);
	}
	
	@Override
	public void behave(Entity e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void callbackTrigger(Entity e, ETrigger t) {
		sinSpawn(e);
		spawned(e, t);
	}

	@Override
	public void callbackTouched(Entity e) {
		sinSpawn(e);
		spawned(e, ETrigger.TOUCH);
	}

	/**
	 * Callback for spawning the enemy.
	 */
	private void sinSpawn(Entity e) {
		ComponentUtils.applyComponentQueue(e);
		if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size())
			statusScreen.targetEnemy(e);
		// no need to target the targetted enemy again...
//		else {
//			Entity enemy = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);
//			statusScreen.targetEnemy(enemy);
//		}
	}
	
	// =====================
	//   Abstract methods
	// =====================
	protected abstract ETexture requestedIconMain();
	protected abstract ETexture requestedIconSub();

	protected abstract IResource<? extends Enum<?>, ?>[] requestedResources();

	protected abstract Rectangle requestedBoundingBox();

	protected abstract Circle requestedBoundingCircle();

	/** @return Enemy maximum pain points (pp). */
	protected abstract int requestedMaxPainPoints();
	
	protected abstract void spawned(Entity e, ETrigger t);
}
