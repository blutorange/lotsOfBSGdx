package de.homelab.madgaksha.enums;

import de.homelab.madgaksha.entityengine.entity.MakerUtils;
import de.homelab.madgaksha.entityengine.entitysystem.CollisionSystem;

/**
 * To add new groups, change:
 * <ul>
 * <li>{@link MakerUtils#makeTriggerTouch}</li>
 * <li>{@link CollisionSystem}</li>
 * <li>Add the appropriate components in
 * {@link de.homelab.madgaksha.entityengine.component.collision}.
 * </ul>
 * 
 * @author madgaksha
 *
 */
public enum ECollisionGroup {
	GROUP_01,
	GROUP_02,
	GROUP_03,
	GROUP_04,
	GROUP_05;
	public final static ECollisionGroup PLAYER_GROUP = GROUP_01;
}
