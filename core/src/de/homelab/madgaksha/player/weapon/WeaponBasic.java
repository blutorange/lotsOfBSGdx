package de.homelab.madgaksha.player.weapon;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.entity.BulletMaker;
import de.homelab.madgaksha.entityengine.entity.BulletShapeMaker;
import de.homelab.madgaksha.entityengine.entity.trajectory.LinearMotionTrajectory;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EModel;
import de.homelab.madgaksha.resourcecache.ETexture;
import de.homelab.madgaksha.resourcecache.IResource;

public class WeaponBasic extends AWeapon {

	private final LinearMotionTrajectory linearMotionTrajectory = new LinearMotionTrajectory();
	private final Vector2 v = new Vector2();
	private final static long BULLET_POWER = 939480L;
	private final static float BULLET_SPEED = 1200.0f;
	private final static float BULLET_LIFE = 1.0f;
	private final static float BULLET_INTERVAL = 0.4f;
	private float remainingTime = BULLET_INTERVAL;

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
			final Entity enemy = GlobalBag.cameraTrackingComponent.focusPoints
					.get(GlobalBag.cameraTrackingComponent.trackedPointIndex);
			final PositionComponent pcEnemy = Mapper.positionComponent.get(enemy);
			final PositionComponent pcPlayer = Mapper.positionComponent.get(player);
			v.set(pcEnemy.x - pcPlayer.x, pcEnemy.y - pcPlayer.y).nor().scl(BULLET_SPEED);
			linearMotionTrajectory.velocity(v.x, v.y);
			linearMotionTrajectory.life(BULLET_LIFE);
			final PositionComponent pc = Mapper.positionComponent.get(player);
			linearMotionTrajectory.position(pc.x, pc.y);
			BulletMaker.makeForPlayer(BulletShapeMaker.ORB_NOCOLOR, linearMotionTrajectory, BULLET_POWER);
		}
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource<?, ?>[] { BulletShapeMaker.ORB_NOCOLOR.getResource(), };
	}
}
