package de.homelab.madgaksha.entityengine.entityutils;

import static de.homelab.madgaksha.GlobalBag.gameEntityEngine;

import com.badlogic.ashley.core.EntitySystem;

import de.homelab.madgaksha.entityengine.entitysystem.AiSystem;
import de.homelab.madgaksha.entityengine.entitysystem.CollisionSystem;
import de.homelab.madgaksha.entityengine.entitysystem.GrantPositionSystem;
import de.homelab.madgaksha.entityengine.entitysystem.InputPlayerDesktopSystem;
import de.homelab.madgaksha.entityengine.entitysystem.LifeSystem;
import de.homelab.madgaksha.entityengine.entitysystem.MovementSystem;
import de.homelab.madgaksha.logging.Logger;

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
		gameEntityEngine.getSystem(MovementSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(GrantPositionSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(CollisionSystem.class).setProcessing(false);
		//gameEntityEngine.getSystem(DamageSystem.class).setProcessing(false);
		gameEntityEngine.getSystem(LifeSystem.class).setProcessing(false);
		final EntitySystem inputDesktop = gameEntityEngine.getSystem(InputPlayerDesktopSystem.class);
		if (inputDesktop != null)
			inputDesktop.setProcessing(false);
	}

	public static void enableAction() {
		gameEntityEngine.getSystem(AiSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(MovementSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(GrantPositionSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(CollisionSystem.class).setProcessing(true);
		//gameEntityEngine.getSystem(DamageSystem.class).setProcessing(true);
		gameEntityEngine.getSystem(LifeSystem.class).setProcessing(true);
		final EntitySystem inputDesktop = gameEntityEngine.getSystem(InputPlayerDesktopSystem.class);
		if (inputDesktop != null)
			inputDesktop.setProcessing(true);
	}

	public static void disableActionExceptCamera() {
		disableAction();
	}

	public static void disableActionExceptGrantPosition() {
		disableAction();
		gameEntityEngine.getSystem(GrantPositionSystem.class).setProcessing(true);

	}
}
