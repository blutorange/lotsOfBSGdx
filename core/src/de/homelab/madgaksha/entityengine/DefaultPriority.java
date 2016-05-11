package de.homelab.madgaksha.entityengine;

/**
 * Global configuration for the default priority of each
 * entity system. This priority can be overridden with
 * the appropriate constructor for an entity system.
 * 
 * The lower the priority, the earlier the system is run.
 * @author madgaksha
 *
 */
public class DefaultPriority {
	private DefaultPriority() {}
	public final static int cameraZoomingSystem = 0;
	public final static int danmakuSystem = 1;
	public final static int grantPositionSystem = 2;
	public final static int grantRotationSystem = 3;
	public final static int newtonianForceSystem = 4;
	public final static int movementSystem = 5;
	public final static int collisionSystem = 6;
	public final static int viewportUpdateSystem = 7;
}
