package de.homelab.madgaksha.lotsofbs.entityengine.entitysystem;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.player;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.lotsofbs.GlobalBag;
import de.homelab.madgaksha.lotsofbs.KeyMapDesktop;
import de.homelab.madgaksha.lotsofbs.audiosystem.SoundPlayer;
import de.homelab.madgaksha.lotsofbs.entityengine.DefaultPriority;
import de.homelab.madgaksha.lotsofbs.entityengine.Mapper;
import de.homelab.madgaksha.lotsofbs.entityengine.component.ConeDistributionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.PositionComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.TemporalComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entity.ItemMaker;
import de.homelab.madgaksha.lotsofbs.entityengine.entityutils.ComponentUtils;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ESound;

/**
 * Updates velocity from the force applied to a component and its mass.
 * 
 * @author madgaksha
 *
 */
public class InputDesktopSystem extends IteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(InputDesktopSystem.class);
	private static Vector2 v = new Vector2();
	private static Vector2 w = new Vector2();

	public InputDesktopSystem() {
		this(DefaultPriority.inputPlayerDesktopSystem);
	}

	@SuppressWarnings("unchecked")
	public InputDesktopSystem(int priority) {
		super(Family.all(VelocityComponent.class, InputDesktopComponent.class, DirectionComponent.class).get(),
				priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		final InputDesktopComponent ic = Mapper.inputDesktopComponent.get(entity);
		final DirectionComponent dc = Mapper.directionComponent.get(entity);

		// Switch active item.
		if (KeyMapDesktop.isActiveItemSwitchJustPressed()) {
			ConeDistributionComponent cdc = Mapper.coneDistributionComponent.get(playerHitCircleEntity);
			ComponentUtils.cycleConeDistributionForward(cdc);
		}
		
		// Use active item.
		if (KeyMapDesktop.isActiveItemUseJustPressed()) {
			ItemMaker.useActiveConsumable();
		}
		
		// Switch weapon
		if (KeyMapDesktop.isWeaponSwitchJustPressed()) {
			if (player.cycleWeaponForward()) {
				SoundPlayer.getInstance().play(ESound.EQUIP_WEAPON);
			} else
				SoundPlayer.getInstance().play(ESound.CANNOT_USE);
		} else if (KeyMapDesktop.isTokugiSwitchJustPressed()) {
			if (player.cycleTokugiForward()) {
				SoundPlayer.getInstance().play(ESound.EQUIP_TOKUGI);
			} else
				SoundPlayer.getInstance().play(ESound.CANNOT_USE);
		}

		// Get direction vector from pressed arrow keys.
		v.set((KeyMapDesktop.isPlayerMoveRightPressed()) ? 1.0f
				: (KeyMapDesktop.isPlayerMoveLeftPressed()) ? -1.0f : 0.0f,
				(KeyMapDesktop.isPlayerMoveUpPressed()) ? 1.0f
						: (KeyMapDesktop.isPlayerMoveDownPressed()) ? -1.0f : 0.0f);

		// Adjust direction vector relative to the camera direction.
		if (ic.relativeToCamera)
			v.rotate(-viewportGame.getRotationUpXY());

		// During battle mode, do not apply any friction or acceleration as
		// maximum responsiveness is needed to dodge bullets.
		if (battleModeActive) {
			final float f = KeyMapDesktop.isSpeedupPressed() ? ic.battleSpeedHigh : ic.battleSpeedLow;
			vc.x = v.x * f;
			vc.y = v.y * f;
			final PositionComponent pcEnemy = Mapper.positionComponent
					.get(GlobalBag.cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex));
			final PositionComponent pcPlayer = Mapper.positionComponent.get(entity);
			dc.degree = 450.0f - w.set(pcPlayer.x - pcEnemy.x, pcPlayer.y - pcEnemy.y).angle();
		} else {
			final float f = KeyMapDesktop.isSpeedupPressed() ? ic.accelerationFactorHigh : ic.accelerationFactorLow;
			vc.x = (vc.x + f * v.x) * ic.frictionFactor;
			vc.y = (vc.y + f * v.y) * ic.frictionFactor;
			if (v.x * v.y != 0.0f)
				dc.degree = 630.0f - w.set(vc.x, vc.y).angle() + viewportGame.getRotationUpXY();
		}

		// Make random generator a bit more unpredictable.
		if (v.x * v.y > 0.0f)
			MathUtils.random.nextInt();

		// Check if we need to switch the targetted enemy and change
		// the info displayed on the status screen.
		if (cameraTrackingComponent.focusPoints.size() > 1) {
			if (KeyMapDesktop.isEnemyPrevJustPressed()) {
				SoundPlayer.getInstance().play(ESound.ENEMY_SWITCH);
				cameraTrackingComponent.trackedPointIndex++;
				if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
					cameraTrackingComponent.trackedPointIndex = 0;
				}
				EnemyMaker.targetSwitched(
						cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex), true);
			} else if (KeyMapDesktop.isEnemyNextJustPressed()) {
				SoundPlayer.getInstance().play(ESound.ENEMY_SWITCH);
				cameraTrackingComponent.trackedPointIndex--;
				if (cameraTrackingComponent.trackedPointIndex < 0) {
					cameraTrackingComponent.trackedPointIndex = cameraTrackingComponent.focusPoints.size() - 1;
				}
				EnemyMaker.targetSwitched(
						cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex), true);
			}
		}

		// Fire tokugi.
		if (battleModeActive && KeyMapDesktop.isTokugiFireJustPressed()) {
			TemporalComponent tc = Mapper.temporalComponent.get(entity);
			player.getEquippedTokugi().fire(playerEntity, tc.deltaTime);
		}
	}
}
