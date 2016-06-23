package de.homelab.madgaksha.entityengine.entitysystem;

import static de.homelab.madgaksha.GlobalBag.battleModeActive;
import static de.homelab.madgaksha.GlobalBag.cameraTrackingComponent;
import static de.homelab.madgaksha.GlobalBag.player;
import static de.homelab.madgaksha.GlobalBag.viewportGame;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.homelab.madgaksha.GlobalBag;
import de.homelab.madgaksha.KeyMapDesktop;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.entityengine.DefaultPriority;
import de.homelab.madgaksha.entityengine.Mapper;
import de.homelab.madgaksha.entityengine.component.DirectionComponent;
import de.homelab.madgaksha.entityengine.component.InputDesktopComponent;
import de.homelab.madgaksha.entityengine.component.PositionComponent;
import de.homelab.madgaksha.entityengine.component.VelocityComponent;
import de.homelab.madgaksha.entityengine.entity.EnemyMaker;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;

/**
 * Updates velocity from the force applied to a component and its mass.
 * 
 * @author madgaksha
 *
 */
public class InputPlayerDesktopSystem extends IteratingSystem {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(InputPlayerDesktopSystem.class);
	private static Vector2 v = new Vector2();
	private static Vector2 w = new Vector2();

	public InputPlayerDesktopSystem() {
		this(DefaultPriority.inputPlayerDesktopSystem);
	}

	@SuppressWarnings("unchecked")
	public InputPlayerDesktopSystem(int priority) {
		super(Family.all(VelocityComponent.class, InputDesktopComponent.class, DirectionComponent.class).get(),
				priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final VelocityComponent vc = Mapper.velocityComponent.get(entity);
		final InputDesktopComponent ic = Mapper.inputDesktopComponent.get(entity);
		final DirectionComponent dc = Mapper.directionComponent.get(entity);

		// Switch weapon
		if (KeyMapDesktop.isWeaponSwitchJustPressed()) {
			if (player.cycleWeaponForward()) {
				SoundPlayer.getInstance().play(ESound.EQUIP_WEAPON);
			} else
				SoundPlayer.getInstance().play(ESound.CANNOT_EQUIP);
		} else if (KeyMapDesktop.isTokugiSwitchJustPressed()) {
			if (player.cycleTokugiForward()) {
				SoundPlayer.getInstance().play(ESound.EQUIP_TOKUGI);
			} else
				SoundPlayer.getInstance().play(ESound.CANNOT_EQUIP);
		}

		// Arrow keys direction.
		v.set((Gdx.input.isKeyPressed(ic.right)) ? 1.0f : (Gdx.input.isKeyPressed(ic.left)) ? -1.0f : 0.0f,
				(Gdx.input.isKeyPressed(ic.up)) ? 1.0f : (Gdx.input.isKeyPressed(ic.down)) ? -1.0f : 0.0f);

		if (ic.relativeToCamera)
			v.rotate(-viewportGame.getRotationUpXY());

		if (battleModeActive) {
			final float f = Gdx.input.isKeyPressed(ic.speedTrigger) ? ic.battleSpeedHigh : ic.battleSpeedLow;
			vc.x = v.x * f;
			vc.y = v.y * f;
			final PositionComponent pcEnemy = Mapper.positionComponent
					.get(GlobalBag.cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex));
			final PositionComponent pcPlayer = Mapper.positionComponent.get(entity);
			dc.degree = 450.0f - w.set(pcPlayer.x - pcEnemy.x, pcPlayer.y - pcEnemy.y).angle();
		} else {
			final float f = Gdx.input.isKeyPressed(ic.speedTrigger) ? ic.accelerationFactorHigh
					: ic.accelerationFactorLow;
			vc.x = (vc.x + f * v.x) * ic.frictionFactor;
			vc.y = (vc.y + f * v.y) * ic.frictionFactor;
			if (v.x * v.y != 0.0f)
				dc.degree = 630.0f - w.set(vc.x, vc.y).angle() + viewportGame.getRotationUpXY();
		}

		// Check if we need to switch the targetted enemy and change
		// the info displayed on the status screen.
		if (cameraTrackingComponent.focusPoints.size() > 1) {
			if (Gdx.input.isKeyJustPressed(ic.enemySwitcherPrev)) {
				SoundPlayer.getInstance().play(ESound.ENEMY_SWITCH);
				cameraTrackingComponent.trackedPointIndex++;
				if (cameraTrackingComponent.trackedPointIndex >= cameraTrackingComponent.focusPoints.size()) {
					cameraTrackingComponent.trackedPointIndex = 0;
				}
				EnemyMaker.targetSwitched(
						cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex), true);
			} else if (Gdx.input.isKeyJustPressed(ic.enemySwitcherNext)) {
				SoundPlayer.getInstance().play(ESound.ENEMY_SWITCH);
				cameraTrackingComponent.trackedPointIndex--;
				if (cameraTrackingComponent.trackedPointIndex < 0) {
					cameraTrackingComponent.trackedPointIndex = cameraTrackingComponent.focusPoints.size() - 1;
				}
				EnemyMaker.targetSwitched(
						cameraTrackingComponent.focusPoints.get(cameraTrackingComponent.trackedPointIndex), true);
			}
		}
	}
}
