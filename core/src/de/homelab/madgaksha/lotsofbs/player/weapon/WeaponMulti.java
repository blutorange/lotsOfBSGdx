package de.homelab.madgaksha.lotsofbs.player.weapon;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.BulletShapeMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.trajectory.LinearMotionTrajectory;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EModel;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.IResource;

public class WeaponMulti extends AWeapon {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(WeaponMulti.class);

	private final static float BULLET_FORWARD_SPREAD_MIN = -22.0f;
	private final static float BULLET_FORWARD_SPREAD_MAX = 22.0f;
	private final static int BULLET_FORWARD_SPREAD_COUNT = 17;
	private final static long BULLET_POWER = 22432L;
	private final static float BULLET_INTERVAL_MIN = 0.4f;
	private final static float BULLET_INTERVAL_MAX = 1.2f;
	private final static float BULLET_LIFE = 3.0f;
	private final static float BULLET_ANGULAR_SPEED = 900.0f;
	private final static float BULLET_SPEED = 730.0f;
	private final static BulletShapeMaker bulletList[] = new BulletShapeMaker[] { BulletShapeMaker.STAR_BLUE,
			BulletShapeMaker.STAR_CYAN, BulletShapeMaker.STAR_GREEN, BulletShapeMaker.STAR_ORANGE,
			BulletShapeMaker.STAR_PINK, BulletShapeMaker.STAR_RED, BulletShapeMaker.STAR_YELLOW, };

	private float remainingTime = 0.0f;
	private Vector2 v = new Vector2();
	private LinearMotionTrajectory linearMotionTrajectory = new LinearMotionTrajectory();

	@Override
	public EModel getModel() {
		return EModel.ITEM_WEAPON_MULTI;
	}

	@Override
	public IResource<? extends Enum<?>, ?>[] requestedRequiredResources() {
		return new IResource<?, ?>[] { ETexture.WEAPON_MULTI_ICON_MAIN, ETexture.WEAPON_MULTI_ICON_SUB };
	}

	@Override
	protected ETexture requestedIconMain() {
		return ETexture.WEAPON_MULTI_ICON_MAIN;
	}

	@Override
	protected ETexture requestedIconSub() {
		return ETexture.WEAPON_MULTI_ICON_SUB;
	}

	@Override
	public void fire(Entity player, float deltaTime) {
		remainingTime -= deltaTime;
		if (remainingTime <= 0.0f) {
			remainingTime = MathUtils.random(BULLET_INTERVAL_MIN, BULLET_INTERVAL_MAX);

			if (cameraTrackingComponent.focusPoints.size() < 1)
				return;

			final Entity enemy = cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex);
			final PositionComponent pcEnemy = Mapper.positionComponent.get(enemy);
			final PositionComponent pcPlayer = Mapper.positionComponent.get(player);
			float deltaSpread = (BULLET_FORWARD_SPREAD_MAX - BULLET_FORWARD_SPREAD_MIN)
					/ (BULLET_FORWARD_SPREAD_COUNT - 1);

			v.set(pcEnemy.x - pcPlayer.x, pcEnemy.y - pcPlayer.y).nor().scl(BULLET_SPEED);

			linearMotionTrajectory.life(BULLET_LIFE);
			linearMotionTrajectory.position(pcPlayer.x, pcPlayer.y);
			linearMotionTrajectory.angularSpeed(BULLET_ANGULAR_SPEED);

			v.rotate(-90.0f);
			linearMotionTrajectory.velocity(v);
			BulletMaker.makeForPlayer(bulletList[MathUtils.random(bulletList.length - 1)], linearMotionTrajectory,
					BULLET_POWER);
			v.rotate(180.0f);
			linearMotionTrajectory.velocity(v);
			BulletMaker.makeForPlayer(bulletList[MathUtils.random(bulletList.length - 1)], linearMotionTrajectory,
					BULLET_POWER);
			v.rotate(-90.0f);

			// Create forward bullets.
			v.rotate(BULLET_FORWARD_SPREAD_MIN);
			for (int i = 0; i != BULLET_FORWARD_SPREAD_COUNT; ++i) {
				BulletShapeMaker bulletShape = bulletList[MathUtils.random(bulletList.length - 1)];
				v.rotate(deltaSpread);
				linearMotionTrajectory.velocity(v);
				BulletMaker.makeForPlayer(bulletShape, linearMotionTrajectory, BULLET_POWER);

			}
			// Play sound
			SoundPlayer.getInstance().play(ESound.WEAPON_BASIC_1);
		}
	}
}
