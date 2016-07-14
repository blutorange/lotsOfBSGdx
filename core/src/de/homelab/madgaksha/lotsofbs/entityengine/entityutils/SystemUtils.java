package de.homelab.madgaksha.lotsofbs.entityengine.entityutils;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.cameraEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.enemyTargetCrossEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.gameEntityEngine;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerBattleStigmaEntity;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.playerHitCircleEntity;

import com.badlogic.ashley.core.EntitySystem;

import de.homelab.madgaksha.lotsofbs.entityengine.component.DisableAllExceptTheseComponent;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.AiSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.CollisionSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ConeDistributionSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.ForceFieldSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.InputDesktopSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.LifeSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.MovementSystem;
import de.homelab.madgaksha.lotsofbs.entityengine.entitysystem.VelocityFieldSystem;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Utilities for working with an entity's pain points. Usually processed by an
 * appropriate entity system, this should be used sparingly.
 * 
 * @author madgaksha
 *
 */
public class SystemUtils {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SystemUtils.class);

	public static void disableAction() {
		gameEntityEngine.getSystem(AiSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(ConeDistributionSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(VelocityFieldSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(ForceFieldSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(MovementSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(CollisionSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(LifeSystem.class).setProcessing(false);
		final EntitySystem inputDesktop = gameEntityEngine.getSystem(InputDesktopSystem.class);
		if (inputDesktop != null)
			inputDesktop.setProcessing(false);
		cameraEntity.add(gameEntityEngine.createComponent(DisableAllExceptTheseComponent.class));
		playerHitCircleEntity.add(gameEntityEngine.createComponent(DisableAllExceptTheseComponent.class));
		playerBattleStigmaEntity.add(gameEntityEngine.createComponent(DisableAllExceptTheseComponent.class));
		enemyTargetCrossEntity.add(gameEntityEngine.createComponent(DisableAllExceptTheseComponent.class));
	}

	public static void enableAction() {
		gameEntityEngine.getSystem(AiSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(ConeDistributionSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(VelocityFieldSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(ForceFieldSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(MovementSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(CollisionSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(LifeSystem.class).setProcessing(true);
		final EntitySystem inputDesktop = gameEntityEngine.getSystem(InputDesktopSystem.class);
		if (inputDesktop != null)
			inputDesktop.setProcessing(true);
		cameraEntity.remove(DisableAllExceptTheseComponent.class);
		playerHitCircleEntity.remove(DisableAllExceptTheseComponent.class);
		playerBattleStigmaEntity.remove(DisableAllExceptTheseComponent.class);
		enemyTargetCrossEntity.remove(DisableAllExceptTheseComponent.class);
	}
}
