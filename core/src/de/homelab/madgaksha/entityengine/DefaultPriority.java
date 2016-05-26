package de.homelab.madgaksha.entityengine;

/**
 * Global configuration for the default priority of each entity system. This
 * priority can be overridden with the appropriate constructor for an entity
 * system.
 * 
 * The lower the priority, the earlier the system is run.
 * 
 * @author madgaksha
 *
 */
public class DefaultPriority {
	private DefaultPriority() {
	}

	public final static int temporalSystem = 0;
	
	// updating
	public final static int cameraZoomingSystem = 1;
	public final static int danmakuSystem = 2;
	public final static int grantPositionSystem = 3;
	public final static int grantRotationSystem = 4;
	public final static int grantScaleSystem = 5;
	public final static int inputPlayerDesktopSystem = 6;
	public final static int newtonianForceSystem = 7;
	public final static int movementSystem = 8;
	public final static int collisionSystem = 9;

	public final static int damageSystem = 10;
	public final static int postEffectSystem = 11;	

	// drawing
	public final static int viewportUpdateSystem = 12;
	public final static int birdsViewSpriteSystem = 13;
	public final static int spriteAnimationSystem = 14;
	public final static int spriteRenderSystem = 15;
	public final static int particleEffectRenderSystem = 16;
}
