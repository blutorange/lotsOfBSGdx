package de.homelab.madgaksha.entityengine.entity;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.entityengine.ETrigger;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.AnimationForDirectionComponent;
import de.homelab.madgaksha.entityengine.component.AnimationModeListComponent;
import de.homelab.madgaksha.entityengine.component.BattleDistanceComponent;
import de.homelab.madgaksha.entityengine.component.BehaviourComponent;
import de.homelab.madgaksha.entityengine.component.CameraFocusComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.RotationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteAnimationComponent;
import de.homelab.madgaksha.entityengine.component.SpriteComponent;
import de.homelab.madgaksha.enums.ESpriteDirectionStrategy;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EAnimationList;

/**
 * Maker for most enemies with normal and common properties sprites, direction,
 * movement etc.
 * 
 * @author madgaksha
 *
 */
public abstract class NormalEnemyMaker extends EnemyMaker {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(NormalEnemyMaker.class);

	protected NormalEnemyMaker() {
		super();
	}

	@Override
	public void setup(Entity e, Shape2D shape, MapProperties props, ETrigger spawn, Vector2 initialPos, Float initDir,
			Float tileRadius) {
		super.setup(e, shape, props, spawn, initialPos, initDir, tileRadius);

		BehaviourComponent bc = new BehaviourComponent(BEHAVIOUR_BASICS);
		RotationComponent rc = new RotationComponent(true);
		AnimationModeListComponent amlc = new AnimationModeListComponent();
		AnimationForDirectionComponent afdc = new AnimationForDirectionComponent();
		SpriteAnimationComponent sac = new SpriteAnimationComponent();
		SpriteComponent sc = new SpriteComponent();

		bc.cortex = getBehaviour(props);
		amlc.setupNormal(requestedAnimationListNormal(), ESpriteDirectionStrategy.ZENITH, true);
		amlc.setupDamage(requestedAnimationListDamage(), ESpriteDirectionStrategy.ZENITH, true);
		afdc.setup(amlc.normal.animationList, amlc.normal.spriteDirectionStrategy);
		sac.setup(afdc);
		sc.setup(sac);

		e.add(bc);
		e.add(sc);
		e.add(sac);
		e.add(afdc);
		e.add(amlc);
		e.add(rc);
	}

	protected abstract IBehaving getBehaviour(MapProperties props);

	protected abstract EAnimationList requestedAnimationListNormal();

	protected abstract EAnimationList requestedAnimationListDamage();

	private final static IBehaving BEHAVIOUR_BASICS = new IBehaving() {
		@Override
		/**
		 * Callback for enemy behaviour. Aka the update method. Check if enemy
		 * is within the battle distance and deactivate enemy if it is not.
		 */
		public boolean behave(Entity enemy) {
			BattleDistanceComponent bdc = Mapper.battleDistanceComponent.get(enemy);
			PositionComponent pcEnemy = Mapper.positionComponent.get(enemy);
			PositionComponent pcOther = Mapper.positionComponent.get(bdc.relativeToEntity);
			float dr = (pcEnemy.x - pcOther.x) * (pcEnemy.x - pcOther.x)
					+ (pcEnemy.y - pcOther.y) * (pcEnemy.y - pcOther.y);
			if (dr > bdc.battleOutSquared) {
				if (Mapper.cameraFocusComponent.get(enemy) != null)
					enemy.remove(CameraFocusComponent.class);
				return false;
			}
			final CameraFocusComponent cfc = Mapper.cameraFocusComponent.get(enemy);
			if (cfc != null)
				return true;
			else if (dr < bdc.battleInSquared) {
				enemy.add(gameEntityEngine.createComponent(CameraFocusComponent.class));
				return true;
			}
			return false;
		}
	};

}