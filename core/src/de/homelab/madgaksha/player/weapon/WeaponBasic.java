package de.homelab.madgaksha.player.weapon;

import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.component.boundingbox.BoundingBoxCollisionComponent;
import de.homelab.madgaksha.entityengine.entity.BulletMaker;
import de.homelab.madgaksha.entityengine.entity.BulletShapeMaker;
import de.homelab.madgaksha.entityengine.entity.trajectory.HomingForceTrajectory;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EModel;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;

public class WeaponBasic extends AWeapon {

	private final static long BULLET_POWER = 9394L;
	private final static float BULLET_INITIAL_SPEED = 800.0f;
	private final static float BULLET_LIFE = 3.0f;
	private final static float BULLET_INTERVAL = 3.4f;
	private final static float BULLET_ANGULAR_SPEED = 900.0f;
	private final static float BULLET_ATTRACTION = 1.8f;
	private final static float BULLET_FRICTION = 0.2f;
	
	//private final LinearMotionTrajectory linearMotionTrajectory = new LinearMotionTrajectory();
	private final HomingForceTrajectory homingForceTrajectory = new HomingForceTrajectory();
	private final Vector2 v = new Vector2();			
	private float remainingTime = 0.0f;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(WeaponBasic.class);

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.WEAPON_BASIC_ICON_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.WEAPON_BASIC_ICON_SUB;
	}

	@Override
	public EModel getModel() {
		return EModel.ITEM_WEAPON_BASIC;
	}

	@Override
	public void fire(Entity player, float deltaTime) {
		remainingTime -= deltaTime;
		if (remainingTime <= 0.0f) {
			remainingTime = BULLET_INTERVAL;

			if (cameraTrackingComponent.focusPoints.size() < 1) return;
			
			final Entity enemy = GlobalBag.cameraTrackingComponent.focusPoints
					.get(GlobalBag.cameraTrackingComponent.trackedPointIndex);
			final PositionComponent pcEnemy = Mapper.positionComponent.get(enemy);
			final PositionComponent pcPlayer = Mapper.positionComponent.get(player);
			final VelocityComponent vcPlayer = Mapper.velocityComponent.get(player);		
			final BoundingBoxCollisionComponent bbcc = Mapper.boundingBoxCollisionComponent.get(enemy);
			
			v.set(pcEnemy.y - pcPlayer.y, pcPlayer.x - pcEnemy.x).nor().scl(BULLET_INITIAL_SPEED);
			
			homingForceTrajectory.angularSpeed(BULLET_ANGULAR_SPEED);
			homingForceTrajectory.velocity(vcPlayer.y + v.x, vcPlayer.y + v.y);
			homingForceTrajectory.life(BULLET_LIFE);
			homingForceTrajectory.target(pcEnemy);
			homingForceTrajectory.position(pcPlayer.x, pcPlayer.y);
			homingForceTrajectory.attraction(BULLET_ATTRACTION);
			homingForceTrajectory.friction(BULLET_FRICTION);
			homingForceTrajectory.absorptionRadius(0.5f * Math.min(bbcc.maxX-bbcc.minX,bbcc.maxY-bbcc.minY));
			
			// Create left and right bullet.
			BulletMaker.makeForPlayer(BulletShapeMaker.ORB_NOCOLOR, homingForceTrajectory, BULLET_POWER);
			homingForceTrajectory.velocity(vcPlayer.x -v.x, vcPlayer.y - v.y);
			BulletMaker.makeForPlayer(BulletShapeMaker.ORB_NOCOLOR, homingForceTrajectory, BULLET_POWER);
			
			// Play sound
			SoundPlayer.getInstance().play(ESound.WEAPON_BASIC_1);
		}
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource<?, ?>[] {
			BulletShapeMaker.ORB_NOCOLOR.getResource(),
			ESound.WEAPON_BASIC_1,
			};
	}
}
